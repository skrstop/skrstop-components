package com.zoe.framework.components.starter.redis.configuration.redisson.dynamic;

import com.zoe.framework.components.starter.redis.configuration.dynamic.DynamicRedisProperties;
import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:09:21
 */
@EnableConfigurationProperties(DynamicRedisProperties.class)
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnProperty(name = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".enabled", havingValue = "true", matchIfMissing = true)
public class DynamicRedissonAutoConfiguration {


}
