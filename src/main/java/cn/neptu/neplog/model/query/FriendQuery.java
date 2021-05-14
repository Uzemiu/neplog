package cn.neptu.neplog.model.query;

import cn.neptu.neplog.model.entity.Friend;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendQuery extends BaseQuery<Friend>{

    private Integer id;

    private String name;

    private String link;

    /**
     * 0 待审核
     * 1 公开
     */
    private Integer status;

    @Override
    public Specification<Friend> toSpecification() {
        Specification<Friend> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(id != null){
                predicates.add(criteriaBuilder.equal(root.get("id"),id));
            }
            if(StringUtils.hasText(name)){
                Predicate nLike = criteriaBuilder.like(root.get("name"),"%" + name + "%");
                Predicate iLike = criteriaBuilder.like(root.get("introduction"),"%" + name + "%");
                predicates.add(criteriaBuilder.or(nLike,iLike));
            }
            if(StringUtils.hasText(link)){
                predicates.add(criteriaBuilder.like(root.get("link"),"%" + link + "%"));
            }
            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
