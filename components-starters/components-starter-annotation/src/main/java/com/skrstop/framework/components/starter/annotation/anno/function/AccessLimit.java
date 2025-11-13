package com.skrstop.framework.components.starter.annotation.anno.function;

import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule;
import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.DefaultAccessLimitRule;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;


/**
 * 限流注解
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AccessLimit {
    /**
     * 每秒限流数
     *
     * @return
     */
    double limit() default 1000D;

    /**
     * 限流提示信息
     *
     * @return
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * 基于beanName查找限流规则, 优先级高于 beanClass
     *
     * @see AccessLimitRule
     * bean 需要实现限流规则接口
     */
    String beanName() default "";

    /**
     * 基于beanClass查找限流规则，优先级低于 beanName
     * 使用 beanClass 时，需要保证全局只有一个实例
     *
     * @see AccessLimitRule
     * bean 需要实现限流规则接口
     */
    Class<?> beanClass() default void.class;

    /**
     * 未指定 beanName 和 beanClass 的情况下使用的默认规则
     *
     * @return
     */
    Class<? extends AccessLimitRule> defaultRule() default DefaultAccessLimitRule.class;

}
