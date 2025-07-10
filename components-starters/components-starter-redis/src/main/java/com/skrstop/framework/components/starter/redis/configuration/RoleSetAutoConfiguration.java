package com.skrstop.framework.components.starter.redis.configuration;

import com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicRedisProperties;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonProperties;
import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author 蒋时华
 * @date 2025-03-28 00:07:46
 * @since 1.0.0
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RoleSetAutoConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public static BeanDefinitionRegistryPostProcessor redisStarterPropertiesRoleSet() {
        return registry -> {
            String dynamicRedisPropertiesBeanName = GlobalConfigConst.REDIS_DYNAMIC + "-" + DynamicRedisProperties.class.getName();
            if (registry.containsBeanDefinition(dynamicRedisPropertiesBeanName)) {
                BeanDefinition beanDefinition = registry.getBeanDefinition(dynamicRedisPropertiesBeanName);
                beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            }
            String dynamicRedissonPropertiesBeanName = GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + "-" + DynamicRedissonProperties.class.getName();
            if (registry.containsBeanDefinition(dynamicRedissonPropertiesBeanName)) {
                BeanDefinition beanDefinition = registry.getBeanDefinition(dynamicRedissonPropertiesBeanName);
                beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            }
        };
    }

}
