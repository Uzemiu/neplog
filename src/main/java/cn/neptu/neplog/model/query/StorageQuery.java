package cn.neptu.neplog.model.query;

import cn.neptu.neplog.model.entity.Storage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class StorageQuery extends BaseQuery<Storage>{

    private String filename;

    private String type;

    private String location;

    @Override
    public Specification<Storage> toSpecification() {
        Specification<Storage> specification = super.toSpecification();
        return specification.and((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(filename)){
                predicates.add(criteriaBuilder.like(root.get("filename"),"%" + filename + "%"));
            }
            if(StringUtils.hasText(type)){
                predicates.add(criteriaBuilder.equal(root.get("type"),type));
            }
            if(StringUtils.hasText(location)){
                predicates.add(criteriaBuilder.equal(root.get("location"),location));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
