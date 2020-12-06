package cn.neptu.neplog.utils;

import cn.neptu.neplog.config.security.SecurityConfig;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.support.VerificationCode;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VerificationCodeUtil {

    private final RedisUtil redisUtil;
    private final SecurityConfig securityConfig;

    public VerificationCode generateImageCaptcha(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111,36,2);
        String verCode = captcha.text().toLowerCase();
        if(verCode.contains(".")){
            verCode = verCode.split("\\.")[0];
        }
        String uuid = UUID.randomUUID().toString();
        redisUtil.set(securityConfig.getCaptchaPrefix() + uuid, verCode, 2, TimeUnit.MINUTES);
        return new VerificationCode(verCode,captcha.toBase64(), uuid);
    }

    public boolean verify(VerificationCode code){
        String captcha = (String) redisUtil.get(securityConfig.getCaptchaPrefix() + code.getUuid());
        redisUtil.del(securityConfig.getCaptchaPrefix() + code.getUuid());
        if (Strings.isBlank(captcha)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (Strings.isBlank(code.getCode()) || !code.getCode().equalsIgnoreCase(captcha)) {
            throw new BadRequestException("验证码错误");
        }
        return true;
    }
}
