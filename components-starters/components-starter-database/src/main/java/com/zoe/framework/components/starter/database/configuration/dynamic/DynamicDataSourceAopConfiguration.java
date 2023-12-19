package com.zoe.framework.components.starter.database.configuration.dynamic;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.aop.DynamicLocalTransactionInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import com.zoe.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceProperties;
import com.zoe.framework.components.starter.database.configuration.dynamic.common.DynamicDatasourceAopProperties;
import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;

/**
 * 动态数据源核心自动配置类
 *
 * @author TaoYu Kanyuxia
 * @see DynamicDataSourceProvider
 * @see DynamicDataSourceStrategy
 * @see DynamicRoutingDataSource
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceAopConfiguration {

    private final DynamicDataSourceProperties properties;

    public DynamicDataSourceAopConfiguration(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor(BeanFactory beanFactory) {
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return spelExpressionProcessor;
    }


    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.DATABASE_DYNAMIC + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDatasourceAopProperties aopProperties = properties.getAop();
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(aopProperties.getAllowedPublicOnly(), dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
        advisor.setOrder(aopProperties.getOrder());
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnProperty(prefix = GlobalConfigConst.DATABASE_DYNAMIC, name = "seata", havingValue = "false", matchIfMissing = true)
    public Advisor dynamicTransactionAdvisor() {
        DynamicDatasourceAopProperties aopProperties = properties.getAop();
        DynamicLocalTransactionInterceptor interceptor = new DynamicLocalTransactionInterceptor(aopProperties.getAllowedPublicOnly());
        return new DynamicDataSourceAnnotationAdvisor(interceptor, DSTransactional.class);
    }

}