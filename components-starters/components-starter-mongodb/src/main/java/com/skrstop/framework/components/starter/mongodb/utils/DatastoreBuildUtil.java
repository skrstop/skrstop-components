package com.skrstop.framework.components.starter.mongodb.utils;

import com.mongodb.client.MongoClient;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.util.value.data.StrUtil;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.config.MorphiaConfig;
import io.smallrye.config.DefaultValuesConfigSource;
import io.smallrye.config.SmallRyeConfigBuilder;
import lombok.experimental.UtilityClass;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2024-04-17 16:15:50
 * @since 1.0.0
 */
@UtilityClass
public class DatastoreBuildUtil {

    public static Datastore build(MongoClient mongoClient
            , MongoProperties mongoProperties
            , GlobalMongodbProperties globalMongodbProperties) {
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
        params.put("morphia.apply-caps", StrUtil.toString(globalMongodbProperties.isApplyCaps()));
        params.put("morphia.apply-document-validations", StrUtil.toString(globalMongodbProperties.isApplyDocumentValidations()));
        params.put("morphia.apply-indexes", StrUtil.toString(globalMongodbProperties.isApplyIndexes()));
        params.put("morphia.auto-import-models", StrUtil.toString(globalMongodbProperties.isAutoImportModels()));
        params.put("morphia.codec-provider", globalMongodbProperties.getCodecProvider());
        params.put("morphia.collection-naming", globalMongodbProperties.getCollectionNaming());
        params.put("morphia.database", databaseName);
        params.put("morphia.date-storage", globalMongodbProperties.getDateStorage());
        params.put("morphia.discriminator", globalMongodbProperties.getDiscriminator());
        params.put("morphia.discriminator-key", globalMongodbProperties.getDiscriminatorKey());
        params.put("morphia.enable-polymorphic-queries", StrUtil.toString(globalMongodbProperties.isEnablePolymorphicQueries()));
        params.put("morphia.ignore-finals", StrUtil.toString(globalMongodbProperties.isIgnoreFinals()));
        params.put("morphia.packages", globalMongodbProperties.getMapPackage());
        params.put("morphia.property-discovery", globalMongodbProperties.getPropertyDiscovery());
        params.put("morphia.property-naming", globalMongodbProperties.getPropertyNaming());
        params.put("morphia.query-factory", globalMongodbProperties.getQueryFactory());
        params.put("morphia.store-empties", StrUtil.toString(globalMongodbProperties.isStoreEmpties()));
        params.put("morphia.store-nulls", StrUtil.toString(globalMongodbProperties.isStoreNulls()));
        params.put("morphia.uuid-representation", globalMongodbProperties.getUuidRepresentation());
        DefaultValuesConfigSource configValue = new DefaultValuesConfigSource(params, "default", 0);
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

}
