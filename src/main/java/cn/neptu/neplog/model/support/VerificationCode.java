package cn.neptu.neplog.model.support;

import cn.neptu.neplog.config.security.SecurityConfig;
import cn.neptu.neplog.utils.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {

    /**
     * 验证码本体
     */
    private String code;

    /**
     * 显示验证码的实体形式
     */
    private Object entity;

    /**
     * 用于Redis获取验证码
     */
    private String uuid;
}
