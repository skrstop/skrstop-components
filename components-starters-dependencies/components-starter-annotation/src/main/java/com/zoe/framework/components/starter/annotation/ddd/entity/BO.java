package com.zoe.framework.components.starter.annotation.ddd.entity;

import com.zoe.framework.components.util.constant.StringPoolConst;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2021-03-17 09:52:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BO {

    String value() default StringPoolConst.EMPTY;

    String remark() default StringPoolConst.EMPTY;

    /**
     * 对象作用域
     * {@link ObjectScope}
     *
     * @return
     */
    ObjectScope[] scope() default {ObjectScope.NOMAL};

}
