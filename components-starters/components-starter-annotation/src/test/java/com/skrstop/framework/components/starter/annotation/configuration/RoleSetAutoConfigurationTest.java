package com.skrstop.framework.components.starter.annotation.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.reflect.Proxy;

public class RoleSetAutoConfigurationTest {

    @SuppressWarnings("unused")
    private final Object ignore = new RoleSetAutoConfiguration();

    @SuppressWarnings("unchecked")
    private BeanDefinitionRegistry registry(boolean contains, BeanDefinition definition) {
        return (BeanDefinitionRegistry) Proxy.newProxyInstance(
                BeanDefinitionRegistry.class.getClassLoader(),
                new Class[]{BeanDefinitionRegistry.class},
                (proxy, method, args) -> {
                    if ("containsBeanDefinition".equals(method.getName())) {
                        return contains;
                    }
                    if ("getBeanDefinition".equals(method.getName())) {
                        return definition;
                    }
                    return null;
                });
    }

    @Test
    public void testContainsFalse() {
        BeanDefinitionRegistry registry = registry(false, null);
        RoleSetAutoConfiguration.annotationStarterPropertiesRoleSet()
                .postProcessBeanDefinitionRegistry(registry);
        Assert.assertNotNull(registry);
    }

    @Test
    public void testSetRole() {
        BeanDefinition definition = (BeanDefinition) Proxy.newProxyInstance(
                BeanDefinition.class.getClassLoader(),
                new Class[]{BeanDefinition.class},
                (proxy, method, args) -> {
                    if ("setRole".equals(method.getName())) {
                        Assert.assertEquals(BeanDefinition.ROLE_INFRASTRUCTURE, args[0]);
                        return null;
                    }
                    return null;
                });
        BeanDefinitionRegistry registry = registry(true, definition);
        RoleSetAutoConfiguration.annotationStarterPropertiesRoleSet()
                .postProcessBeanDefinitionRegistry(registry);
    }
}
