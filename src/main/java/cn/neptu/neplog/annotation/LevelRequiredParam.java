package cn.neptu.neplog.annotation;

import cn.neptu.neplog.validator.LevelValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LevelValidator.class)
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface LevelRequiredParam {

    int level() default 6;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
