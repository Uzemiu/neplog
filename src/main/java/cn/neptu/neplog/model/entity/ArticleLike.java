package cn.neptu.neplog.model.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "article_like", indexes = {
        @Index(name = "article_like_target_id", columnList = "target_id")})
public class ArticleLike extends BaseLike {
}
