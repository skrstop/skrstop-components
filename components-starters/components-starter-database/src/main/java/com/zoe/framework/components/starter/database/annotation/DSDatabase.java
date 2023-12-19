package com.zoe.framework.components.starter.database.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;
import org.springframework.core.annotation.AliasFor;

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
@DS(GlobalConfigConst.DS_DEFAULT)
public @interface DSDatabase {
    @AliasFor(annotation = DS.class, attribute = "value")
    String value();

}
