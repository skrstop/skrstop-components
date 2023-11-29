package com.zoe.framework.components.starter.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 执行时间耗时打印切面
 *
 * @author 蒋时华
 * @date 2020-09-04 14:29:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface ExecuteTimeLog {

    String value() default "";

}
