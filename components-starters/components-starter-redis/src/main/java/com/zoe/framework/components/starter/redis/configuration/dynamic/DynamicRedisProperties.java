package com.zoe.framework.components.starter.redis.configuration.dynamic;

import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
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
public class DynamicRedisProperties {

    /*** 是否开启动态redis数据源 */
    private Boolean enabled = false;

    /*** 设置默认数据源 */
    private String primary = "default";

    /*** aop相关配置 */
    @NestedConfigurationProperty
    private Aop aop = new Aop();

    /*** service相关配置 */
    @NestedConfigurationProperty
    private Service service = new Service();

    /*** 当为true时，未找到指定的redis时会抛出异常，为false时，则使用默认redis */
    private Boolean exceptionWhileNotFoundRedis = false;

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
        private Boolean aop = true;
        /**
         * aop order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE + 1;
        /**
         * aop allowedPublicOnly
         */
        private Boolean allowedPublicOnly = false;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Service {
        /**
         * service order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE;
    }

}
