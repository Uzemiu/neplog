package cn.neptu.neplog.model.entity;

import cn.neptu.neplog.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@MappedSuperclass
public class BaseArticle extends BaseEntity{

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
     * 0 草稿
     * 4 公开
     */
    @Column(name = "status")
    @ColumnDefault("0")
    private Integer status;

    /**
     * 只能通过{@link ArticleRepository#updateViews}修改
     */
    @Column(name = "views", updatable = false)
    @ColumnDefault("1")
    private Long views;

    /**
     * 只能通过{@link ArticleRepository#updateLikes}修改
     */
    @Column(name = "likes", updatable = false)
    @ColumnDefault("0")
    private Long likes;

    /**
     * 只能通过{@link ArticleRepository#updateComments}修改
     */
    @Column(name = "comments", updatable = false)
    @ColumnDefault("0")
    private Long comments;

    /**
     * 0 任何人
     * 16 关闭(仅限博主)
     */
    @Column(name = "comment_permission")
    @ColumnDefault("0")
    private Integer commentPermission;

    /**
     * 0 任何人
     * 16 私有
     */
    @Column(name = "view_permission")
    @ColumnDefault("0")
    private Integer viewPermission;

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
            cover = "";
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
