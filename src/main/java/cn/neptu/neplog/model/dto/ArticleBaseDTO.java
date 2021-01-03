package cn.neptu.neplog.model.dto;

import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ArticleBaseDTO {

    private Long id;

    private String title;

    private String summary;

    private String cover;

    private Long views;

    private Long likes;

    private Long comments;

    private String htmlContent;

    private String category;

    private List<String> tags;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;

    private Boolean deleted;

}
