package com.skrstop.framework.components.starter.annotation.anno.paramAlias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: wangzh
 * @date: 2021/3/11 19:49
 * @description: desc...
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParamAlias {

    /**
     * 参数名(别名)列表
     */
    String[] value();
}
