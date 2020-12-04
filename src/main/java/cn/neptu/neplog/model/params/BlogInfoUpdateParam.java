package cn.neptu.neplog.model.params;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogInfoUpdateParam {

    @NotNull(message = "博客名称不能为空")
    @Length(max = 31,message = "博客名称不能超过{max}个字符")
    @ApiParam("博客名称")
    private String name;

    @Length(max = 31,message = "自定义地址长度不能超过{max}个字符")
    @ApiParam("自定义URL")
    private String customUrl;

    @Length(max = 255,message = "欢迎标语不能超过{max}个字符")
    @ApiParam("欢迎标语")
    private String welcomeMessage;

}
