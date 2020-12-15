package cn.neptu.neplog.model.params;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class InstallParam {

    @Length(min = 1, max = 32, message = "博客名称长度在1~32个字符内")
    private String blogName;

    @Pattern(regexp = "^[0-9A-Za-z]{6,31}$",
            message = "用户名长度在6~32个字符内且只能由字母和数字组成")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Length(min = 1, max = 32, message = "昵称长度长度在1~32个字符内")
    private String nickname;

    @Email
    private String email;

}
