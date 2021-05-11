package cn.neptu.neplog.model.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private Long id;

    private String name;

    private Long parentId;

    private Long articleCount;
}
