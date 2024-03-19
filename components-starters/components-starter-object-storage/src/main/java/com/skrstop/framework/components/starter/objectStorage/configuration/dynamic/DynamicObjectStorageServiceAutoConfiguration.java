package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic;

import com.skrstop.framework.components.starter.common.proxy.DynamicAnnotationAdvisor;
import com.skrstop.framework.components.starter.common.proxy.DynamicServiceAdvisor;
import com.skrstop.framework.components.starter.objectStorage.annotation.DSObjectStorage;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.aop.DynamicAnnotationInterceptor;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.service.DynamicServiceInterceptor;
import com.skrstop.framework.components.starter.objectStorage.selector.DsSelector;
import com.skrstop.framework.components.starter.objectStorage.selector.DsSpelExpressionSelector;
import com.skrstop.framework.components.starter.objectStorage.service.DynamicObjectStorageService;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.starter.objectStorage.service.impl.DynamicObjectStorageServiceImpl;
import com.skrstop.framework.components.starter.objectStorage.service.impl.MultipleObjectStorageServiceImpl;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author 蒋时华
 * @date 2020-05-11 20:00:01
 */
@Service
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "skrstop.object-storage.dynamic.enable", havingValue = "true", matchIfMissing = false)
public class DynamicObjectStorageServiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DsSelector dsSelector(BeanFactory beanFactory) {
        DsSpelExpressionSelector spelExpressionProcessor = new DsSpelExpressionSelector();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return spelExpressionProcessor;
    }


    @Bean(destroyMethod = "close")
    public ObjectStorageService cosObjectStorage(DynamicObjectStorageProperties dynamicObjectStorageProperties) {
        return new MultipleObjectStorageServiceImpl(dynamicObjectStorageProperties);
    }

    /**
     * aop
     *
     * @param dsSelector
     * @param dynamicObjectStorageProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnProperty(value = "skrstop.object-storage.dynamic.aop.enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicObjectStorageAnnotationAdvisor(DsSelector dsSelector, DynamicObjectStorageProperties dynamicObjectStorageProperties) {
        DynamicObjectStorageProperties.Aop aop = dynamicObjectStorageProperties.getAop();
        DynamicAnnotationInterceptor interceptor = new DynamicAnnotationInterceptor(dynamicObjectStorageProperties, dsSelector);
        DynamicAnnotationAdvisor advisor = new DynamicAnnotationAdvisor(interceptor, DSObjectStorage.class);
        advisor.setOrder(aop.getOrder());
        return advisor;
    }

    /**
     * service
     *
     * @param dsSelector
     * @param dynamicObjectStorageProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(value = "skrstop.object-storage.dynamic.service.enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicObjectStorageServiceAdvisor(DsSelector dsSelector, DynamicObjectStorageProperties dynamicObjectStorageProperties) {
        DynamicServiceInterceptor interceptor = new DynamicServiceInterceptor(dsSelector);
        DynamicServiceAdvisor advisor = new DynamicServiceAdvisor(interceptor, DynamicObjectStorageService.class);
        advisor.setOrder(dynamicObjectStorageProperties.getService().getOrder());
        return advisor;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnBean(ObjectStorageService.class)
    @ConditionalOnProperty(value = "skrstop.object-storage.dynamic.service.enabled", havingValue = "true", matchIfMissing = true)
    public DynamicObjectStorageService dynamicRedisService(ObjectStorageService objectStorageService) {
        return new DynamicObjectStorageServiceImpl(objectStorageService);
    }

}
