package cn.neptu.neplog.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LevelRequiredAccess {

    int value() default 6;
}
