package cn.neptu.neplog.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    @Length(min = 1,max = 32,
            message = "昵称长度须在1~32个字符之间")
    private String nickname;

    @Length(max = 1023, message = "头像地址URL长度须在1023个字符以内")
    private String avatar;

    @Length(max = 1023, message = "个人站点URL长度须在1023个字符以内")
    private String site;

    @Email(message = "邮箱地址不合法")
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer level;
}
