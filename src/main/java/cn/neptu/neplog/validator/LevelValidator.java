package cn.neptu.neplog.validator;

import cn.neptu.neplog.annotation.LevelRequiredParam;
import cn.neptu.neplog.model.entity.User;
import cn.neptu.neplog.utils.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LevelValidator implements ConstraintValidator<LevelRequiredParam,Object> {

    private int level;

    @Override
    public void initialize(LevelRequiredParam constraintAnnotation) {
        level = constraintAnnotation.level();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        User user = SecurityUtil.getCurrentUser();
        return value == null || user != null && user.getLevel() >= level;
    }
}
