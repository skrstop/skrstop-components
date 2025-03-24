package com.skrstop.framework.components.starter.annotation.anno.function;

import com.skrstop.framework.components.starter.annotation.handle.function.serviceLock.DefaultServiceLockRule;
import com.skrstop.framework.components.starter.annotation.handle.function.serviceLock.ServiceLockRule;
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

    /**
     * 提示信息
     *
     * @return
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * 基于beanName查找限流规则, 优先级高于 beanClass
     *
     * @see ServiceLockRule
     * bean 需要实现限流规则接口
     */
    String beanName() default "";

    /**
     * 基于beanClass查找限流规则，优先级低于 beanName
     * 使用 beanClass 时，需要保证全局只有一个实例
     *
     * @see ServiceLockRule
     * bean 需要实现限流规则接口
     */
    Class<?> beanClass() default void.class;

    /**
     * 未指定 beanName 和 beanClass 的情况下使用的默认规则
     *
     * @return
     */
    Class<? extends ServiceLockRule> defaultRule() default DefaultServiceLockRule.class;

}
