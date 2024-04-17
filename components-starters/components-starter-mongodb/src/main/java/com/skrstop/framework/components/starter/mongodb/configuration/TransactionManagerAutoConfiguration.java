package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

/**
 * @author 蒋时华
 * @date 2020-10-17 10:57:45
 */
@Configuration
@EnableConfigurationProperties(GlobalMongodbProperties.class)
public class TransactionManagerAutoConfiguration {

    @Bean(MongodbConst.TRANSACTION_NAME_MONGO)
    @ConditionalOnClass(name = "com.mongodb.client.MongoClient")
    @ConditionalOnMissingBean(MongoTransactionManager.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory factory) {
        return new MongoTransactionManager(factory);
    }

    @Bean(MongodbConst.TRANSACTION_REACTIVE_NAME_MONGO)
    @ConditionalOnClass(name = "com.mongodb.reactivestreams.client.MongoClient")
    @ConditionalOnMissingBean(ReactiveMongoTransactionManager.class)
    @ConditionalOnBean(ReactiveMongoDatabaseFactory.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ReactiveMongoTransactionManager mongoReactiveTransactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

}
