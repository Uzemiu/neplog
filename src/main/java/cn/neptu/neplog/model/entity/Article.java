package cn.neptu.neplog.model.entity;

import cn.neptu.neplog.model.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 博客文章
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Article extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title",columnDefinition = "varchar(255) not null")
    private String title;

    @Column(name = "summary", columnDefinition = "varchar(255) default ''")
    private String summary;

    @Column(name = "content", columnDefinition = "longtext not null")
    private String content;

    @Column(name = "html_content", columnDefinition = "longtext not null")
    private String htmlContent;

    @Column(name = "privacy", columnDefinition = "boolean default false")
    private Boolean privacy;

    @Column(name = "cover", columnDefinition = "varchar(1023) default ''")
    private String cover;

    @Column(name = "priority", columnDefinition = "int default 0")
    private Integer priority;

    @Column(name = "status", columnDefinition = "int default 0")
    private Integer status;

    @Column(name = "views", columnDefinition = "int default 0")
    private Integer views;

    @Column(name = "likes", columnDefinition = "int default 0")
    private Integer likes;

    @Column(name = "category", columnDefinition = "int default 0")
    private Integer category;

    @Override
    public void prePersist(){
        super.prePersist();
        if(title == null || title.equals("")){
            title = "Untitled";
        }
        if(content == null){
            content = "";
        }
        if(htmlContent == null){
            htmlContent = "";
        }
        if(summary == null || summary.length() == 0){
            summary = ""; // TODO generate summary
        }
        if(privacy == null){
            privacy = false;
        }
        if(cover == null){
            cover = ""; // TODO default cover
        }
        if(priority == null){
            priority = 0;
        }
        if(status == null){
            status = 0;
        }
        if(views == null){
            views = 1;
        }
        if(likes == null){
            likes = 0;
        }
        if(category == null){
            category = 0;
        }
    }

}
