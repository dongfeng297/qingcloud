package qingcloud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import qingcloud.service.serviceImpl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.FileReader;

@EnableAsync
@SpringBootTest
class QingCloudApplicationTests {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void generateUsers() {
        userService.generateAndSaveUsers(1000);
    }




}
