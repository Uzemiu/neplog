package cn.neptu.neplog.model.params.query;

import cn.neptu.neplog.annotation.LevelRequiredParam;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.enums.PostStatus;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleQuery extends BaseQuery<Article>{

    private Integer id;

    private String title;

    private String content;

    /**
     * 0~3 Draft
     * 4 Published
     */
    @LevelRequiredParam
    private Integer status;

    /**
     * 0~3 Anybody
     * 4~7 Require review
     * 8~15 User only
     * >=16 Closed(Owner only)
     */
    @LevelRequiredParam
    private Integer commentPermission;

    /**
     * 0~3 Anybody
     * 4~7 User only
     * >=16 Private
     */
    @LevelRequiredParam
    private Integer viewPermission;

    private Integer categoryId;

    private Boolean deleted;

    @Override
    public Specification<Article> toSpecification() {
        Specification<Article> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(id != null){
                predicates.add(criteriaBuilder.equal(root.get("id"),id));
            }
            if(title != null){
                predicates.add(criteriaBuilder.like(root.get("title"),title));
            }
            if(categoryId != null){
                predicates.add(criteriaBuilder.equal(root.get("categoryId"),categoryId));
            }
            if(deleted != null){
                predicates.add(criteriaBuilder.equal(root.get("deleted"),deleted));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
