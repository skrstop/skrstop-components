package cn.auntec.framework.components.util.value.validate.annotation;

import cn.auntec.framework.components.util.value.validate.validator.NotContainNullConstraintValidatorForCollection;

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
        NotContainNullConstraintValidatorForCollection.class
})
public @interface NotContainNull {

    String charset() default "UTF-8";

    String message() default "{javax.validation.constraints.NotNull.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
