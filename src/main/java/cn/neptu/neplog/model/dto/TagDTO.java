package cn.neptu.neplog.model.dto;

import lombok.Data;

@Data
public class TagDTO {

    private Integer id;

    private Integer articleId;

    private String tag;
}
