package cn.neptu.neplog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleViewDTO extends ArticleBaseDTO {

    private String htmlContent;
}
