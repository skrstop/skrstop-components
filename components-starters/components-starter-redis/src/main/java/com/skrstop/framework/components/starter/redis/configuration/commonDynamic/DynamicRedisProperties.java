package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2023-12-07 15:52:58
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ConfigurationProperties(prefix = GlobalConfigConst.REDIS_DYNAMIC)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class DynamicRedisProperties {

    /*** 是否开启动态redis数据源 */
    private boolean enabled = false;

    /*** 设置默认数据源 */
    private String primary = "default";

    /*** 当为true时，未找到指定的redis时会抛出异常，为false时，则使用默认redis */
    private boolean exceptionWhileNotFound = false;

    /*** aop相关配置 */
    @NestedConfigurationProperty
    private Aop aop = new Aop();

    /*** service相关配置 */
    @NestedConfigurationProperty
    private Service service = new Service();

    /*** 数据源 */
    @NestedConfigurationProperty
    private Map<String, RedisProperties> dataSources = new LinkedHashMap<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Aop {
        /*** 开启aop注解 */
        private boolean enabled = true;
        /**
         * aop order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE + 1;
        /**
         * aop allowedPublicOnly
         */
        private boolean allowedPublicOnly = true;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Service {
        /*** 开启 DynamicRedisService */
        private boolean enabled = true;
        /**
         * service order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE;
    }

}
