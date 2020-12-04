package cn.neptu.neplog.model.params;


import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterParam {

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,31}$",
            message = "用户名须在6~31位且仅能由数字和字母组成")
    @ApiParam(value = "用户名，仅能由数字和字母组成",allowableValues = "range[6,31]",example = "xzytql",required = true)
    private String username;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![.~!@#$%^&*]+$)[0-9A-Za-z.~!@#$%^&*]{8,31}$",
            message = "密码须在8~31位且须由数字、字母、字符(.~!@#$%^&*)中两者以上组成")
    @ApiParam(value = "密码，由数字、字母、字符(.~!@#$%^&*)中两者以上组成",allowableValues = "range[8,31]",example = "xzyyyds0",required = true)
    private String password;

    @NotNull(message = "昵称不能为空")
    @Length(min = 3,max = 31,message = "昵称长度在3~31字符之间")
    @ApiParam(value = "昵称",allowableValues = "range[3,31]",required = true)
    private String nickname;

    @Email(message = "请输入合法的邮箱地址")
    @ApiParam(value = "邮箱")
    private String email;

    @NotBlank(message = "邀请码不能为空")
    @ApiParam(value = "邀请码",required = true)
    private String inviteCode;

}
