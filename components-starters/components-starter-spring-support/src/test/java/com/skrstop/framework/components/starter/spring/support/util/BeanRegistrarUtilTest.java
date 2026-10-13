package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author 蒋时华
 */
public class BeanRegistrarUtilTest {

    @Test
    public void testRegisterInfrastructureBean() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();

        Assert.assertTrue(BeanRegistrarUtil.registerInfrastructureBean(registry, "bean", String.class));
        Assert.assertFalse(BeanRegistrarUtil.registerInfrastructureBean(registry, "bean", String.class));

        Assert.assertEquals(org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE,
                registry.getBeanDefinition("bean").getRole());
    }

    @Test
    public void testHasAlias() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registry.registerAlias("realBean", "aliasA");
        registry.registerAlias("realBean", "aliasB");

        Assert.assertTrue(BeanRegistrarUtil.hasAlias(registry, "realBean", "aliasA"));
        Assert.assertFalse(BeanRegistrarUtil.hasAlias(registry, "realBean", "aliasC"));
        Assert.assertFalse(BeanRegistrarUtil.hasAlias(registry, "", "aliasA"));
        Assert.assertFalse(BeanRegistrarUtil.hasAlias(registry, "realBean", ""));
        Assert.assertFalse(BeanRegistrarUtil.hasAlias(registry, null, null));
    }

}