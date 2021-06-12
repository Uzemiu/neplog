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
     * AES加密密钥
     */
    private String AESKey;

    /**
     * token过期时间（小时）
     */
    private int tokenExpireTime;

    /**
     * token有效期小于该时间则续期（小时）
     */
    private int tokenRefreshTime;

    /**
     * 如果设置为true，启动时会将博客密码设置为neplog
     */
    private boolean resetPassword;

}
