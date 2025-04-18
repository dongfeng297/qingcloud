package qingcloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.annotation.Log;
import qingcloud.dto.LoginDTO;
import qingcloud.dto.Result;
import qingcloud.service.UserService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    @Log(value = "登录接口")
    public Result login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
    @ApiOperation(value = "发送验证码")
    @PostMapping("/code")
    @Log(value = "发送验证码")
    public Result sendCode(@RequestParam("emali") String email) throws MessagingException {
        return userService.sendCode(email);
    }

    @ApiOperation(value = "获取优惠券列表")
    @GetMapping("/voucher")
    public Result getVouchers( @RequestParam("id") Long userId){
        return userService.getVouchers(userId);
    }

}
