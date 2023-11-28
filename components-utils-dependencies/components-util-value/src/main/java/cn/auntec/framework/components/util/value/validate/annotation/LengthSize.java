package cn.auntec.framework.components.util.value.validate.annotation;

import cn.auntec.framework.components.util.value.validate.validator.LengthStrConstraintValidatorForCollection;
import cn.auntec.framework.components.util.value.validate.validator.LengthStrConstraintValidatorForObj;
import cn.auntec.framework.components.util.value.validate.validator.LengthStrConstraintValidatorForString;

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
        LengthStrConstraintValidatorForString.class
        , LengthStrConstraintValidatorForCollection.class
        , LengthStrConstraintValidatorForObj.class
})
public @interface LengthSize {

    /*** 检验字符长度，否则校验字节长度 */
    boolean size() default false;

    long min() default 0;

    long max() default Long.MAX_VALUE;

    /*** 是否忽略null 值，如果不忽略，默认当做空字符串处理 */
    boolean ignoreNull() default true;

    String charset() default "UTF-8";

    String message() default "{org.hibernate.validator.constraints.Length.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
