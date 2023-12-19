package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.ClientResourcesBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:09:21
 */
@EnableConfigurationProperties(DynamicRedisProperties.class)
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnProperty(name = GlobalConfigConst.REDIS_DYNAMIC + ".enabled", havingValue = "true", matchIfMissing = false)
public class DynamicRedisConnectFactoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DynamicRedisConnectionFactory.class)
    public DynamicRedisConnectionFactory dynamicRedisConnectionFactory(
            DynamicRedisProperties dynamicRedisProperties
            , ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider
            , ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration
            , ObjectProvider<RedisClusterConfiguration> clusterConfiguration
            , ObjectProvider<JedisClientConfigurationBuilderCustomizer> jedisBuilderCustomizers
            , ObjectProvider<LettuceClientConfigurationBuilderCustomizer> lettuceBuilderCustomizers
            , ObjectProvider<ClientResourcesBuilderCustomizer> lettuceClientResourcesCustomizers
            , ObjectProvider<SslBundles> sslBundles) {
        return new DynamicRedisConnectionFactory(dynamicRedisProperties
                , standaloneConfigurationProvider
                , sentinelConfiguration
                , clusterConfiguration
                , jedisBuilderCustomizers
                , lettuceBuilderCustomizers
                , lettuceClientResourcesCustomizers
                , sslBundles);
    }

}
