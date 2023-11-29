package com.jphoebe.framework.components.starter.mongodb.configuration;

import com.jphoebe.framework.components.starter.mongodb.config.GlobalMongodbConfig;
import com.jphoebe.framework.components.starter.mongodb.constant.MongodbConst;
import com.jphoebe.framework.components.util.value.data.StrUtil;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.config.MorphiaConfig;
import io.smallrye.config.ConfigValuePropertiesConfigSource;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-10-17 10:57:45
 */
@Configuration
@EnableConfigurationProperties(GlobalMongodbConfig.class)
public class MorphiaAutoConfiguration {

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
        HashMap<String, String> params = new HashMap<>();
        params.put("morphia.apply-caps", globalMongodbConfig.getApplyCaps().toString());
        params.put("morphia.apply-document-validations", globalMongodbConfig.getApplyDocumentValidations().toString());
        params.put("morphia.apply-indexes", globalMongodbConfig.getApplyIndexes().toString());
        params.put("morphia.auto-import-models", globalMongodbConfig.getAutoImportModels().toString());
        params.put("morphia.codec-provider", globalMongodbConfig.getCodecProvider());
        params.put("morphia.collection-naming", globalMongodbConfig.getCollectionNaming());
        params.put("morphia.database", databaseName);
        params.put("morphia.date-storage", globalMongodbConfig.getDateStorage());
        params.put("morphia.discriminator", globalMongodbConfig.getDiscriminator());
        params.put("morphia.discriminator-key", globalMongodbConfig.getDiscriminatorKey());
        params.put("morphia.enable-polymorphic-queries", globalMongodbConfig.getEnablePolymorphicQueries().toString());
        params.put("morphia.ignore-finals", globalMongodbConfig.getIgnoreFinals().toString());
        params.put("morphia.packages", globalMongodbConfig.getMapPackage());
        params.put("morphia.property-discovery", globalMongodbConfig.getPropertyDiscovery());
        params.put("morphia.property-naming", globalMongodbConfig.getPropertyNaming());
        params.put("morphia.query-factory", globalMongodbConfig.getQueryFactory());
        params.put("morphia.store-empties", globalMongodbConfig.getStoreEmpties().toString());
        params.put("morphia.store-nulls", globalMongodbConfig.getStoreNulls().toString());
        params.put("morphia.uuid-representation", globalMongodbConfig.getUuidRepresentation());
        ConfigValuePropertiesConfigSource configValue = new ConfigValuePropertiesConfigSource(params, "default", 0);
        List<ConfigSource> configSources = new ArrayList<>();
        configSources.add(configValue);
        MorphiaConfig morphiaConfig = new SmallRyeConfigBuilder()
                .addDefaultInterceptors()
                .withMapping(MorphiaConfig.class)
                .withSources(configSources)
                .addDefaultSources()
                .build()
                .getConfigMapping(MorphiaConfig.class);
        return Morphia.createDatastore(mongoClient, morphiaConfig);
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
    @ConditionalOnBean(ReactiveMongoDatabaseFactory.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

}
