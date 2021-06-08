package cn.neptu.neplog.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "comment", indexes = {
        @Index(name = "comment_article_id", columnList = "article_id"),
        @Index(name = "comment_parent_id", columnList = "parent_id")})
public class ArticleComment extends BaseComment{

}
