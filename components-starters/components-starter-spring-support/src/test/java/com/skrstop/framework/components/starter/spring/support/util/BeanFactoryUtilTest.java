package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.List;

/**
 * @author 蒋时华
 */
public class BeanFactoryUtilTest {

    @Test
    public void testGetOptionalBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("str", new RootBeanDefinition(String.class));

        Assert.assertNull(BeanFactoryUtil.getOptionalBean(beanFactory, "", String.class));
        Assert.assertNull(BeanFactoryUtil.getOptionalBean(beanFactory, null, String.class));
        Assert.assertNull(BeanFactoryUtil.getOptionalBean(beanFactory, "  ", String.class));

        String bean = BeanFactoryUtil.getOptionalBean(beanFactory, "str", String.class);
        Assert.assertNotNull(bean);

        // 名字存在但类型不匹配
        Assert.assertNull(BeanFactoryUtil.getOptionalBean(beanFactory, "str", Integer.class));
    }

    @Test
    public void testGetBeans() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("a", new RootBeanDefinition(String.class));
        beanFactory.registerBeanDefinition("b", new RootBeanDefinition(String.class));

        Assert.assertEquals(0, BeanFactoryUtil.getBeans(beanFactory, new String[0], String.class).size());
        Assert.assertEquals(0, BeanFactoryUtil.getBeans(beanFactory, null, String.class).size());

        List<String> beans = BeanFactoryUtil.getBeans(beanFactory, new String[]{"a", "b", "c"}, String.class);
        Assert.assertEquals(2, beans.size());

        // 空名字
        Assert.assertEquals(0, BeanFactoryUtil.getBeans(beanFactory, new String[]{""}, String.class).size());
    }

}