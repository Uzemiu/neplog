package cn.neptu.neplog.model.params;

import cn.neptu.neplog.model.enums.PostStatus;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostParam {

    @ApiParam("博客ID，传NULL值时为创建博客，否则更新博客")
    private Integer id;

    @Length(min = 1,max = 255,message = "标题不能超过{max}个字符")
    @NotBlank(message = "标题不能为空")
    @ApiParam(value = "博客标题",allowableValues = "range[1,255]",required = true)
    private String title;

    @Length(min = 0,max = 255,message = "摘要不能超过{max}个字符")
    @ApiParam(value = "博客摘要，传NULL值时自动生成",allowableValues = "range[0,255]")
    private String summary;

    @NotBlank(message = "文章内容不能为空")
    @ApiParam(value = "Markdown文章内容",required = true)
    private String content;

    @ApiParam(value = "HTML文章内容")
    private String htmlContent;

    @ApiParam(value = "是否为私密文章")
    private Boolean privacy;

    @Length(max = 1023,message = "封面图地址须小于{max}个字符")
    @ApiParam(value = "封面图URL")
    private String cover;

    @ApiParam(value = "文章重要性，默认为1，置顶则另外传值")
    private Integer priority;

    @ApiParam(value = "是否为草稿",example = "DRAFT|PUBLISHED")
    private PostStatus status;

    @ApiParam(value = "文章标签")
    private List<String> tags;

    @NotNull
    @ApiParam(value = "文章分类ID",required = true)
    private Integer categoryId;

    @ApiParam(value = "是否为转载文章")
    private Boolean shared;
}
