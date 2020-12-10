package cn.neptu.neplog.model.dto;

import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleBaseDTO {

    private Integer id;

    private String title;

    private String summary;

    private String cover;

    private Integer views;

    private Integer likes;

    private Integer comments;

    private CategoryDTO category;

    private List<String> tags;

    private Date createTime;

    private Date updateTime;

}
