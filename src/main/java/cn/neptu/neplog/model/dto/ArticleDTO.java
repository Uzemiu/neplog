package cn.neptu.neplog.model.dto;

import cn.neptu.neplog.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
public class ArticleDTO{
    private Integer id;

    private String title;

    private String summary;

    private String cover;

    private CategoryDTO category;

    private List<String> tags;

    private Date createTime;

    private Date updateTime;

    private String content;

    private String htmlContent;

    private Integer commentPermission;

    private Integer viewPermission;

    private Integer priority;

    private Integer status;

}
