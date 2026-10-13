package com.skrstop.framework.components.starter.feign.protostuff.configuration.webmvc;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.function.Supplier;

public class ProtobufFeignClientBeanDefinitionPostProcessorTest {

    @ProtostuffFeignClient("marked-svc")
    interface MarkedClient {
    }

    @Test
    public void postProcessWithFactoryBeanAttribute() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // feign client bean definition (GenericBeanDefinition + instanceSupplier + factoryBean 属性)
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        clientDefinition.setInstanceSupplier((Supplier<MarkedClient>) () -> null);
        org.springframework.cloud.openfeign.FeignClientFactoryBean factoryBean =
                new org.springframework.cloud.openfeign.FeignClientFactoryBean();
        factoryBean.setName("marked-svc");
        factoryBean.setContextId("");
        clientDefinition.setAttribute("feignClientsRegistrarFactoryBean", factoryBean);
        registry.registerBeanDefinition("markedClient", clientDefinition);
        // 对应的 specification bean, index1 为 configuration Object[]
        BeanDefinitionBuilder specBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(org.springframework.cloud.openfeign.FeignClientSpecification.class);
        specBuilder.addConstructorArgValue("marked-svc");
        specBuilder.addConstructorArgValue(new Object[0]);
        specBuilder.addConstructorArgValue("marked-svc");
        registry.registerBeanDefinition("marked-svc.FeignClientSpecification", specBuilder.getBeanDefinition());

        ProtobufFeignClientBeanDefinitionPostProcessor processor = new ProtobufFeignClientBeanDefinitionPostProcessor();
        processor.postProcessBeanDefinitionRegistry(registry);
        ConstructorArgumentValues.ValueHolder holder = registry.getBeanDefinition("marked-svc.FeignClientSpecification")
                .getConstructorArgumentValues().getIndexedArgumentValues().get(1);
        Object[] configuration = (Object[]) holder.getValue();
        boolean contains = false;
        for (Object o : configuration) {
            if (o == FeignMessageConverterAutoConfiguration.class || o instanceof Class) {
                contains = true;
            }
        }
        Assert.assertTrue(contains);
        processor.postProcessBeanFactory(registry);
    }

    @Test
    public void postProcessWithoutFactoryBeanUsesPropertyValues() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        clientDefinition.setInstanceSupplier((Supplier<MarkedClient>) () -> null);
        clientDefinition.getPropertyValues().add("name", "pv-name");
        clientDefinition.getPropertyValues().add("contextId", "pv-context");
        registry.registerBeanDefinition("markedClient", clientDefinition);
        BeanDefinitionBuilder specBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(org.springframework.cloud.openfeign.FeignClientSpecification.class);
        specBuilder.addConstructorArgValue("pv-context");
        specBuilder.addConstructorArgValue(new Object[0]);
        specBuilder.addConstructorArgValue("pv-context");
        registry.registerBeanDefinition("pv-context.FeignClientSpecification", specBuilder.getBeanDefinition());

        new ProtobufFeignClientBeanDefinitionPostProcessor().postProcessBeanDefinitionRegistry(registry);
        // contextId 非空优先
        Assert.assertNotNull(registry.getBeanDefinition("pv-context.FeignClientSpecification"));
    }

    @Test
    public void nonGenericBeanDefinitionRemoved() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // RootBeanDefinition 不是 GenericBeanDefinition -> 迭代器移除
        org.springframework.beans.factory.support.RootBeanDefinition rootBeanDefinition =
                new org.springframework.beans.factory.support.RootBeanDefinition(MarkedClient.class);
        registry.registerBeanDefinition("rootClient", rootBeanDefinition);

        ProtobufFeignClientBeanDefinitionPostProcessor processor = new ProtobufFeignClientBeanDefinitionPostProcessor();
        // serviceName 被清空, forEach 不执行
        processor.postProcessBeanDefinitionRegistry(registry);
        Assert.assertNotNull(registry.getBeanDefinition("rootClient"));
    }

    @Test
    public void instanceSupplierNullRemoved() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        registry.registerBeanDefinition("markedClient", clientDefinition);
        new ProtobufFeignClientBeanDefinitionPostProcessor().postProcessBeanDefinitionRegistry(registry);
        Assert.assertNotNull(registry.getBeanDefinition("markedClient"));
    }

    @Test(expected = RuntimeException.class)
    public void nameAndContextIdAllNullThrows() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        clientDefinition.setInstanceSupplier((Supplier<MarkedClient>) () -> null);
        registry.registerBeanDefinition("markedClient", clientDefinition);
        new ProtobufFeignClientBeanDefinitionPostProcessor().postProcessBeanDefinitionRegistry(registry);
    }

    @Test(expected = RuntimeException.class)
    public void nameAndContextIdAllBlankThrows() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        clientDefinition.setInstanceSupplier((Supplier<MarkedClient>) () -> null);
        clientDefinition.getPropertyValues().add("name", "");
        clientDefinition.getPropertyValues().add("contextId", "");
        registry.registerBeanDefinition("markedClient", clientDefinition);
        new ProtobufFeignClientBeanDefinitionPostProcessor().postProcessBeanDefinitionRegistry(registry);
    }

    @Test(expected = RuntimeException.class)
    public void factoryBeanNameAndContextIdAllBlankThrows() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        clientDefinition.setInstanceSupplier((Supplier<MarkedClient>) () -> null);
        org.springframework.cloud.openfeign.FeignClientFactoryBean factoryBean =
                new org.springframework.cloud.openfeign.FeignClientFactoryBean();
        clientDefinition.setAttribute("feignClientsRegistrarFactoryBean", factoryBean);
        registry.registerBeanDefinition("markedClient", clientDefinition);
        new ProtobufFeignClientBeanDefinitionPostProcessor().postProcessBeanDefinitionRegistry(registry);
    }


    @Test
    public void postProcessWithOnlyName() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        GenericBeanDefinition clientDefinition = new GenericBeanDefinition();
        clientDefinition.setBeanClass(MarkedClient.class);
        clientDefinition.setInstanceSupplier((Supplier<MarkedClient>) () -> null);
        // 仅 name, 无 contextId -> name==null&&contextId==null 为 false, isAllBlank 为 false
        clientDefinition.getPropertyValues().add("contextId", "only-ctx");
        registry.registerBeanDefinition("markedClient", clientDefinition);
        BeanDefinitionBuilder specBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(org.springframework.cloud.openfeign.FeignClientSpecification.class);
        specBuilder.addConstructorArgValue("only-ctx");
        specBuilder.addConstructorArgValue(new Object[0]);
        specBuilder.addConstructorArgValue("only-ctx");
        registry.registerBeanDefinition("only-ctx.FeignClientSpecification", specBuilder.getBeanDefinition());

        // name 为 null 时 StrUtil.toString(name, EMPTY) 会 NPE(存量缺陷, 与 null 判断分支无关)
        try {
            new ProtobufFeignClientBeanDefinitionPostProcessor().postProcessBeanDefinitionRegistry(registry);
            Assert.fail("should throw");
        } catch (NullPointerException e) {
            // expected
        }
    }
}
