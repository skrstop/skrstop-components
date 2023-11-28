package cn.auntec.framework.components.starter.web.response;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 不需要全局返回值包装处理注解
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface NoGlobalResponse {

}
