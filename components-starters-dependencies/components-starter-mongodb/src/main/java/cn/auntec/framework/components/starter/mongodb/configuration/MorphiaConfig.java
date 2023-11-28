package cn.auntec.framework.components.starter.mongodb.configuration;

import cn.auntec.framework.components.core.exception.defined.illegal.ParameterException;
import cn.auntec.framework.components.starter.mongodb.config.GlobalMongodbConfig;
import cn.auntec.framework.components.starter.mongodb.constant.MongodbConst;
import cn.auntec.framework.components.util.value.data.StrUtil;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
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
@EnableConfigurationProperties(GlobalMongodbConfig.class)
public class MorphiaConfig {

    @Bean
    public Datastore datastore(MongoClient mongoClient
            , MongoProperties mongoProperties
            , GlobalMongodbConfig globalMongodbConfig
    ) {
        String databaseName = mongoProperties.getDatabase();
        if (StrUtil.isBlank(databaseName) && StrUtil.isNotBlank(mongoProperties.getUri())) {
            String uri = mongoProperties.getUri();
            int start = uri.indexOf("//") + 2;
            String substring = uri.substring(start);
            int i = substring.indexOf("/") + 1;
            int end = substring.indexOf("?");
            if (end <= -1) {
                databaseName = substring.substring(i);
            } else {
                databaseName = substring.substring(i, end);
            }
        }

        if (StrUtil.isBlank(databaseName)) {
            throw new RuntimeException("请配置mongodb相关参数");
        }

        MapperOptions mapperOptions = MapperOptions.builder().mapSubPackages(true).build();
        Datastore datastore = Morphia.createDatastore(mongoClient, databaseName, mapperOptions);
        if (StrUtil.isBlank(globalMongodbConfig.getMapPackage())) {
            throw new ParameterException("请填写，mongodb对应实体类包路径");
        }
        datastore.getMapper().mapPackage(globalMongodbConfig.getMapPackage());
        // 确保实体类的映射建立
        datastore.ensureIndexes();
        return datastore;
    }

    @Bean(MongodbConst.TRANSACTION_NAME)
    @ConditionalOnClass(name = "com.mongodb.client.MongoClient")
    @ConditionalOnMissingBean(MongoTransactionManager.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public MongoTransactionManager transactionManager(MongoDatabaseFactory factory) {
        return new MongoTransactionManager(factory);
    }

    @Bean(MongodbConst.TRANSACTION_REACTIVE_NAME)
    @ConditionalOnClass(name = "com.mongodb.reactivestreams.client.MongoClient")
    @ConditionalOnMissingBean(ReactiveMongoTransactionManager.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

}
