package com.skrstop.framework.components.starter.annotation.configuration;

import com.skrstop.framework.components.starter.annotation.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.Arrays;

/**
 * @author 蒋时华
 */
public class AnnotationPropertiesTest {

    @Test
    public void testDefaults() {
        AnnotationProperties properties = new AnnotationProperties();
        Assert.assertTrue(properties.isAllowedPublicOnly());
        Assert.assertNotNull(properties.getAccessControl());
        Assert.assertNotNull(properties.getExecuteTimeLog());
        Assert.assertTrue(properties.getExecuteTimeLog().isLogLevelInfo());
        Assert.assertNotNull(properties.getAccessControl().getReleaseAlias());
        // setter
        properties.setAllowedPublicOnly(false);
        Assert.assertFalse(properties.isAllowedPublicOnly());
        properties.getAccessControl().setReleaseAlias(Arrays.asList("a"));
        Assert.assertEquals(Arrays.asList("a"), properties.getAccessControl().getReleaseAlias());
        properties.getExecuteTimeLog().setLogLevelInfo(false);
        Assert.assertFalse(properties.getExecuteTimeLog().isLogLevelInfo());
    }

    @Test
    public void testRoleSetAutoConfiguration() throws Exception {
        // 真实的 BeanDefinitionRegistryPostProcessor
        org.springframework.beans.factory.config.BeanFactoryPostProcessor processor =
                RoleSetAutoConfiguration.annotationStarterPropertiesRoleSet();
        String beanName = GlobalConfigConst.ANNOTATION_PREFIX + "-" + AnnotationProperties.class.getName();
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // 不存在 → 跳过
        ((org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor) processor)
                .postProcessBeanDefinitionRegistry(registry);
        // 存在 → role 设置
        registry.registerBeanDefinition(beanName, new RootBeanDefinition(AnnotationProperties.class));
        ((org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor) processor)
                .postProcessBeanDefinitionRegistry(registry);
        Assert.assertEquals(BeanDefinition.ROLE_INFRASTRUCTURE,
                registry.getBeanDefinition(beanName).getRole());
    }

}