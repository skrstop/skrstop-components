package com.zoe.framework.components.starter.redis.configuration.dynamic;

import com.zoe.framework.components.starter.redis.configuration.dynamic.annotation.DSRedis;
import com.zoe.framework.components.starter.redis.configuration.dynamic.aop.DynamicAopAnnotationAdvisor;
import com.zoe.framework.components.starter.redis.configuration.dynamic.aop.DynamicAopSourceAnnotationInterceptor;
import com.zoe.framework.components.starter.redis.configuration.dynamic.selector.DsSelector;
import com.zoe.framework.components.starter.redis.configuration.dynamic.selector.DsSpelExpressionProcessor;
import com.zoe.framework.components.starter.redis.configuration.dynamic.service.DynamicServiceAnnotationAdvisor;
import com.zoe.framework.components.starter.redis.configuration.dynamic.service.DynamicServiceAnnotationInterceptor;
import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import com.zoe.framework.components.starter.redis.service.DynamicRedisService;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.ClientResourcesBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;
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
@ConditionalOnProperty(name = GlobalConfigConst.REDIS_DYNAMIC + ".enabled", havingValue = "true", matchIfMissing = true)
public class DynamicRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DynamicRedisConnectionFactory.class)
    public DynamicRedisConnectionFactory dynamicRedisConnectionFactory(
            DynamicRedisProperties dynamicRedisProperties
            , ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider
            , ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration
            , ObjectProvider<RedisClusterConfiguration> clusterConfiguration
            , ObjectProvider<JedisClientConfigurationBuilderCustomizer> jedisBuilderCustomizers
            , ObjectProvider<LettuceClientConfigurationBuilderCustomizer> lettuceBuilderCustomizers
            , ObjectProvider<ClientResourcesBuilderCustomizer> lettuceClientResourcesCustomizers) {
        return new DynamicRedisConnectionFactory(dynamicRedisProperties
                , standaloneConfigurationProvider
                , sentinelConfiguration
                , clusterConfiguration
                , jedisBuilderCustomizers
                , lettuceBuilderCustomizers
                , lettuceClientResourcesCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DsSelector dsProcessor(BeanFactory beanFactory) {
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return spelExpressionProcessor;
    }

    /**
     * aop
     *
     * @param dsSelector
     * @param dynamicRedisProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDIS_DYNAMIC + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicConnectionFactoryAnnotationAdvisor(DsSelector dsSelector, DynamicRedisProperties dynamicRedisProperties) {
        DynamicRedisProperties.Aop aop = dynamicRedisProperties.getAop();
        DynamicAopSourceAnnotationInterceptor interceptor = new DynamicAopSourceAnnotationInterceptor(aop.isAllowedPublicOnly(), dsSelector);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, DSRedis.class);
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
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.REDIS_DYNAMIC + ".service", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicConnectionFactoryServiceAdvisor(DsSelector dsSelector, DynamicRedisProperties dynamicRedisProperties) {
        DynamicServiceAnnotationInterceptor interceptor = new DynamicServiceAnnotationInterceptor(dsSelector);
        DynamicServiceAnnotationAdvisor advisor = new DynamicServiceAnnotationAdvisor(interceptor, DynamicRedisService.class);
        advisor.setOrder(dynamicRedisProperties.getService().getOrder());
        return advisor;
    }

}
