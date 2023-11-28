package com.jphoebe.framework.components.starter.annotation.ddd;

import com.jphoebe.framework.components.util.constant.StringPoolConst;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2021-03-17 09:52:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface DddCtlEndpoint {

    @AliasFor(annotation = Controller.class)
    String value() default StringPoolConst.EMPTY;

}
