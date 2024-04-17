package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.mongodb.client.MongoClient;
import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.dsSelector.DsSpelExpressionSelector;
import com.skrstop.framework.components.starter.common.proxy.DynamicAnnotationAdvisor;
import com.skrstop.framework.components.starter.mongodb.annotation.DSMongo;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.aop.DynamicAnnotationInterceptor;
import com.skrstop.framework.components.starter.mongodb.constant.GlobalConfigConst;
import dev.morphia.Datastore;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * @author 蒋时华
 * @date 2020-10-17 10:57:45
 */
@Configuration
@EnableConfigurationProperties({DynamicMongodbProperties.class, GlobalMongodbProperties.class})
@AutoConfigureBefore(value = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ConditionalOnProperty(prefix = GlobalConfigConst.MONGODB_DYNAMIC, name = "enabled", havingValue = "true", matchIfMissing = false)
public class MorphiaDynamicAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DsSelector dsSelector(BeanFactory beanFactory) {
        DsSpelExpressionSelector spelExpressionProcessor = new DsSpelExpressionSelector();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return spelExpressionProcessor;
    }

    /**
     * aop
     *
     * @param dsSelector
     * @param dynamicMongoProperties
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnProperty(prefix = GlobalConfigConst.MONGODB_DYNAMIC + ".aop", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Advisor dynamicRedisDsRedisAnnotationAdvisor(DsSelector dsSelector, DynamicMongodbProperties dynamicMongoProperties) {
        DynamicMongoAopProperties aop = dynamicMongoProperties.getAop();
        DynamicAnnotationInterceptor interceptor = new DynamicAnnotationInterceptor(aop, dsSelector);
        DynamicAnnotationAdvisor advisor = new DynamicAnnotationAdvisor(interceptor, DSMongo.class);
        advisor.setOrder(aop.getOrder());
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean(MongoClient.class)
    public MongoClient mongo(DynamicMongodbProperties dynamicMongodbProperties, Environment environment, ObjectProvider<SslBundles> sslBundles) {
        return new DynamicMongoClient(dynamicMongodbProperties, environment, sslBundles);
    }

    @Bean
    @ConditionalOnMissingBean(Datastore.class)
    public Datastore datastore(MongoClient mongoClient, DynamicMongodbProperties dynamicMongodbProperties, GlobalMongodbProperties globalMongodbProperties) {
        return new DynamicDatastore(mongoClient, dynamicMongodbProperties, globalMongodbProperties);
    }

}
