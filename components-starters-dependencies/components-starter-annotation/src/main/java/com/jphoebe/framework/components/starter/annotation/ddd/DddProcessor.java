package com.jphoebe.framework.components.starter.annotation.ddd;

import com.jphoebe.framework.components.starter.annotation.ddd.processor.ProcessorContext;
import com.jphoebe.framework.components.starter.annotation.ddd.processor.asserts.DefaultAssert;
import com.jphoebe.framework.components.starter.annotation.ddd.processor.asserts.ProcessorAssert;
import com.jphoebe.framework.components.util.constant.StringPoolConst;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2021-03-17 09:52:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Repeatable(DddProcessors.class)
public @interface DddProcessor {

    @AliasFor(annotation = Component.class)
    String value() default StringPoolConst.EMPTY;

    /**
     * 处理器容器名字
     */
    String containerName() default ProcessorContext.DEFAULT_CONTAINERS_NAME;

    /**
     * 处理器名字, 默认：beanName
     */
    String key() default StringPoolConst.EMPTY;

    /**
     * 处理器描述
     *
     * @return
     */
    String description() default StringPoolConst.EMPTY;

    /**
     * 处理器满足条件判断，多个处理器满足条件的情况下 按照顺序默认取第一个。
     * 当为空时，默认取值assertBeanClass
     */
    String assertBeanName() default StringPoolConst.EMPTY;

    /**
     * 处理器满足条件判断，多个处理器满足条件的情况下 按照顺序默认取第一个。
     * 当为空时，默认取值assertClass
     */
    Class<?> assertBeanClass() default void.class;

    /**
     * 处理器满足条件判断
     * 当为空时，默认不满足任何条件
     */
    Class<? extends ProcessorAssert> assertClass() default DefaultAssert.class;

    /**
     * 是否是默认的处理器，当找不到对应的处理器时，默认使用该处理器，多个默认处理器, 按照顺序默认取第一个
     */
    boolean defaultProcessor() default false;

    /**
     * 顺序，值越小，优先级越高
     */
    int ordered() default Ordered.HIGHEST_PRECEDENCE;

}
