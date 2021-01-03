package cn.neptu.neplog.model.query;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BaseQuery<T> {


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> updateTime;

    public Specification<T> toSpecification(){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(createTime != null && createTime.size() > 0){
                predicates.add(createTime.size() > 1
                        ? criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"),createTime.get(0))
                        : criteriaBuilder.between(root.get("createTime"), createTime.get(0), createTime.get(1)));
            }
            if(updateTime != null && updateTime.size() > 0){
                predicates.add(updateTime.size() > 1
                        ? criteriaBuilder.greaterThanOrEqualTo(root.get("updateTime"),updateTime.get(0))
                        : criteriaBuilder.between(root.get("updateTime"), updateTime.get(0), updateTime.get(1)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
