package cn.neptu.neplog.annotation;

import cn.neptu.neplog.model.entity.Article;
import cn.neptu.neplog.validator.LevelValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 根据用户等级校验参数是否被允许<br/>
 * 比如{@link Article#getViewPermission}只允许博主搜索
 * @author Uzemiu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LevelValidator.class)
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface LevelRequiredParam {

    /**
     * 对于大于等于此等级的用户开放字段搜索权限
     * 对于小于此等级的用户，被注解的参数必须为null
     */
    int level() default 6;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
