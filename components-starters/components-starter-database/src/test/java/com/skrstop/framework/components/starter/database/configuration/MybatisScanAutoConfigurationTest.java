package com.skrstop.framework.components.starter.database.configuration;

import com.skrstop.framework.components.starter.database.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mock.env.MockEnvironment;

import java.lang.reflect.Method;

public class MybatisScanAutoConfigurationTest {

    @Test
    public void blankLocationDoesNothing() {
        MybatisScanAutoConfiguration configuration = new MybatisScanAutoConfiguration();
        configuration.setEnvironment(new MockEnvironment());
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        configuration.registerBeanDefinitions(AnnotationMetadata.introspect(MybatisScanAutoConfigurationTest.class), registry, new org.springframework.beans.factory.support.DefaultBeanNameGenerator());
        Assert.assertEquals(0, registry.getBeanDefinitionCount());
    }

    @Test
    public void emptyPackagesDoesNothing() {
        MybatisScanAutoConfiguration configuration = new MybatisScanAutoConfiguration();
        MockEnvironment env = new MockEnvironment();
        env.setProperty(GlobalConfigConst.DATABASE_CONFIG_CLASS_CLASS_LOCATION, " , ,");
        configuration.setEnvironment(env);
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        configuration.registerBeanDefinitions(AnnotationMetadata.introspect(MybatisScanAutoConfigurationTest.class), registry, new org.springframework.beans.factory.support.DefaultBeanNameGenerator());
        Assert.assertEquals(0, registry.getBeanDefinitionCount());
    }

    @Test
    public void registersScannerConfigurer() {
        MybatisScanAutoConfiguration configuration = new MybatisScanAutoConfiguration();
        MockEnvironment env = new MockEnvironment();
        env.setProperty(GlobalConfigConst.DATABASE_CONFIG_CLASS_CLASS_LOCATION, " com.a.mapper , com.b.mapper ");
        configuration.setEnvironment(env);
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        configuration.registerBeanDefinitions(AnnotationMetadata.introspect(MybatisScanAutoConfigurationTest.class), registry, new org.springframework.beans.factory.support.DefaultBeanNameGenerator());
        Assert.assertEquals(1, registry.getBeanDefinitionCount());
        String beanName = MybatisScanAutoConfigurationTest.class.getName() + "#MybatisScanAutoConfiguration#0";
        org.springframework.beans.factory.config.BeanDefinition definition = registry.getBeanDefinition(beanName);
        Assert.assertEquals(MapperScannerConfigurer.class.getName(), definition.getBeanClassName());
        Assert.assertEquals("com.a.mapper,com.b.mapper", definition.getPropertyValues().get("basePackage"));
        Assert.assertEquals(Boolean.TRUE, definition.getPropertyValues().get("processPropertyPlaceHolders"));
        Assert.assertEquals(org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE, definition.getRole());
    }

    @Test
    public void generateBaseBeanName() throws Exception {
        Method method = MybatisScanAutoConfiguration.class.getDeclaredMethod("generateBaseBeanName", AnnotationMetadata.class, int.class);
        method.setAccessible(true);
        String name = (String) method.invoke(null, AnnotationMetadata.introspect(MybatisScanAutoConfigurationTest.class), 3);
        Assert.assertEquals(MybatisScanAutoConfigurationTest.class.getName() + "#MybatisScanAutoConfiguration#3", name);
    }
}
