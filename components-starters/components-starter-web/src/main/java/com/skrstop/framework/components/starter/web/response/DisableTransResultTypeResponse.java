package com.skrstop.framework.components.starter.web.response;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 禁用Result的类型自动转换
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface DisableTransResultTypeResponse {

}
