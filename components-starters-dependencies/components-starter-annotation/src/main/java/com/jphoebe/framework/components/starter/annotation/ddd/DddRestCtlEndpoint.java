package com.jphoebe.framework.components.starter.annotation.ddd;

import com.jphoebe.framework.components.util.constant.StringPoolConst;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2021-03-17 09:52:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface DddRestCtlEndpoint {

    @AliasFor(annotation = RestController.class)
    String value() default StringPoolConst.EMPTY;

}
