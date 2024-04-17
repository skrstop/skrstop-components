package com.skrstop.framework.components.starter.feign.protostuff.configuration.webmvc;

import com.skrstop.framework.components.starter.common.proxy.DynamicMultiServiceAdvisor;
import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor.FeignClientMethodInterceptor;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册protostuff的方法代理类
 *
 * @author 蒋时华
 * @date 2020-05-14 13:14:34
 */
@Configuration
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ProtobufFeignClientBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 扫描有protostuffFeignClient的注解
        String[] beanNamesForAnnotation = beanFactory.getBeanNamesForAnnotation(ProtostuffFeignClient.class);
        List<String> serviceName = CollectionUtil.newArrayList(beanNamesForAnnotation);
        List<Class<?>> service = new ArrayList<>();
        for (String name : serviceName) {
            Class<?> type = beanFactory.getType(name);
            if (type == null || !type.isInterface()) {
                continue;
            }
            service.add(type);
        }
        if (service.isEmpty()) {
            return;
        }
        // 注册protostuff的方法代理类
        FeignClientMethodInterceptor interceptor = new FeignClientMethodInterceptor();
        DynamicMultiServiceAdvisor advisor = new DynamicMultiServiceAdvisor(interceptor, service);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        beanFactory.registerSingleton(FeignClientMethodInterceptor.class.getSimpleName(), advisor);
    }
}
