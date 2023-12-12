package com.zoe.framework.components.starter.web.response;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 禁用IResult类型自动转换
 * 默认情况下，当方法返回IResult 时，默认返回数据会去掉全局包装，使用方法返回的IResult
 * 当使用此注解时，将不会对返回值进行处理，直接将IResult当做data返回值处理
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
