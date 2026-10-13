package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author 蒋时华
 */
public class AnnotatedBeanDefinitionRegistryUtilTest {

    @Component
    public static class ScannedComponent {
    }

    @Test
    public void testRegisterBeans() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // 空数组直接返回
        AnnotatedBeanDefinitionRegistryUtil.registerBeans(registry);
        AnnotatedBeanDefinitionRegistryUtil.registerBeans(registry, (Class<?>[]) null);
        Assert.assertEquals(0, registry.getBeanDefinitionCount());

        // 注册
        AnnotatedBeanDefinitionRegistryUtil.registerBeans(registry, ScannedComponent.class);
        Assert.assertTrue(AnnotatedBeanDefinitionRegistryUtil.isPresentBean(registry, ScannedComponent.class));

        // 再次注册（已存在被过滤）
        int before = registry.getBeanDefinitionCount();
        AnnotatedBeanDefinitionRegistryUtil.registerBeans(registry, ScannedComponent.class);
        Assert.assertEquals(before, registry.getBeanDefinitionCount());
    }

    @Test
    public void testIsPresentBean() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        Assert.assertFalse(AnnotatedBeanDefinitionRegistryUtil.isPresentBean(registry, ScannedComponent.class));
        // 非 AnnotatedBeanDefinition 被跳过
        registry.registerBeanDefinition("plain", new org.springframework.beans.factory.support.RootBeanDefinition(String.class));
        Assert.assertFalse(AnnotatedBeanDefinitionRegistryUtil.isPresentBean(registry, ScannedComponent.class));
        // 注册 annotated bean 后为 true
        registry.registerBeanDefinition("comp", new AnnotatedGenericBeanDefinition(ScannedComponent.class));
        Assert.assertTrue(AnnotatedBeanDefinitionRegistryUtil.isPresentBean(registry, ScannedComponent.class));
        Assert.assertFalse(AnnotatedBeanDefinitionRegistryUtil.isPresentBean(registry, String.class));
    }

    @Test
    public void testScanBasePackages() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // 空 basePackages
        Assert.assertEquals(0, AnnotatedBeanDefinitionRegistryUtil.scanBasePackages(registry));
        Assert.assertEquals(0, AnnotatedBeanDefinitionRegistryUtil.scanBasePackages(registry, (String[]) null));
        Assert.assertEquals(0, AnnotatedBeanDefinitionRegistryUtil.scanBasePackages(registry, new String[0]));
        // 扫描本包
        int count = AnnotatedBeanDefinitionRegistryUtil.scanBasePackages(registry,
                "com.skrstop.framework.components.starter.spring.support.util");
        Assert.assertTrue(count >= 0);
    }

}