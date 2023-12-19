package com.zoe.framework.components.starter.database.annotation;

import com.baomidou.dynamic.datasource.annotation.DsTxEventListener;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.event.TransactionPhase;

import java.lang.annotation.*;

/**
 * 事务时间监听器
 *
 * @author 蒋时华
 * @date 2023-12-19 10:08:51
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DsTxEventListener
public @interface DsDatabaseTxEventListener {

    @AliasFor(annotation = DsTxEventListener.class, attribute = "phase")
    TransactionPhase phase() default TransactionPhase.AFTER_COMMIT;

    @AliasFor(annotation = DsTxEventListener.class, attribute = "value")
    Class<?>[] value() default {};

    @AliasFor(annotation = DsTxEventListener.class, attribute = "classes")
    Class<?>[] classes() default {};

    @AliasFor(annotation = DsTxEventListener.class, attribute = "condition")
    String condition() default "";

}
