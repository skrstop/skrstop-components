package com.zoe.framework.components.starter.feign.protostuff.annotation;

import com.zoe.framework.components.core.annotation.source.Snapshot;
import com.zoe.framework.components.starter.feign.protostuff.condition.UseLocalFeignClientCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <code>FeignClient自定义条件封装 -- ProtostuffFeignClient</code>
 *
 * @author 蒋时华
 * @date 2019-03-22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@Headers({
//   "X-Ping: {token}"
//})
//@FeignClient(configuration = {FeignMessageConverterAutoConfiguration.class})
@FeignClient
@Conditional({UseLocalFeignClientCondition.class})
public @interface ProtostuffFeignClient {

    @AliasFor(annotation = FeignClient.class)
    String value() default "";

    @AliasFor(annotation = FeignClient.class)
    String contextId() default "";

    @AliasFor(annotation = FeignClient.class)
    String qualifier() default "";

    @AliasFor(annotation = FeignClient.class)
    boolean decode404() default false;

    @AliasFor(annotation = FeignClient.class)
    boolean primary() default true;

    @AliasFor(annotation = FeignClient.class)
    String name() default "";

    @AliasFor(annotation = FeignClient.class)
    String url() default "";

    @AliasFor(annotation = FeignClient.class)
    Class<?> fallback() default void.class;

    @AliasFor(annotation = FeignClient.class)
    Class<?> fallbackFactory() default void.class;

    @AliasFor(annotation = FeignClient.class)
    Class<?>[] configuration() default {};

    @AliasFor(annotation = FeignClient.class)
    String path() default "";

    /**
     * 不使用http，直接使用本地调用，在服务合并的时候使用
     * 前提：需要controller 实现 feignClient 的接口
     *
     * @return
     */
    @Snapshot(message = "新版本未测试")
    boolean local() default false;

    /**
     * 自动判断是否使用 use local, 通过扫包实现，如果确定的话尽量不要使用自动，因为会进行全量扫包，导致服务启动速度较慢
     * 优先级低于local
     *
     * @return
     */
    @Snapshot(message = "新版本未测试")
    boolean autoLocal() default false;

    /**
     * 使用autoLocal，需要指定扫包路径，否则会导致有些文件无法扫描，会报错
     *
     * @return
     */
    @Snapshot(message = "新版本未测试")
    String autoLocalBasePackage() default "";

}
