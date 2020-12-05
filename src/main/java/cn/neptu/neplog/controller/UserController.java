package cn.neptu.neplog.controller;

import cn.neptu.neplog.annotation.AnonymousAccess;
import cn.neptu.neplog.config.security.SecurityConfig;
import cn.neptu.neplog.model.dto.LoginParam;
import cn.neptu.neplog.model.support.BaseResponse;
import cn.neptu.neplog.utils.JwtUtil;
import cn.neptu.neplog.utils.RedisUtil;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SecurityConfig authenticationConfig;
    @Resource
    private JwtUtil jwtUtil;

    @AnonymousAccess
    @PostMapping("/login")
    public BaseResponse<String> login(@Validated LoginParam param){
        // check captcha
        String captcha = (String) redisUtil.get(authenticationConfig.getCaptchaPrefix() + param.getUuid());
        redisUtil.del(authenticationConfig.getCaptchaPrefix() + param.getUuid());
        if (Strings.isBlank(captcha)) {
//            throw new BadRequestException("验证码不存在或已过期");
        }
        if (Strings.isBlank(param.getCode()) || !param.getCode().equalsIgnoreCase(captcha)) { 
//            throw new BadRequestException("验证码错误");
        }

        // check username ppw


//        String jwt = JwtUtil.generateToken(authentication);
        String jwt = "";

        // save token to redis
        return new BaseResponse<>(200,"ok",JwtUtil.TOKEN_PREFIX + jwt);
    }

    @PostMapping("/logout")
    public BaseResponse<?> logout(HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
//        redisUtil.del(authenticationConfig.getJwtPrefix() + token);
        return BaseResponse.ok("您已退出登录");
    }

    @AnonymousAccess
    @GetMapping("/captcha")
    public BaseResponse<?> captcha(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130,48,2);
        String verCode = captcha.text().toLowerCase();
        if(verCode.contains(".")){
            verCode = verCode.split("\\.")[0];
        }
        String uuid =  UUID.randomUUID().toString();
        redisUtil.set(authenticationConfig.getCaptchaPrefix() + uuid, verCode, 2, TimeUnit.MINUTES);
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return BaseResponse.ok("ok",imgResult);
    }
}
