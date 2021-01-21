package cn.neptu.neplog.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class ArticleDTO {

    private Long id;

    @NotBlank(message = "文章标题不能为空")
    private String title;

    private String summary;

    private String cover;

    private Long views;

    private Long likes;

    private Long comments;

    private String htmlContent;

    private String content;

    private Integer priority;

    private Integer status;

    private Integer commentPermission;

    private Integer viewPermission;

    @NotBlank(message = "分类不能为空")
    private String category;

    private List<String> tags;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;

    private Boolean deleted;

}
