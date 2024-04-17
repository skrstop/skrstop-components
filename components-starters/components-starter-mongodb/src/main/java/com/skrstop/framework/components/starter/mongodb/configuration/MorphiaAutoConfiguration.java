package com.skrstop.framework.components.starter.mongodb.configuration;

import com.mongodb.client.MongoClient;
import com.skrstop.framework.components.starter.mongodb.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.mongodb.utils.DatastoreBuildUtil;
import dev.morphia.Datastore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-10-17 10:57:45
 */
@Configuration
@EnableConfigurationProperties(GlobalMongodbProperties.class)
@ConditionalOnProperty(prefix = GlobalConfigConst.MONGODB_DYNAMIC, name = "enabled", havingValue = "false", matchIfMissing = true)
public class MorphiaAutoConfiguration {

    @Bean
    public Datastore datastore(MongoClient mongoClient
            , MongoProperties mongoProperties
            , GlobalMongodbProperties globalMongodbProperties) {
        return DatastoreBuildUtil.build(mongoClient, mongoProperties, globalMongodbProperties);
    }

}
