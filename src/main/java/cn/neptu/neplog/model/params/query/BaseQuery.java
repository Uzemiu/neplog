package cn.neptu.neplog.model.params.query;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseQuery<T> {

    public abstract Specification<T> toSpecification();
}
