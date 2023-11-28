package cn.auntec.framework.components.starter.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 内网访问请求限制
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface IntranetLimit {


}
