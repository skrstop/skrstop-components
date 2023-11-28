package com.jphoebe.framework.components.starter.annotation.ddd;

import com.jphoebe.framework.components.util.constant.StringPoolConst;
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
public @interface DddEvent {

    @AliasFor(annotation = Component.class)
    String value() default StringPoolConst.EMPTY;

}
