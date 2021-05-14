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
public class Article extends BaseArticle{

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT,name = "none"))
    private Category category;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "article_id"),
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"),
            inverseForeignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private Set<Tag> tags;


}
