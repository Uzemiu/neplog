package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.annotation.LevelRequiredAccess;
import cn.neptu.neplog.constant.LevelConstant;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.params.LoginParam;
import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.params.ResetPasswordParam;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.model.support.VerificationCode;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.mapstruct.UserMapper;
import cn.neptu.neplog.utils.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final VerificationCodeUtil verificationCodeUtil;
    private final UserMapper userMapper;
    private final UserService userService;

    @ApiOperation(value = "用户登录", notes = "返回{user: UserDTO, token: String}")
    @AnonymousAccess
    @PostMapping("/login")
    public BaseResponse<Map<String, Object>> login(@Validated @RequestBody LoginParam param){
        return BaseResponse.ok("登陆成功", userService.login(param));
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    @AnonymousAccess
    public BaseResponse<UserDTO> getUserInfo(){
        return BaseResponse.ok("ok",userMapper.toDto(SecurityUtil.getCurrentUser()));
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public BaseResponse<?> logout(HttpServletRequest request){
        userService.logout(request);
        return BaseResponse.ok("您已退出登录");
    }

    @ApiOperation("注册")
    @AnonymousAccess
    @PostMapping("/register")
    public BaseResponse<?> register(@Validated @RequestBody RegisterParam param){
        userService.register(param);
        return BaseResponse.ok("注册成功");
    }

    @ApiOperation("更新用户信息")
    @LevelRequiredAccess(1)
    @PutMapping
    public BaseResponse<UserDTO> update(@RequestBody UserDTO userDTO){
        if(!SecurityUtil.isOwner()){
            // 非博主不能修改用户名
            userDTO.setUsername(SecurityUtil.getCurrentUser().getUsername());
        }
        return BaseResponse.ok("更新用户信息成功", userMapper.toDto(userService.update(userDTO)));
    }

    @ApiOperation("重置密码")
    @LevelRequiredAccess(1)
    @PostMapping("/resetPassword")
    public BaseResponse<?> resetPassword(@RequestBody ResetPasswordParam param){
        userService.resetPassword(param);
        return BaseResponse.ok("重置密码成功");
    }

    @ApiOperation("获取验证码")
    @AnonymousAccess
    @GetMapping("/captcha")
    public BaseResponse<Map<String, Object>> captcha(){
        VerificationCode captcha = verificationCodeUtil.generateImageCaptcha();
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.getEntity());
            put("uuid", captcha.getUuid());
        }};
        return BaseResponse.ok("ok",imgResult);
    }
}
