package com.skrstop.framework.components.starter.annotation.configuration;

import com.skrstop.framework.components.starter.annotation.constant.GlobalConfigConst;
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
    public static BeanDefinitionRegistryPostProcessor annotationStarterPropertiesRoleSet() {
        return registry -> {
            String dynamicRedisPropertiesBeanName = GlobalConfigConst.ANNOTATION_PREFIX + "-" + AnnotationProperties.class.getName();
            if (registry.containsBeanDefinition(dynamicRedisPropertiesBeanName)) {
                BeanDefinition beanDefinition = registry.getBeanDefinition(dynamicRedisPropertiesBeanName);
                beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            }
        };
    }

}
