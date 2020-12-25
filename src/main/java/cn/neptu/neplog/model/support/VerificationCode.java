package cn.neptu.neplog.model.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
