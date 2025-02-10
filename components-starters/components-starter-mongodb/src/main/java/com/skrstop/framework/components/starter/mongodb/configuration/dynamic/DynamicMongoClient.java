package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.*;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2024-04-17 14:53:04
 * @since 1.0.0
 */
@Slf4j
public class DynamicMongoClient implements MongoClient {

    @Getter
    private final ConcurrentHashMap<String, MongoClient> dynamicMongoClientMap = new ConcurrentHashMap<>();
    private final DynamicMongodbProperties dynamicMongodbProperties;

    public DynamicMongoClient(DynamicMongodbProperties dynamicMongodbProperties, Environment environment, ObjectProvider<SslBundles> sslBundles) {
        this.dynamicMongodbProperties = dynamicMongodbProperties;
        Map<String, MongoProperties> dataSources = dynamicMongodbProperties.getDataSources();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().build();
        dataSources.forEach((key, value) -> {
            log.info("初始化mongo数据源：{}", key);
            PropertiesMongoConnectionDetails propertiesMongoConnectionDetails = new PropertiesMongoConnectionDetails(value);
            StandardMongoClientSettingsBuilderCustomizer standardMongoClientSettingsBuilderCustomizer = new StandardMongoClientSettingsBuilderCustomizer(propertiesMongoConnectionDetails.getConnectionString(),
                    value.getUuidRepresentation(), value.getSsl(), sslBundles.getIfAvailable());
            List<MongoClientSettingsBuilderCustomizer> builderCustomizers = CollectionUtil.newArrayList(standardMongoClientSettingsBuilderCustomizer);
            MongoClient mongoClient = new MongoClientFactory(builderCustomizers).createMongoClient(mongoClientSettings);
            dynamicMongoClientMap.put(key, mongoClient);
        });
    }

    public MongoClient getMongoClient() {
        String peek = DynamicMongoContextHolder.peek();
        if (StrUtil.isBlank(peek)) {
            peek = dynamicMongodbProperties.getPrimary();
        }
        MongoClient mongoClient = dynamicMongoClientMap.get(peek);
        if (ObjectUtil.isNull(mongoClient)) {
            throw new RuntimeException("未找到mongodb数据源，请检查是否配置了mongodb数据源: " + peek);
        }
        return mongoClient;
    }

    @Override
    public MongoDatabase getDatabase(String databaseName) {
        return this.getMongoClient().getDatabase(databaseName);
    }

    @Override
    public ClientSession startSession() {
        return this.getMongoClient().startSession();
    }

    @Override
    public ClientSession startSession(ClientSessionOptions options) {
        return this.getMongoClient().startSession(options);
    }

    @Override
    public void close() {
        dynamicMongoClientMap.forEach((key, value) -> {
            value.close();
        });
    }

    @Override
    public MongoIterable<String> listDatabaseNames() {
        return this.getMongoClient().listDatabaseNames();
    }

    @Override
    public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
        return this.getMongoClient().listDatabaseNames(clientSession);
    }

    @Override
    public ListDatabasesIterable<Document> listDatabases() {
        return this.getMongoClient().listDatabases();
    }

    @Override
    public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
        return this.getMongoClient().listDatabases(clientSession);
    }

    @Override
    public <TResult> ListDatabasesIterable<TResult> listDatabases(Class<TResult> tResultClass) {
        return this.getMongoClient().listDatabases(tResultClass);
    }

    @Override
    public <TResult> ListDatabasesIterable<TResult> listDatabases(ClientSession clientSession, Class<TResult> tResultClass) {
        return this.getMongoClient().listDatabases(clientSession, tResultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch() {
        return this.getMongoClient().watch();
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> tResultClass) {
        return this.getMongoClient().watch(tResultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
        return this.getMongoClient().watch(pipeline);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> tResultClass) {
        return this.getMongoClient().watch(pipeline, tResultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
        return this.getMongoClient().watch(clientSession);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> tResultClass) {
        return this.getMongoClient().watch(clientSession, tResultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.getMongoClient().watch(clientSession, pipeline);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> tResultClass) {
        return this.getMongoClient().watch(clientSession, pipeline, tResultClass);
    }

    @Override
    public ClusterDescription getClusterDescription() {
        return this.getMongoClient().getClusterDescription();
    }
}
