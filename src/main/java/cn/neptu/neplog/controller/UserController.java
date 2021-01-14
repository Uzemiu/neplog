package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.dto.UserDTO;
import cn.neptu.neplog.model.params.LoginParam;
import cn.neptu.neplog.model.params.RegisterParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.model.support.VerificationCode;
import cn.neptu.neplog.service.UserService;
import cn.neptu.neplog.service.mapstruct.UserMapper;
import cn.neptu.neplog.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final VerificationCodeUtil verificationCodeUtil;
    private final RedisUtil redisUtil;
    private final AESUtil aesUtil;
    private final TokenUtil tokenUtil;
    private final UserMapper userMapper;
    private final UserService userService;

    @AnonymousAccess
    @PostMapping("/login")
    public BaseResponse<?> login(@Validated @RequestBody LoginParam param){
        if(SecurityUtil.getCurrentUser() != null){
            throw new BadRequestException("你已经登陆过了");
        }
        verificationCodeUtil.verify(new VerificationCode(param.getCaptcha(),null,param.getUuid()));

        String plainPassword = aesUtil.decrypt(param.getPassword());
        User user = userService.findByUsername(param.getUsername()).orElseThrow(() -> new BadRequestException("用户名或密码错误"));
        Assert.isTrue(BCrypt.checkpw(plainPassword,user.getPassword()),"用户名或密码错误");

        Map<String, Object> res = new HashMap<String, Object>(2){{
            put("user",userMapper.toDto(user));
            put("token", TokenUtil.TOKEN_PREFIX + tokenUtil.generateToken(user));
        }};
        return BaseResponse.ok("ok",res);
    }

    @GetMapping("/info")
    @AnonymousAccess
    public BaseResponse<UserDTO> getUserInfo(){
        return BaseResponse.ok("ok",userMapper.toDto(SecurityUtil.getCurrentUser()));
    }

    @PostMapping("/logout")
    public BaseResponse<?> logout(HttpServletRequest request){
        String token = TokenUtil.resolveToken(request);
        redisUtil.del(token);
        return BaseResponse.ok("您已退出登录");
    }

    @AnonymousAccess
    @PostMapping("/register")
    public BaseResponse<?> register(@Validated @RequestBody RegisterParam param){
        verificationCodeUtil.verify(new VerificationCode(param.getCaptcha(),null,param.getUuid()));
        param.setPassword(aesUtil.decrypt(param.getPassword()));
        userService.register(param);
        return BaseResponse.ok();
    }

    @AnonymousAccess
    @GetMapping("/captcha")
    public BaseResponse<?> captcha(){
        VerificationCode captcha = verificationCodeUtil.generateImageCaptcha();
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.getEntity());
            put("uuid", captcha.getUuid());
        }};
        return BaseResponse.ok("ok",imgResult);
    }
}
