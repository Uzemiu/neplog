package cn.neptu.neplog.model.params;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegisterParam {

    @Pattern(regexp = "^[0-9A-Za-z]{6,31}$",
            message = "用户名长度须在6~32个字符之间且只能由字母和数字组成")
    private String username;

    private String password;

    @Length(min = 2,max = 32,
            message = "昵称长度须在6~32个字符之间")
    private String nickname;

    @Email(message = "邮件可是不合法")
    private String email;

    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @NotBlank
    private String uuid;
}
