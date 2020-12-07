package cn.neptu.neplog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

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
    @Column(name = "article_id")
    private Integer id;

    @Column(name = "title",length = 255,nullable = false)
    private String title;

    @Column(name = "summary",length = 255)
    @ColumnDefault("''")
    private String summary;

    @Column(name = "content",nullable = false)
    @Lob
    private String content;

    @Column(name = "html_content",nullable = false)
    @Lob
    private String htmlContent;

    @Column(name = "cover",length = 1023)
    @ColumnDefault("''")
    private String cover;

    @Column(name = "priority")
    @ColumnDefault("0")
    private Integer priority;

    /**
     * 0 Draft
     * 5 Private
     * 10 Public
     */
    @Column(name = "status")
    @ColumnDefault("0")
    private Integer status;

    @Column(name = "views")
    @ColumnDefault("0")
    private Integer views;

    @Column(name = "likes")
    @ColumnDefault("0")
    private Integer likes;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Override
    public void prePersist(){
        super.prePersist();
        if(title == null || title.equals("")){
            title = "Untitled";
        }
        if(content == null){
            content = "Edit not";
        }
        if(htmlContent == null){
            htmlContent = "";
        }
        if(summary == null){
            summary = "Edit now";
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
    }

}
