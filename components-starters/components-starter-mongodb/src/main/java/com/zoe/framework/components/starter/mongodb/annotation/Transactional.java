package com.zoe.framework.components.starter.mongodb.annotation;

import com.zoe.framework.components.starter.mongodb.constant.MongodbConst;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2023-12-19 14:22:54
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@org.springframework.transaction.annotation.Transactional
public @interface Transactional {

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "value")
    String value() default MongodbConst.TRANSACTION_NAME_MONGO;

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "transactionManager")
    String transactionManager() default MongodbConst.TRANSACTION_NAME_MONGO;

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "label")
    String[] label() default {};

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "propagation")
    Propagation propagation() default Propagation.REQUIRED;

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "isolation")
    Isolation isolation() default Isolation.DEFAULT;

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "timeout")
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "timeoutString")
    String timeoutString() default "";

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "readOnly")
    boolean readOnly() default false;

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "rollbackFor")
    Class<? extends Throwable>[] rollbackFor() default {};

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "rollbackForClassName")
    String[] rollbackForClassName() default {};

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "noRollbackFor")
    Class<? extends Throwable>[] noRollbackFor() default {};

    @AliasFor(annotation = org.springframework.transaction.annotation.Transactional.class, attribute = "noRollbackForClassName")
    String[] noRollbackForClassName() default {};

}
