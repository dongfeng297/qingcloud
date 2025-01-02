package qingcloud.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.mail.util.BEncoderStream;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import qingcloud.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.看有无token
        String token = request.getHeader("Authorization");
        if(StrUtil.isBlank(token)){
            return true;
        }
        //2.查询redis
        Map map = stringRedisTemplate.opsForHash().entries(token);
        UserDTO userDto = new UserDTO();
        BeanUtil.fillBeanWithMap(map,userDto, false);
        //3.保存到ThreadLocal
        UserHolder.setUser(userDto);
        return true;
    }
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        UserHolder.removeUser();
    }
}
