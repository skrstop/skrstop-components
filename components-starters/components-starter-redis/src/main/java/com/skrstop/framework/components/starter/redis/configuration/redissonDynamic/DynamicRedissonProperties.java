package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic;

import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import lombok.*;
import lombok.experimental.Accessors;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2021-05-25 19:50:45
 */
@Getter
@Setter
@Configuration
@ConditionalOnClass({Config.class, RedissonClient.class})
@ConfigurationProperties(GlobalConfigConst.REDISSON_DYNAMIC_PREFIX)
public class DynamicRedissonProperties {

    /**
     * 是否开启redisson dynamic.
     */
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
    private Client client = new Client();

    /*** 数据源 */
    @NestedConfigurationProperty
    private Map<String, Config> dataSources = new LinkedHashMap<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Config {
        /**
         * 指定redisson配置文件.
         */
        private String configFile;
        /**
         * yaml配置文本.
         */
        private String configYamlStr;
        /**
         * json配置文本
         */
        @Deprecated
        private String configJsonStr;
    }

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
         * order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE + 1;
        /**
         * 只允许public方法
         */
        private boolean allowedPublicOnly = true;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Client {
        /*** 开启 DynamicRedissonClient */
        private boolean enabled = true;
        /**
         * service order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE;
    }

}
