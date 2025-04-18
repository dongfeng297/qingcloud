package qingcloud.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import qingcloud.annotation.Log;
import qingcloud.dto.Result;
import qingcloud.entity.ApiAccessLog;
import qingcloud.mapper.ApiAccessLogMapper;
import qingcloud.utils.UserHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Autowired
    private ApiAccessLogMapper apiAccessLogMapper;

    @Around("@annotation(qingcloud.annotation.Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ApiAccessLog apiAccessLog = new ApiAccessLog();

        try {
            // 设置基本信息
            apiAccessLog.setStartTime(LocalDateTime.now());
            apiAccessLog.setUsername(UserHolder.getUser() != null ? UserHolder.getUser().getUsername() : "unknown");

            // 获取方法和注解信息
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);

            apiAccessLog.setMethodName(method.getName());
            apiAccessLog.setOperationDesc(logAnnotation.value());

            // 获取请求信息
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                apiAccessLog.setRequestUrl(request.getRequestURL().toString());
                apiAccessLog.setRequestMethod(request.getMethod());
                apiAccessLog.setIpAddress(request.getRemoteAddr());
                apiAccessLog.setStatusCode(requestAttributes.getResponse().getStatus());
            }

            // 执行目标方法
            Object result = joinPoint.proceed();

            // 处理返回结果

            String errorMsg = ((Result) result).getErrorMsg();
            if (errorMsg != null) {
                apiAccessLog.setErrorMessage(errorMsg);
            }


            return result;
        } catch (Exception e) {
            apiAccessLog.setErrorMessage(e.getMessage());
            apiAccessLog.setStatusCode(500);
            throw e;
        } finally {
            // 设置执行时间并保存日志
            apiAccessLog.setExecutionTime(System.currentTimeMillis() - startTime);
            try {
                apiAccessLogMapper.insert(apiAccessLog);
            } catch (Exception e) {
                log.error("保存访问日志失败", e);
            }
        }
    }
}