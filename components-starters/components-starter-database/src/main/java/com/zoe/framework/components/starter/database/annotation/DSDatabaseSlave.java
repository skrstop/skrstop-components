package com.zoe.framework.components.starter.database.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;

import java.lang.annotation.*;

/**
 * 从库
 *
 * @author 蒋时华
 * @date 2023-12-19 10:10:13
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS(GlobalConfigConst.DS_SLAVE)
public @interface DSDatabaseSlave {
}
