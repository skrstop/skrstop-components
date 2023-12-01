package com.zoe.framework.components.util.value.validate.annotation;

import com.zoe.framework.components.util.value.validate.validator.NotContainBlankConstraintValidatorForCollection;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证字符长度
 *
 * @author 蒋时华
 * @date 2020-09-27 19:35:49
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        NotContainBlankConstraintValidatorForCollection.class
})
public @interface NotContainBlank {

    String message() default "{javax.validation.constraints.NotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
