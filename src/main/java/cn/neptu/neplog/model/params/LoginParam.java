package cn.neptu.neplog.model.params;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {

    @NotBlank(message = "用户名不能为空")
    @ApiParam(value = "用户名",required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiParam(value = "密码",required = true)
    private String password;

    @Null
    private String ip;

}
