package qingcloud.service.serviceImpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import qingcloud.constant.RedisConstant;
import qingcloud.dto.LoginDTO;
import qingcloud.dto.Result;
import qingcloud.dto.UserDTO;
import qingcloud.entity.User;
import qingcloud.entity.Voucher;
import qingcloud.mapper.UserMapper;
import qingcloud.mapper.VoucherMapper;
import qingcloud.mapper.VoucherOrderMapper;
import qingcloud.service.UserService;
import qingcloud.utils.MailUtils;
import qingcloud.utils.RegexUtils;
import qingcloud.vo.VoucherVO;

import javax.mail.MessagingException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static qingcloud.constant.RedisConstant.LOGIN_CODE_KEY;
import static qingcloud.constant.RedisConstant.LOGIN_USER_TTL;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;

    @Autowired
    private VoucherMapper voucherMapper;

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
        MailUtils.sendMail(email, "尊敬的用户:你好!\n注册验证码为:" + code + "(有效期为一分钟,请勿告知他人)");

        return Result.ok();
    }

    @Override
    public Result getVouchers(Long userId) {
       List<Long> ids= voucherOrderMapper.getVoucherIds(userId);
       if(ids==null ||ids.isEmpty()){
           return Result.ok(Collections.emptyList());
       }
       List<Voucher> vouchers=voucherMapper.getVouchers(ids);
        List<VoucherVO> voucherVos = vouchers.stream()
                .map(voucher -> BeanUtil.copyProperties(voucher, VoucherVO.class))
                .collect(Collectors.toList());
        return Result.ok(voucherVos);
    }

    //扣减余额
    @Override
    public boolean deductBalance(Long userId, BigDecimal payAmount) {
        BigDecimal balance = userMapper.getBalance(userId);
        if(balance.compareTo(payAmount) < 0){
            return false;
        }
        userMapper.updateBalance(userId,payAmount);
        return true;

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
            User user = new User();
            user.setEmail(email);
            user.setUsername("user_" + System.currentTimeMillis());
            user.setId((long) (i + 62));
            userMapper.saveWithId(user);

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
