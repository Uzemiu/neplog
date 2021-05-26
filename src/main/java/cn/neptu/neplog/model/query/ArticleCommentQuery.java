package cn.neptu.neplog.model.query;

import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.ArticleComment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleCommentQuery extends BaseQuery<ArticleComment>{

    private Long id;

    private Long articleId;

    private Long parentId;

    private Integer status;

    @Override
    public Specification<ArticleComment> toSpecification() {
        Specification<ArticleComment> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(id != null){
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }
            if(articleId != null){
                predicates.add(criteriaBuilder.equal(root.get("articleId"), articleId));
            }
            if(parentId != null){
                if(0 == parentId){
                    predicates.add(criteriaBuilder.isNull(root.get("parentId")));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("parentId"), parentId));
                }
            }
            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
