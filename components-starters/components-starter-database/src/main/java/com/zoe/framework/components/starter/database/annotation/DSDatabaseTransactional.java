package com.zoe.framework.components.starter.database.annotation;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.tx.DsPropagation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 多数据源事务
 *
 * @author 蒋时华
 * @date 2023-12-19 10:06:15
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DSTransactional
public @interface DSDatabaseTransactional {

    /**
     * 回滚异常
     *
     * @return Class[]
     */
    @AliasFor(annotation = DSTransactional.class, attribute = "rollbackFor")
    Class<? extends Throwable>[] rollbackFor() default {Exception.class};

    /**
     * 不回滚异常
     *
     * @return Class[]
     */
    @AliasFor(annotation = DSTransactional.class, attribute = "noRollbackFor")
    Class<? extends Throwable>[] noRollbackFor() default {};

    /**
     * 事务传播行为
     *
     * @return DsPropagation
     */
    @AliasFor(annotation = DSTransactional.class, attribute = "propagation")
    DsPropagation propagation() default DsPropagation.REQUIRED;

}
