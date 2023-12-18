package com.zoe.framework.components.starter.redis.configuration.commonDynamic;

import com.zoe.framework.components.starter.common.proxy.DynamicAnnotationAdvisor;
import com.zoe.framework.components.starter.common.proxy.DynamicServiceAdvisor;
import com.zoe.framework.components.starter.redis.annotation.DSRedis;
import com.zoe.framework.components.starter.redis.configuration.common.CustomRedisAutoConfiguration;
import com.zoe.framework.components.starter.redis.configuration.commonDynamic.aop.DynamicAnnotationInterceptor;
import com.zoe.framework.components.starter.redis.configuration.commonDynamic.service.DynamicServiceInterceptor;
import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import com.zoe.framework.components.starter.redis.selector.DsSelector;
import com.zoe.framework.components.starter.redis.service.DynamicRedisService;
import com.zoe.framework.components.starter.redis.service.RedisService;
import com.zoe.framework.components.starter.redis.service.impl.DynamicRedisServiceImpl;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:09:21
 */
@EnableConfigurationProperties(DynamicRedisProperties.class)
@Configuration
@AutoConfigureAfter(CustomRedisAutoConfiguration.class)
@ConditionalOnProperty(name = GlobalConfigConst.REDIS_DYNAMIC + ".enabled", havingValue = "true", matchIfMissing = false)
public class DynamicRedisAutoConfiguration {
    /**
     * aop
     *
     * @param dsSelector
     * @param dynamicRedisProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDIS_DYNAMIC + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicRedisDsRedisAnnotationAdvisor(DsSelector dsSelector, DynamicRedisProperties dynamicRedisProperties) {
        DynamicRedisProperties.Aop aop = dynamicRedisProperties.getAop();
        DynamicAnnotationInterceptor interceptor = new DynamicAnnotationInterceptor(dynamicRedisProperties, dsSelector);
        DynamicAnnotationAdvisor advisor = new DynamicAnnotationAdvisor(interceptor, DSRedis.class);
        advisor.setOrder(aop.getOrder());
        return advisor;
    }

    /**
     * service
     *
     * @param dsSelector
     * @param dynamicRedisProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDIS_DYNAMIC + ".service", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicRedisServiceAdvisor(DsSelector dsSelector, DynamicRedisProperties dynamicRedisProperties) {
        DynamicServiceInterceptor interceptor = new DynamicServiceInterceptor(dsSelector);
        DynamicServiceAdvisor advisor = new DynamicServiceAdvisor(interceptor, DynamicRedisService.class);
        advisor.setOrder(dynamicRedisProperties.getService().getOrder());
        return advisor;
    }

    @Bean("fastJsonDynamicRedisService")
    @ConditionalOnBean(RedisService.class)
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDIS_DYNAMIC + ".service", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DynamicRedisService dynamicRedisService(RedisService redisService) {
        return new DynamicRedisServiceImpl(redisService);
    }

}
