package com.skrstop.framework.components.starter.redis.configuration;

import com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicRedisProperties;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonProperties;
import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoleSetAutoConfigurationTest {

    @Test
    public void roleSet() throws Exception {
        new RoleSetAutoConfiguration();
        BeanDefinition def1 = mock(BeanDefinition.class);
        BeanDefinition def2 = mock(BeanDefinition.class);
        // 第一次：redis 命中、redisson 未命中
        BeanDefinitionRegistry registry = mock(BeanDefinitionRegistry.class);
        when(registry.containsBeanDefinition(anyString())).thenReturn(true, false);
        when(registry.getBeanDefinition(GlobalConfigConst.REDIS_DYNAMIC + "-" + DynamicRedisProperties.class.getName())).thenReturn(def1);
        RoleSetAutoConfiguration.redisStarterPropertiesRoleSet().postProcessBeanDefinitionRegistry(registry);
        Mockito.verify(def1).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        // 第二次：redis 未命中、redisson 命中
        BeanDefinitionRegistry registry2 = mock(BeanDefinitionRegistry.class);
        when(registry2.containsBeanDefinition(anyString())).thenReturn(false, true);
        when(registry2.getBeanDefinition(GlobalConfigConst.REDISSON_DYNAMIC_PREFIX + "-" + DynamicRedissonProperties.class.getName())).thenReturn(def2);
        RoleSetAutoConfiguration.redisStarterPropertiesRoleSet().postProcessBeanDefinitionRegistry(registry2);
        Mockito.verify(def2).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    }
}
