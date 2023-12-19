package com.skrstop.framework.components.starter.redis.annotation;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2023-12-07 16:45:16
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DSRedis {

    /**
     * 数据源名称, 支持 spel
     */
    String value() default "default";

}
