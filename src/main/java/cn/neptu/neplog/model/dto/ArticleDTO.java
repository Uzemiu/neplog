package cn.neptu.neplog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleDTO extends ArticleBaseDTO{

    private String content;

    private Integer commentPermission;

    private Integer viewPermission;

    private Integer priority;

    private Integer status;

    private Boolean deleted;

}
