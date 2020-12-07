package cn.neptu.neplog.model.dto;

import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class BaseArticleDTO{

    private Integer id;

    private String title;

    private String summary;

    private String cover;

    private Integer views;

    private Integer likes;

    private Integer comments;

    private Category category;

    private Set<Tag> tags;

    private Date createTime;

    private Date updateTime;

}
