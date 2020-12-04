package cn.neptu.neplog.model.params;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloggerInfoUpdateParam {

    @Length(min = 3,max = 31,message = "昵称长度在3~31字符之间")
    @ApiParam(value = "用户昵称",allowableValues = "range[3,31]")
    private String nickname;

    @Email(message = "请输入合法的邮箱地址")
    @ApiParam(value = "用户Email",allowableValues = "range[3,31]")
    private String email;

    @Length(max = 127,message = "个人简介长度须小于等于127个字符")
    @ApiParam(value = "用户个人简介、签名",allowableValues = "range[0,127]")
    private String description;
}
