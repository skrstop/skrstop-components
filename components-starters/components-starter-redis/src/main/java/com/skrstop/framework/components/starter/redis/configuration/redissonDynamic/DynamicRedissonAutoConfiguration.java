package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.proxy.DynamicAnnotationAdvisor;
import com.skrstop.framework.components.starter.common.proxy.DynamicServiceAdvisor;
import com.skrstop.framework.components.starter.redis.annotation.DSRedisson;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.aop.DynamicAnnotationInterceptor;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.client.DynamicClientInterceptor;
import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.redis.service.impl.DynamicAopRedissonClient;
import com.skrstop.framework.components.starter.redis.service.impl.DynamicRedissonClient;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:09:21
 */
@ConditionalOnClass({RedissonClient.class, Config.class})
@EnableConfigurationProperties(DynamicRedissonProperties.class)
@Configuration
@Order
@ConditionalOnProperty(name = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
public class DynamicRedissonAutoConfiguration {

    @Bean
    public MultiRedisson multiRedisson(DynamicRedissonProperties dynamicRedissonProperties, ApplicationContext ctx) {
        return new MultiRedisson(dynamicRedissonProperties, ctx);
    }

    /**
     * client aop Advisor
     *
     * @param dsSelector
     * @param dynamicRedissonProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicRedissonDsRedissonAnnotationAdvisor(DsSelector dsSelector, DynamicRedissonProperties dynamicRedissonProperties) {
        DynamicRedissonProperties.Aop aop = dynamicRedissonProperties.getAop();
        DynamicAnnotationInterceptor interceptor = new DynamicAnnotationInterceptor(dynamicRedissonProperties, dsSelector);
        DynamicAnnotationAdvisor advisor = new DynamicAnnotationAdvisor(interceptor, DSRedisson.class);
        advisor.setOrder(aop.getOrder());
        return advisor;
    }

    /**
     * client aop
     *
     * @param multiRedisson
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DynamicAopRedissonClient dynamicAopRedissonClient(MultiRedisson multiRedisson) {
        return new DynamicAopRedissonClient(multiRedisson);
    }

    /**
     * client Advisor
     *
     * @param dsSelector
     * @param dynamicRedissonProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".client", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicRedissonClientAdvisor(DsSelector dsSelector, DynamicRedissonProperties dynamicRedissonProperties) {
        DynamicClientInterceptor interceptor = new DynamicClientInterceptor(dsSelector);
        DynamicServiceAdvisor advisor = new DynamicServiceAdvisor(interceptor, DynamicRedissonClient.class);
        advisor.setOrder(dynamicRedissonProperties.getClient().getOrder());
        return advisor;
    }

    /**
     * client
     *
     * @param multiRedisson
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + ".client", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DynamicRedissonClient dynamicRedissonClient(MultiRedisson multiRedisson) {
        return new DynamicRedissonClient(multiRedisson);
    }

}
