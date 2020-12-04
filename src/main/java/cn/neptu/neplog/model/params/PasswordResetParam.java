package cn.neptu.neplog.model.params;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetParam {

    @NotBlank(message = "原密码不能为空")
    @ApiParam(value = "原密码",required = true)
    private String originalPassword;

    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![.~!@#$%^&*]+$)[0-9A-Za-z.~!@#$%^&*]{8,31}$",
            message = "密码须在8~31位且须由数字、字母、字符(.~!@#$%^&*)中两者以上组成")
    @ApiParam(value = "新密码",required = true)
    private String password;
}
