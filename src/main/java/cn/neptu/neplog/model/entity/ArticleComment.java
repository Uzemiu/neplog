package cn.neptu.neplog.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class ArticleComment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content",length = 4095,nullable = false)
    private String content;

    @Column(name = "avatar",length = 1023)
    @ColumnDefault("''")
    private String avatar;

    @Column(name = "nickname",nullable = false)
    private String nickname;

    @Column(name = "link",length = 1023)
    @ColumnDefault("'#'")
    private String link;

    @Column(name = "email",length = 1023)
    @ColumnDefault("''")
    private String email;

    @Column(name = "article_id",nullable = false)
    private Integer articleId;

    @Column(name = "status")
    @ColumnDefault("0")
    private Integer status;

    @Column(name = "father_id")
    private Integer fatherId;

    @Column(name = "likes")
    @ColumnDefault("0")
    private Integer likes;

    @Column(name = "user_agent")
    @ColumnDefault("Unknown")
    private String userAgent;

    @Column(name = "operating_system")
    @ColumnDefault("Unknown")
    private String operatingSystem;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(likes == null){
            likes = 0;
        }
        if(!StringUtils.hasText(email)){
            email = "";
        }
        if(!StringUtils.hasText(link)){
            link = "#";
        }
        if(!StringUtils.hasText(userAgent)){
            userAgent = "Unknown";
        }
        if(!StringUtils.hasText(operatingSystem)){
            userAgent = "Unknown";
        }
    }
}
