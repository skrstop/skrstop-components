package com.zoe.framework.components.starter.mongodb.configuration;

import com.mongodb.client.MongoClient;
import com.zoe.framework.components.starter.mongodb.constant.MongodbConst;
import com.zoe.framework.components.util.value.data.StrUtil;
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
@EnableConfigurationProperties(GlobalMongodbProperties.class)
public class MorphiaAutoConfiguration {

    @Bean
    public Datastore datastore(MongoClient mongoClient
            , MongoProperties mongoProperties
            , GlobalMongodbProperties globalMongodbProperties
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
        params.put("morphia.apply-caps", globalMongodbProperties.getApplyCaps().toString());
        params.put("morphia.apply-document-validations", globalMongodbProperties.getApplyDocumentValidations().toString());
        params.put("morphia.apply-indexes", globalMongodbProperties.getApplyIndexes().toString());
        params.put("morphia.auto-import-models", globalMongodbProperties.getAutoImportModels().toString());
        params.put("morphia.codec-provider", globalMongodbProperties.getCodecProvider());
        params.put("morphia.collection-naming", globalMongodbProperties.getCollectionNaming());
        params.put("morphia.database", databaseName);
        params.put("morphia.date-storage", globalMongodbProperties.getDateStorage());
        params.put("morphia.discriminator", globalMongodbProperties.getDiscriminator());
        params.put("morphia.discriminator-key", globalMongodbProperties.getDiscriminatorKey());
        params.put("morphia.enable-polymorphic-queries", globalMongodbProperties.getEnablePolymorphicQueries().toString());
        params.put("morphia.ignore-finals", globalMongodbProperties.getIgnoreFinals().toString());
        params.put("morphia.packages", globalMongodbProperties.getMapPackage());
        params.put("morphia.property-discovery", globalMongodbProperties.getPropertyDiscovery());
        params.put("morphia.property-naming", globalMongodbProperties.getPropertyNaming());
        params.put("morphia.query-factory", globalMongodbProperties.getQueryFactory());
        params.put("morphia.store-empties", globalMongodbProperties.getStoreEmpties().toString());
        params.put("morphia.store-nulls", globalMongodbProperties.getStoreNulls().toString());
        params.put("morphia.uuid-representation", globalMongodbProperties.getUuidRepresentation());
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
