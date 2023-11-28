package com.jphoebe.framework.components.core.annotation.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可选
 *
 * @author 蒋时华
 * @date 2021-10-19 22:27:35
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE
        , ElementType.METHOD
        , ElementType.PARAMETER
        , ElementType.PACKAGE
        , ElementType.CONSTRUCTOR
        , ElementType.LOCAL_VARIABLE
        , ElementType.ANNOTATION_TYPE
        , ElementType.TYPE_PARAMETER
        , ElementType.TYPE_USE
        , ElementType.FIELD})
public @interface Optional {

    String message() default "";

}
