package cn.neptu.neplog.model.query;

import cn.neptu.neplog.annotation.LevelRequiredParam;
import cn.neptu.neplog.model.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleQuery extends BaseQuery<Article>{

    private Integer id;

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

    @LevelRequiredParam
    private Boolean deleted;

    private Integer categoryId;

    @Override
    public Specification<Article> toSpecification() {
        Specification<Article> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(id != null){
                predicates.add(criteriaBuilder.equal(root.get("id"),id));
            }
            if(StringUtils.hasText(content)){
                Predicate cLike = criteriaBuilder.like(root.get("content"),"%" + content + "%");
                Predicate tLike = criteriaBuilder.like(root.get("title"),"%" + content + "%");
                predicates.add(criteriaBuilder.or(cLike,tLike));
            }
            if(categoryId != null){
                predicates.add(criteriaBuilder.equal(root.get("categoryId"),categoryId));
            }
            if(deleted != null){
                predicates.add(criteriaBuilder.equal(root.get("deleted"),deleted));
            }
            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
