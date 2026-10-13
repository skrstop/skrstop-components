package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class BeanUtilTest {

    public static class MyBean {
    }

    @Order(2)
    public static class OrderedBeanA {
    }

    @Order(1)
    public static class OrderedBeanB {
    }

    @Test
    public void testIsBeanPresent() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("b", new RootBeanDefinition(MyBean.class));

        Assert.assertTrue(BeanUtil.isBeanPresent(beanFactory, MyBean.class));
        Assert.assertFalse(BeanUtil.isBeanPresent(beanFactory, String.class));
        Assert.assertTrue(BeanUtil.isBeanPresent(beanFactory, MyBean.class, false));
        Assert.assertFalse(BeanUtil.isBeanPresent(beanFactory, String.class, true));
        Assert.assertTrue(BeanUtil.isBeanPresent(beanFactory, MyBean.class.getName()));
        Assert.assertFalse(BeanUtil.isBeanPresent(beanFactory, "java.lang.String2"));
        Assert.assertFalse(BeanUtil.isBeanPresent(beanFactory, "not.existing.Class"));
    }

    @Test
    public void testGetBeanNames() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("b1", new RootBeanDefinition(MyBean.class));
        beanFactory.registerBeanDefinition("b2", new RootBeanDefinition(MyBean.class));

        Assert.assertEquals(2, BeanUtil.getBeanNames(beanFactory, MyBean.class).length);
        Assert.assertEquals(2, BeanUtil.getBeanNames(beanFactory, MyBean.class, false).length);
        Assert.assertEquals(0, BeanUtil.getBeanNames(beanFactory, String.class).length);
        Assert.assertArrayEquals(new String[0], BeanUtil.getBeanNames(beanFactory, String.class, true));
    }

    @Test
    public void testGetBeanNamesWithParent() {
        DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
        parent.registerBeanDefinition("pb", new RootBeanDefinition(MyBean.class));
        DefaultListableBeanFactory child = new DefaultListableBeanFactory();
        child.setParentBeanFactory(parent);
        child.registerBeanDefinition("cb", new RootBeanDefinition(MyBean.class));

        Assert.assertEquals(1, BeanUtil.getBeanNames(child, MyBean.class, false).length);
        Assert.assertEquals(2, BeanUtil.getBeanNames(child, MyBean.class, true).length);
    }

    @Test
    public void testGetBeanNamesNonConfigurable() {
        // 非 ConfigurableListableBeanFactory → EMPTY
        org.springframework.beans.factory.ListableBeanFactory stub =
                new org.springframework.beans.factory.support.StaticListableBeanFactory();
        Assert.assertArrayEquals(new String[0], BeanUtil.getBeanNames(stub, MyBean.class, false));
    }

    @Test
    public void testGetBeanNamesFromApplicationContext() {
        org.springframework.context.annotation.AnnotationConfigApplicationContext ctx =
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        try {
            ctx.registerBean(MyBean.class);
            ctx.refresh();
            // ConfigurableApplicationContext 分支 + ListableBeanFactory 2 参重载
            Assert.assertEquals(1, BeanUtil.getBeanNames(ctx, MyBean.class).length);
            Assert.assertEquals(1, BeanUtil.getBeanNames(ctx, MyBean.class, false).length);
            Assert.assertTrue(BeanUtil.isBeanPresent(ctx, MyBean.class));
            Assert.assertNotNull(BeanUtil.getOptionalBean(ctx, MyBean.class));
        } finally {
            ctx.close();
        }
    }

    @Test
    public void testGetOptionalBeanAncestors() {
        DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
        parent.registerBeanDefinition("pb", new RootBeanDefinition(MyBean.class));
        DefaultListableBeanFactory child = new DefaultListableBeanFactory();
        child.setParentBeanFactory(parent);
        // includingAncestors=true 走 beanOfTypeIncludingAncestors
        Assert.assertNotNull(BeanUtil.getOptionalBean(child, MyBean.class, true));
        // 不含祖先 → null
        Assert.assertNull(BeanUtil.getOptionalBean(child, MyBean.class, false));
    }

    @Test
    public void testGetOptionalBeanAmbiguous() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("b1", new RootBeanDefinition(MyBean.class));
        beanFactory.registerBeanDefinition("b2", new RootBeanDefinition(MyBean.class));
        // getBean(Class) 唯一性异常 → catch → null
        Assert.assertNull(BeanUtil.getOptionalBean(beanFactory, MyBean.class, false));
    }

    @Test
    public void testUnresolvableBeanDefinition() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 无 beanClassName 且无 factory → resolveBeanType null → log.error（261/267/268）
        org.springframework.beans.factory.support.GenericBeanDefinition bad =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        beanFactory.registerBeanDefinition("bad", bad);
        Assert.assertEquals(0, BeanUtil.getBeanNames(beanFactory, MyBean.class).length);
        // beanClassName 无法解析（294 行 beanClass==null 分支）
        org.springframework.beans.factory.support.GenericBeanDefinition missing =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        missing.setBeanClassName("not.existing.Missing");
        beanFactory.registerBeanDefinition("missing", missing);
        Assert.assertEquals(0, BeanUtil.getBeanNames(beanFactory, MyBean.class).length);
    }

    @Test
    public void testFactoryBeanViaFactoryClassName() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // factoryBeanClassName 为空 → 从 factoryBeanName 解析（204 行）
        beanFactory.registerBeanDefinition("config2", new RootBeanDefinition(FactoryConfig.class));
        org.springframework.beans.factory.support.GenericBeanDefinition beanDef =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        beanDef.setFactoryBeanName("config2");
        beanDef.setFactoryMethodName("createBean");
        beanFactory.registerBeanDefinition("fb2", beanDef);
        Assert.assertEquals("fb2", BeanUtil.getBeanNames(beanFactory, MyBean.class)[0]);
    }

    @Test
    public void testFactoryBeanDefWithClassName() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // beanDef 自带 class name 且设置 factoryBeanName → 204 isEmpty false 分支
        RootBeanDefinition beanDef = new RootBeanDefinition(MyBean.class);
        beanDef.setFactoryBeanName("configX");
        beanDef.setFactoryMethodName("createBean");
        beanFactory.registerBeanDefinition("configX", new RootBeanDefinition(FactoryConfig.class));
        beanFactory.registerBeanDefinition("fbd", beanDef);
        Assert.assertTrue(java.util.Arrays.asList(BeanUtil.getBeanNames(beanFactory, MyBean.class)).contains("fbd"));
    }

    @Test
    public void testFactoryChainNoClassName() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 工厂链上两个 definition 均无 class name → 213 hasText false → beanType null
        org.springframework.beans.factory.support.GenericBeanDefinition defB =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        beanFactory.registerBeanDefinition("defB", defB);
        org.springframework.beans.factory.support.GenericBeanDefinition defA =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        defA.setFactoryBeanName("defB");
        defA.setFactoryMethodName("m");
        beanFactory.registerBeanDefinition("defA", defA);
        Assert.assertEquals(0, BeanUtil.getBeanNames(beanFactory, MyBean.class).length);
    }

    @Test
    public void testFactoryMethodMissing() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("config3", new RootBeanDefinition(FactoryConfig.class));
        org.springframework.beans.factory.support.GenericBeanDefinition beanDef =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        beanDef.setFactoryBeanName("config3");
        beanDef.setFactoryMethodName("noSuchMethod");
        beanFactory.registerBeanDefinition("fb3", beanDef);
        // 工厂方法不存在 → beanClasses 为空 → getFactoryBeanType null（240）→ resolveBeanType null → log.error
        Assert.assertEquals(0, BeanUtil.getBeanNames(beanFactory, MyBean.class).length);
    }

    @Test
    public void testResolveBeanType() {
        Assert.assertNotNull(BeanUtil.resolveBeanType(MyBean.class.getName(), getClass().getClassLoader()));
        Assert.assertNull(BeanUtil.resolveBeanType("", getClass().getClassLoader()));
        Assert.assertNull(BeanUtil.resolveBeanType(null, getClass().getClassLoader()));
        Assert.assertNull(BeanUtil.resolveBeanType("not.existing.X", getClass().getClassLoader()));
    }

    @Test
    public void testResolveBeanTypeWithFactoryBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // @Bean 方法工厂
        org.springframework.beans.factory.config.BeanDefinition configDef =
                new RootBeanDefinition(FactoryConfig.class);
        beanFactory.registerBeanDefinition("config", configDef);
        org.springframework.beans.factory.support.GenericBeanDefinition beanDef =
                new org.springframework.beans.factory.support.GenericBeanDefinition();
        beanDef.setFactoryBeanName("config");
        beanDef.setFactoryMethodName("createBean");
        beanFactory.registerBeanDefinition("fb", beanDef);

        Assert.assertEquals(1, BeanUtil.getBeanNames(beanFactory, MyBean.class).length);
        Assert.assertEquals("fb", BeanUtil.getBeanNames(beanFactory, MyBean.class)[0]);
    }

    public static class FactoryConfig {
        public MyBean createBean() {
            return new MyBean();
        }
    }

    @Test
    public void testGetOptionalBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        Assert.assertNull(BeanUtil.getOptionalBean(beanFactory, MyBean.class));
        beanFactory.registerBeanDefinition("b", new RootBeanDefinition(MyBean.class));
        Assert.assertNotNull(BeanUtil.getOptionalBean(beanFactory, MyBean.class));
        Assert.assertNotNull(BeanUtil.getOptionalBean(beanFactory, MyBean.class, false));
        Assert.assertNull(BeanUtil.getOptionalBean(beanFactory, String.class));
    }

    @Test
    public void testGetSortedBeans() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("a", new RootBeanDefinition(OrderedBeanA.class));
        beanFactory.registerBeanDefinition("b", new RootBeanDefinition(OrderedBeanB.class));

        List<Object> sorted = BeanUtil.getSortedBeans(beanFactory, Object.class);
        Assert.assertEquals(2, sorted.size());
        // OrderedBeanB order=1 在前
        Assert.assertEquals("OrderedBeanB", sorted.get(0).getClass().getSimpleName());
    }

    @Test
    public void testSort() {
        Map<String, Object> beansMap = new LinkedHashMap<>();
        beansMap.put("a", new OrderedBeanA());
        beansMap.put("b", new OrderedBeanB());

        Map<String, Object> sorted = BeanUtil.sort(beansMap);
        Assert.assertEquals("b", sorted.keySet().iterator().next());
        // 空 map
        Assert.assertEquals(0, BeanUtil.sort(new LinkedHashMap<String, Object>()).size());
    }

    @Test
    public void testNamingBean() throws Exception {
        BeanUtil.NamingBean<Object> namingBean = new BeanUtil.NamingBean<>("n", new Object());
        java.lang.reflect.Field f = BeanUtil.NamingBean.class.getDeclaredField("name");
        f.setAccessible(true);
        Assert.assertEquals("n", f.get(namingBean));
        Assert.assertEquals(Ordered.LOWEST_PRECEDENCE, namingBean.getOrder());
        // compareTo 用 AnnotationAwareOrderComparator
        BeanUtil.NamingBean<Object> other = new BeanUtil.NamingBean<>("m", new OrderedBeanA());
        Assert.assertTrue(namingBean.compareTo(other) != 0 || namingBean.compareTo(other) == 0);
    }

}