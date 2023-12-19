package com.skrstop.framework.components.util.value.validate.validator;

import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StringLengthUtil;
import com.skrstop.framework.components.util.value.validate.annotation.LengthSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-09-27 19:36:34
 */
public class LengthStrConstraintValidatorForCollection implements ConstraintValidator<LengthSize, Collection> {

    private boolean size;
    private String charset;
    private long min;
    private long max;
    private boolean ignoreNull;

    @Override
    public void initialize(LengthSize constraintAnnotation) {
        charset = constraintAnnotation.charset();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        size = constraintAnnotation.size();
        ignoreNull = constraintAnnotation.ignoreNull();
    }

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        if (ignoreNull && ObjectUtil.isNull(value)) {
            return true;
        }
        StringBuilder sb = new StringBuilder(StringPoolConst.EMPTY);
        if (ObjectUtil.isNotNull(value) && CollectionUtil.isNotEmpty(value)) {
            for (Object o : value) {
                sb.append(o.toString());
            }
        }
        String test = sb.toString();
        if (size) {
            long size = test.length();
            return (size >= min && size <= max);
        }
        return StringLengthUtil.gtAndlt(test, min, max, charset);
    }
}
