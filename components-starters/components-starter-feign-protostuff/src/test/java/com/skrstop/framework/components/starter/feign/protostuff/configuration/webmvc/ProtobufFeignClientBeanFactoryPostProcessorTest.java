package com.skrstop.framework.components.starter.feign.protostuff.configuration.webmvc;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class ProtobufFeignClientBeanFactoryPostProcessorTest {

    @ProtostuffFeignClient("marked-svc")
    interface MarkedClient {
    }

    @ProtostuffFeignClient("not-interface-svc")
    static class AnnotatedClass {
    }

    @Test
    public void registersAdvisorForAnnotatedInterfaces() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(MarkedClient.class);
        beanFactory.registerBeanDefinition("markedClient", definition);
        // 有注解但非 interface -> 跳过
        GenericBeanDefinition classDefinition = new GenericBeanDefinition();
        classDefinition.setBeanClass(AnnotatedClass.class);
        beanFactory.registerBeanDefinition("annotatedClass", classDefinition);

        new ProtobufFeignClientBeanFactoryPostProcessor().postProcessBeanFactory(beanFactory);
        Object advisor = beanFactory.getSingleton("FeignClientMethodInterceptor");
        Assert.assertNotNull(advisor);
    }

    @Test
    public void noAnnotatedBeans() {
        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        new ProtobufFeignClientBeanFactoryPostProcessor().postProcessBeanFactory(beanFactory);
        Assert.assertNull(((DefaultListableBeanFactory) beanFactory).getSingleton("FeignClientMethodInterceptor"));
    }


    @Test
    public void beanTypeNullSkips() {
        org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory =
                Mockito.mock(org.springframework.beans.factory.config.ConfigurableListableBeanFactory.class);
        Mockito.when(beanFactory.getBeanNamesForAnnotation(ProtostuffFeignClient.class))
                .thenReturn(new String[]{"ghost"});
        // getType 返回 null -> continue -> service 空 -> 直接返回
        new ProtobufFeignClientBeanFactoryPostProcessor().postProcessBeanFactory(beanFactory);
        Mockito.verify(beanFactory, Mockito.never()).getBeansWithAnnotation(Mockito.any());
    }
}
