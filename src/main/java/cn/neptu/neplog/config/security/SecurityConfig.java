package cn.neptu.neplog.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "neplog.security")
public class SecurityConfig {

    /**
     * Redis验证码前缀
     */
    private String captchaPrefix;

    /**
     * jwt签名密钥
     */
    private String secret;

    /**
     * AES加密密钥
     */
    private String AESKey;

    /**
     * jwt过期时间（小时）
     */
    private int tokenExpireTime;

    /**
     * jwt有效期小于该时间则续期（小时）
     */
    private int tokenRefreshTime;

}
