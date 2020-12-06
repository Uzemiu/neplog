package cn.neptu.neplog.model.entity;

import cn.neptu.neplog.model.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "title",length = 255,nullable = false)
    private String title;

    @Column(name = "summary",length = 255)
    @ColumnDefault("")
    private String summary;

    @Column(name = "content",nullable = false)
    @Lob
    private String content;

    @Column(name = "html_content",nullable = false)
    @Lob
    private String htmlContent;

    @Column(name = "privacy")
    private Boolean privacy;

    @Column(name = "cover",length = 1023)
    @ColumnDefault("")
    private String cover;

    @Column(name = "priority")
    @ColumnDefault("0")
    private Integer priority;

    @Column(name = "status")
    @ColumnDefault("0")
    private Integer status;

    @Column(name = "views")
    @ColumnDefault("0")
    private Integer views;

    @Column(name = "likes")
    @ColumnDefault("0")
    private Integer likes;

    @Column(name = "category")
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
