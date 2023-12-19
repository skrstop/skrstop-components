package com.skrstop.framework.components.starter.annotation.anno.function;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * controller接口失效
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AccessControl {

    String message() default "服务器忙，请稍后重试";

    String alias() default "";

}
