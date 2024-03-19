package com.skrstop.framework.components.starter.redis.configuration.common;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.dsSelector.DsSpelExpressionSelector;
import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.redis.constant.ValueProcessorConst;
import com.skrstop.framework.components.starter.redis.filter.BinaryValueFilter;
import com.skrstop.framework.components.starter.redis.filter.FastjsonValueFilter;
import com.skrstop.framework.components.starter.redis.filter.StringValueFilter;
import com.skrstop.framework.components.starter.redis.filter.ValueFilter;
import com.skrstop.framework.components.starter.redis.service.RedisService;
import com.skrstop.framework.components.starter.redis.service.impl.RedisServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
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

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {GlobalConfigConst.REDIS_DYNAMIC + ".enabled", GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".enabled"}, havingValue = "true")
    public DsSelector dsSelector(BeanFactory beanFactory) {
        DsSpelExpressionSelector spelExpressionProcessor = new DsSpelExpressionSelector();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return spelExpressionProcessor;
    }

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

}
