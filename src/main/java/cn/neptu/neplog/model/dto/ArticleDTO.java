package cn.neptu.neplog.model.dto;

import cn.neptu.neplog.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleDTO extends BaseArticleDTO{

    private String content;

    private String htmlContent;

    private Integer priority;

    private Integer status;

}
