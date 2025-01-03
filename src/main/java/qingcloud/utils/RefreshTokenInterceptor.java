package qingcloud.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.mail.util.BEncoderStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import qingcloud.constant.RedisConstant;
import qingcloud.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static qingcloud.constant.RedisConstant.LOGIN_USER_KEY;
import static qingcloud.constant.RedisConstant.LOGIN_USER_TTL;
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.看有无token
        String token = request.getHeader("Authorization");
        log.debug("token:{}",token);
        if(StrUtil.isBlank(token)){
            return true;
        }
        //2.查询redis
        Map <Object, Object> map = stringRedisTemplate.opsForHash().entries(LOGIN_USER_KEY +token);
        if(map.isEmpty()){
            return true;
        }
        UserDTO userDto = new UserDTO();
        BeanUtil.fillBeanWithMap(map,userDto, false);
        //3.保存到ThreadLocal
        UserHolder.setUser(userDto);
        //4.刷新token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
