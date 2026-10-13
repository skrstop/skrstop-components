package com.skrstop.framework.components.starter.database.configuration;

import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceProperties;
import com.skrstop.framework.components.starter.database.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class RoleSetAutoConfigurationTest {

    @Test
    public void roleSetWhenPresent() {
        BeanDefinitionRegistry registry = Mockito.mock(BeanDefinitionRegistry.class);
        String beanName = GlobalConfigConst.DATABASE_DYNAMIC + "-" + DynamicDataSourceProperties.class.getName();
        Mockito.when(registry.containsBeanDefinition(beanName)).thenReturn(true);
        BeanDefinition definition = Mockito.mock(BeanDefinition.class);
        Mockito.when(registry.getBeanDefinition(beanName)).thenReturn(definition);

        BeanDefinitionRegistryPostProcessorAssert.run(RoleSetAutoConfiguration.class);
        RoleSetAutoConfiguration configuration = new RoleSetAutoConfiguration();
        configuration.databaseStarterPropertiesRoleSet().postProcessBeanDefinitionRegistry(registry);
        Mockito.verify(definition).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    }

    @Test
    public void roleSetWhenAbsent() {
        BeanDefinitionRegistry registry = Mockito.mock(BeanDefinitionRegistry.class);
        String beanName = GlobalConfigConst.DATABASE_DYNAMIC + "-" + DynamicDataSourceProperties.class.getName();
        Mockito.when(registry.containsBeanDefinition(beanName)).thenReturn(false);
        new RoleSetAutoConfiguration().databaseStarterPropertiesRoleSet().postProcessBeanDefinitionRegistry(registry);
        Mockito.verify(registry, Mockito.never()).getBeanDefinition(Mockito.anyString());
    }

    /**
     * 仅用于确保类可实例化
     */
    static class BeanDefinitionRegistryPostProcessorAssert {
        static void run(Class<?> clazz) {
            Assert.assertNotNull(clazz);
        }
    }
}
