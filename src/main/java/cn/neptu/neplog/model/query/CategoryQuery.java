package cn.neptu.neplog.model.query;

import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.entity.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryQuery extends BaseQuery<Category>{

    private String name;

    private Integer parentId;

    /**
     * 是否显示分类下文章数量
     */
    private boolean showCount;

    /**
     * 是否显示'未分类文章'分类
     */
    private boolean showUncategorized;

    @Override
    public Specification<Category> toSpecification() {
        Specification<Category> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(parentId != null){
                if(parentId == 0){
                    predicates.add(criteriaBuilder.isNull(root.get("parentId")));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("parentId"),parentId));
                }
            }
            if(StringUtils.hasText(name)){
                predicates.add(criteriaBuilder.like(root.get("name"),"%" + name + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
