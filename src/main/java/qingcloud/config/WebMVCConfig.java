package qingcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import qingcloud.utils.LoginInterceptor;
import qingcloud.utils.RefreshTokenInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/login",
                        "/user/code",
                        "/doc.html",
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**"
                ).order(1);

        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
    }

}
