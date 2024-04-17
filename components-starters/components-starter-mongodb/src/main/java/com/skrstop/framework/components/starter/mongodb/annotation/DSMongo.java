package com.skrstop.framework.components.starter.mongodb.annotation;

import com.skrstop.framework.components.starter.mongodb.constant.GlobalConfigConst;

import java.lang.annotation.*;

/**
 * 数据源指定
 *
 * @author 蒋时华
 * @date 2023-12-19 09:58:20
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DSMongo {
    String value() default GlobalConfigConst.DS_MASTER;

}
