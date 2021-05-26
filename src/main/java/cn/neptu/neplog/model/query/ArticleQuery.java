package cn.neptu.neplog.model.query;

import cn.hutool.core.collection.CollectionUtil;
import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import cn.neptu.neplog.model.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.criterion.Subqueries;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private Integer status;

    /**
     * 0 Anybody
     * // 4 Require review
     * // 8 User only
     * 16 Closed(Owner only)
     */
    private Integer commentPermission;

    /**
     * 0 Anybody
     * 16 Private
     */
    private Integer viewPermission;

    private Boolean deleted;

    private List<Long> categoryId;

    private List<Long> tagId;

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
            if(!CollectionUtil.isEmpty(categoryId)){
                if(categoryId.get(0).equals(0L)){
                    // 查询category为null的未分类文章
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
            if(!CollectionUtil.isEmpty(tagId)){
                Join<Article, Tag> join = root.join("tags", JoinType.INNER);
                CriteriaBuilder.In<Long> tagsIn = criteriaBuilder.in(join.get("id"));
                tagId.forEach(tagsIn::value);
                predicates.add(criteriaBuilder.and(tagsIn));
            }
            if(deleted != null){
                predicates.add(criteriaBuilder.equal(root.get("deleted"),deleted));
            }
            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            }
            if(viewPermission != null){
                predicates.add(criteriaBuilder.equal(root.get("viewPermission"), viewPermission));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
