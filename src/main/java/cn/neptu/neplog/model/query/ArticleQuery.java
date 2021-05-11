package cn.neptu.neplog.model.query;

import cn.neptu.neplog.annotation.LevelRequiredParam;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleQuery extends BaseQuery<Article>{

    private String content;

    /**
     * 0 Draft
     * 4 Published
     */
    @LevelRequiredParam
    private Integer status;

    /**
     * 0 Anybody
     * 4 Require review
     * 8 User only
     * > Closed(Owner only)
     */
    @LevelRequiredParam
    private Integer commentPermission;

    /**
     * 0 Anybody
     * 8 User only
     * 16 Private
     */
    @LevelRequiredParam
    private List<Integer> viewPermission;

    @LevelRequiredParam
    private Boolean deleted;

    private List<Long> categoryId;

    @Override
    public Specification<Article> toSpecification() {
        Specification<Article> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(content)){
                Predicate cLike = criteriaBuilder.like(root.get("content"),"%" + content + "%");
                Predicate tLike = criteriaBuilder.like(root.get("title"),"%" + content + "%");
                predicates.add(criteriaBuilder.or(cLike,tLike));
            }
            if(categoryId != null && !categoryId.isEmpty()){
                if(categoryId.get(0).equals(0L)){
                    // 查询category为null的文章
                    predicates.add(criteriaBuilder.isNull(root.get("category")));
                } else {
                    CriteriaBuilder.In<Category> in = criteriaBuilder.in(root.get("category"));
                    categoryId.forEach(id -> {
                        Category category = new Category();
                        category.setId(id);
                        in.value(category);
                    });
                    predicates.add(criteriaBuilder.and(in));
                }
            }
            if(deleted != null){
                predicates.add(criteriaBuilder.equal(root.get("deleted"),deleted));
            }
            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            }
            if(viewPermission != null && !viewPermission.isEmpty()){
                CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("viewPermission"));
                viewPermission.forEach(in::value);
                predicates.add(criteriaBuilder.and(in));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
