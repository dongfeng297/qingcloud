package qingcloud.controller;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.dto.LoginDTO;
import qingcloud.dto.Result;
import qingcloud.service.UserService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "测试接口")
    @GetMapping("/test")
    public String test(){
        return "hello";
    }
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
    @ApiOperation(value = "发送验证码")
    @PostMapping("/code")
    public Result sendCode(@RequestParam("emali") String email) throws MessagingException {
        return userService.sendCode(email);
    }


}
