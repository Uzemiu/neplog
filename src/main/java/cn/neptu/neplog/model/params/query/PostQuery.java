package cn.neptu.neplog.model.params.query;

import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.model.enums.PostStatus;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostQuery extends BaseQuery<Article>{

    @NotNull(message = "博客ID不能为空")
    @ApiParam(value = "博客ID",required = true)
    private Integer blogId;

    @ApiParam(value = "是否显示私密博客")
    private Boolean showPrivate;

    @ApiParam(value = "是否显示草稿")
    private Boolean showDraft;

    @ApiParam(value = "是否显示转载博客")
    private Boolean showShared;

    @ApiParam(value = "是否显示已删除博客")
    private Boolean deleted;

    @Override
    public Specification<Article> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(blogId != null){
                predicates.add(criteriaBuilder.equal(root.get("blogId"), blogId));
            }
            if(showPrivate != null){
                predicates.add(criteriaBuilder.equal(root.get("privacy"),showPrivate == null ? false : showPrivate));
            }
            if(showDraft != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), PostStatus.values()[showDraft ? 1 : 0]));
            }
            if(showShared != null){
                predicates.add(criteriaBuilder.equal(root.get("shared"),showShared));
            }
            if(deleted != null){
                predicates.add(criteriaBuilder.equal(root.get("deleted"),deleted));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
