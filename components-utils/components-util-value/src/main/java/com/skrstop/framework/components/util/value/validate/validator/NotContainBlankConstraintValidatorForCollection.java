package com.skrstop.framework.components.util.value.validate.validator;

import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.validate.annotation.NotContainBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-09-27 19:36:34
 */
public class NotContainBlankConstraintValidatorForCollection implements ConstraintValidator<NotContainBlank, Collection> {

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (Object obj : value) {
            if (obj == null) {
                return false;
            } else if (StrUtil.isBlank(obj.toString())) {
                return false;
            }
        }
        return true;
    }
}
