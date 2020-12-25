package cn.neptu.neplog.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
//@Entity
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content",length = 1023,nullable = false)
    private String content;

    @Column(name = "nickname",nullable = false)
    private String nickname;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "article_id",nullable = false)
    private Integer articleId;

    @Column(name = "father_id")
    private Integer fatherId;

    @Column(name = "like")
    @ColumnDefault("0")
    private Integer like;

    @Column(name = "dislike")
    @ColumnDefault("0")
    private Integer dislike;

    @Column(name = "user_agent")
    @ColumnDefault("Unknown")
    private String userAgent;

    @Column(name = "operating_system")
    @ColumnDefault("Unknown")
    private String operatingSystem;

    @Override
    protected void prePersist() {
        super.prePersist();
        if(like == null){
            like = 0;
        }
        if(dislike == null){
            dislike = 0;
        }
        if(!StringUtils.hasText(userAgent)){
            userAgent = "Unknown";
        }
        if(!StringUtils.hasText(operatingSystem)){
            userAgent = "Unknown";
        }
    }
}
