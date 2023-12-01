package com.zoe.framework.components.starter.annotation;

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
     * 锁id, 可以跨方法实现锁
     */
    String lockId() default "";

    /**
     * 是否快速失败
     *
     * @return
     */
    boolean fastFail() default false;

    /**
     * fastFail 为 true 才会生效
     * 等待超时时间，单位：毫秒
     *
     * @return
     */
    long timeoutMs() default 0L;

}
