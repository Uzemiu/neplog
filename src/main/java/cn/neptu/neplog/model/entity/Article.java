package cn.neptu.neplog.model.entity;

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
    @Column(name = "article_id")
    private Long id;

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
     * 4 Published
     */
    @Column(name = "status")
    @ColumnDefault("0")
    private Integer status;

    @Column(name = "views")
    @ColumnDefault("1")
    private Long views;

    @Column(name = "likes")
    @ColumnDefault("0")
    private Long likes;

    @Column(name = "comments")
    @ColumnDefault("0")
    private Long comments;

    /**
     * 0 Anybody
     * 4 Require review
     * 8 User only
     * 16 Closed(Owner only)
     */
    @Column(name = "comment_permission")
    @ColumnDefault("0")
    private Integer commentPermission;

    /**
     * 0 Anybody
     * 8 User only
     * 16 Private
     */
    @Column(name = "view_permission")
    @ColumnDefault("0")
    private Integer viewPermission;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "deleted")
    @ColumnDefault("false")
    private Boolean deleted;

    @Override
    public void prePersist(){
        if(title == null || "".equals(title)){
            title = "Untitled";
        }
        if(content == null){
            content = "### Edit now";
        }
        if(htmlContent == null){
            htmlContent = "<h3>Edit now</h3>";
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
            views = 1L;
        }
        if(likes == null){
            likes = 0L;
        }
        if(comments == null){
            comments = 0L;
        }
        if(viewPermission == null){
            viewPermission = 0;
        }
        if(commentPermission == null){
            commentPermission = 0;
        }
        deleted = false;
    }
}
