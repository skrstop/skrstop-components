package cn.auntec.framework.components.starter.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 自定义注解 同步锁
 *
 * @author 蒋时华
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface ServiceLock {

    /**
     * 锁id, 可以跨service实现锁
     */
    String lockId() default "";

}
