package com.zoe.framework.components.starter.redis.configuration;

import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import com.zoe.framework.components.starter.redis.constant.ValueProcessorConst;
import com.zoe.framework.components.starter.redis.filter.BinaryValueFilter;
import com.zoe.framework.components.starter.redis.filter.FastjsonValueFilter;
import com.zoe.framework.components.starter.redis.filter.StringValueFilter;
import com.zoe.framework.components.starter.redis.filter.ValueFilter;
import com.zoe.framework.components.starter.redis.service.DynamicRedisService;
import com.zoe.framework.components.starter.redis.service.RedisService;
import com.zoe.framework.components.starter.redis.service.impl.DynamicRedisServiceImpl;
import com.zoe.framework.components.starter.redis.service.impl.RedisServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by 蒋时华 on 2017/9/21.
 */
@SuppressWarnings({"all"})
@Configuration
@EnableCaching
@ConditionalOnClass({RedisConnectionFactory.class, RedisTemplate.class})
@ConditionalOnBean(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({GlobalRedisProperties.class})
@ConditionalOnProperty(name = GlobalConfigConst.REDIS_PREFIX + ".enabled", havingValue = "true", matchIfMissing = true)
public class CustomRedisAutoConfiguration {

    @Bean("fastJsonRedisService")
    @ConditionalOnBean(RedisTemplate.class)
    public RedisService redisService(RedisConnectionFactory connectionFactory
            , GlobalRedisProperties globalRedisProperties) {
        RedisTemplate redisServiceTemplate;
        ValueFilter valueFilter;
        switch (globalRedisProperties.getValueProcessor()) {
            case ValueProcessorConst.BINARY:
                redisServiceTemplate = new RedisTemplate<String, Object>();
                redisServiceTemplate.setConnectionFactory(connectionFactory);
                redisServiceTemplate.afterPropertiesSet();
                valueFilter = new BinaryValueFilter();
                break;
            case ValueProcessorConst.STRING:
                redisServiceTemplate = new StringRedisTemplate(connectionFactory);
                valueFilter = new StringValueFilter();
                break;
            case ValueProcessorConst.FAST_JSON:
            default:
                redisServiceTemplate = new FastJsonRedisTemplate(connectionFactory
                        , globalRedisProperties.isFastjsonPrettyFormatJson()
                        , globalRedisProperties.isFastjsonSafeMode()
                        , globalRedisProperties.isFastjsonAutoType());
                // autoType： !globalRedisConfig.getFastjsonSafeMode() && globalRedisConfig.getFastjsonAutoType()
                valueFilter = new FastjsonValueFilter(
                        globalRedisProperties.isFastjsonFilterEach()
                        , !globalRedisProperties.isFastjsonSafeMode() && globalRedisProperties.isFastjsonAutoType()
                );
                break;
        }
        return new RedisServiceImpl(redisServiceTemplate, valueFilter);
    }

    @Bean("fastJsonDynamicRedisService")
    @ConditionalOnBean(RedisTemplate.class)
    public DynamicRedisService dynamicRedisService(RedisService redisService) {
        return new DynamicRedisServiceImpl(redisService);
    }
}
