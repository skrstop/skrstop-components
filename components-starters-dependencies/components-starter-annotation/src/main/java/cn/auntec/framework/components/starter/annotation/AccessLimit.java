package cn.auntec.framework.components.starter.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AccessLimit {
    double limit() default 1000D;
}
