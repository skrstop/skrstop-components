package com.zoe.framework.components.starter.annotation.configuration;

import com.zoe.framework.components.starter.annotation.anno.function.*;
import com.zoe.framework.components.starter.annotation.handle.function.*;
import com.zoe.framework.components.starter.annotation.handle.server.processor.ProcessorContainerConfiguration;
import com.zoe.framework.components.starter.annotation.handle.trim.TrimAnnotationFormatterFactory;
import com.zoe.framework.components.starter.common.aop.DynamicAopAnnotationAdvisor;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * @author 蒋时华
 * @date 2023-12-14 12:08:38
 */
@Configuration
@EnableConfigurationProperties(AnnotationProperties.class)
public class AnnotationAutoConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TrimAnnotationFormatterFactory trimAnnotationFormatterFactory() {
        return new TrimAnnotationFormatterFactory();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ProcessorContainerConfiguration processorContainerConfiguration() {
        return new ProcessorContainerConfiguration();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor executeTimeLog(AnnotationProperties annotationProperties) {
        ExecuteTimeAnnotationInterceptor interceptor = new ExecuteTimeAnnotationInterceptor(annotationProperties);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, ExecuteTimeLog.class);
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor accessControl(AnnotationProperties annotationProperties) {
        AccessControlAnnotationInterceptor interceptor = new AccessControlAnnotationInterceptor(annotationProperties);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, AccessControl.class);
        advisor.setOrder(1);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor intranetLimit(AnnotationProperties annotationProperties) {
        IntranetLimitAnnotationInterceptor interceptor = new IntranetLimitAnnotationInterceptor(annotationProperties);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, IntranetLimit.class);
        advisor.setOrder(2);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor accessLimit(AnnotationProperties annotationProperties) {
        AccessLimitAnnotationInterceptor interceptor = new AccessLimitAnnotationInterceptor(annotationProperties);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, AccessLimit.class);
        advisor.setOrder(3);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor serviceLock(AnnotationProperties annotationProperties) {
        ServiceLockAnnotationInterceptor interceptor = new ServiceLockAnnotationInterceptor(annotationProperties);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, ServiceLock.class);
        advisor.setOrder(4);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor privacyInfo(AnnotationProperties annotationProperties) {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(annotationProperties);
        DynamicAopAnnotationAdvisor advisor = new DynamicAopAnnotationAdvisor(interceptor, PrivacyInfo.class);
        advisor.setOrder(5);
        return advisor;
    }

}
