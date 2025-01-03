package qingcloud.service.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import qingcloud.constant.RedisConstant;
import qingcloud.dto.LoginDTO;
import qingcloud.dto.Result;
import qingcloud.dto.UserDTO;
import qingcloud.entity.User;
import qingcloud.mapper.UserMapper;
import qingcloud.service.UserService;
import qingcloud.utils.MailUtils;
import qingcloud.utils.RegexUtils;

import javax.mail.MessagingException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static qingcloud.constant.RedisConstant.LOGIN_CODE_KEY;
import static qingcloud.constant.RedisConstant.LOGIN_USER_TTL;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(LoginDTO loginDTO) {
        //1.校验邮箱格式
        String emali = loginDTO.getEmail();
        if (RegexUtils.isEmailInvalid(emali)) {
            return Result.fail("邮箱格式错误");
        }
        String code = loginDTO.getCode();
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + emali);
        //2.比较验证码
        if (cacheCode == null || !cacheCode.equals(code)) {
            return Result.fail("验证码错误");
        }

        //3.用户不存在
        User user = userMapper.getByEmali(emali);
        if (user == null) {
            user = createUserWithEmail(emali);
        }


        //4.保存用户到redis
        //生成随机token
        String token = UUID.randomUUID().toString(true);
        //将用户对象转为hashmap
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userDTO.getId() + "");
        map.put("username", userDTO.getUsername());
        map.put("avatar", userDTO.getAvatar());

        //保存到redis
        String key = RedisConstant.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(key, map);

        //设置过期时间
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        //删除验证信息
        stringRedisTemplate.delete(LOGIN_CODE_KEY + emali);
        return Result.ok(token);

    }


    @Override
    public Result sendCode(String email) throws MessagingException {
        //校验邮箱
        if (RegexUtils.isEmailInvalid(email)) {
            return Result.fail("邮箱格式错误");
        }
        //发送验证码
        String code = MailUtils.achieveCode();
        //保存到redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + email, code);
        stringRedisTemplate.expire(LOGIN_CODE_KEY + email, RedisConstant.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        MailUtils.sendMail(email, code);

        return Result.ok();
    }

    private User createUserWithEmail(String emali) {
        User user = new User();
        user.setEmail(emali);
        user.setUsername("user_" + System.currentTimeMillis());
        userMapper.save(user);
        return user;
    }



    //生成1000个token TODO 删除

    public void generateAndSaveUsers(int count) {
        for (int i = 0; i < count; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String email = "user" + i + "@example.com";
            User user = createUserWithEmail(email);

            // 为每个用户生成token并保存到Redis
            saveUserTokenToRedis(user);
        }
    }

    private void saveUserTokenToRedis(User user) {
        // 生成随机token
        String token = UUID.randomUUID().toString(true);
        // 将用户对象转为hashmap
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userDTO.getId() + "");
        map.put("username", userDTO.getUsername());
        map.put("avatar", userDTO.getAvatar());

        // 保存到redis
        String key = RedisConstant.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(key, map);

        // 设置过期时间
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 将token保存到本地txt文件
        saveTokenToFile(token);
    }


    private void saveTokenToFile(String token) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tokens.txt", true))) {
            writer.write(token);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
