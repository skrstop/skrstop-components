package com.skrstop.framework.components.util.value.validate.validator;

import com.skrstop.framework.components.util.value.validate.annotation.NotContainNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-09-27 19:36:34
 */
public class NotContainNullConstraintValidatorForCollection implements ConstraintValidator<NotContainNull, Collection> {

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (Object obj : value) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }
}
