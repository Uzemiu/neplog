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
@Table(name = "article", indexes = {
        @Index(name = "article_category_id", columnList = "category_id"),
        @Index(name = "article_create_time", columnList = "create_time")})
public class Article extends BaseArticle{

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT,name = "none"))
    private Category category;

    @ManyToMany
    @JoinTable(name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "article_id"),
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"),
            inverseForeignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"),
            indexes = {
                @Index(name = "article_tag_article_id", columnList = "article_id"),
                    @Index(name = "article_tag_tag_id", columnList = "tag_id")})
    private Set<Tag> tags;


}
