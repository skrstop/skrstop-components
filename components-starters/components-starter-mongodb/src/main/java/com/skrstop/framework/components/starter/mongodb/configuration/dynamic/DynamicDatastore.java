package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.utils.DatastoreBuildUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import dev.morphia.*;
import dev.morphia.aggregation.Aggregation;
import dev.morphia.aggregation.AggregationPipeline;
import dev.morphia.mapping.Mapper;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.transactions.MorphiaSession;
import dev.morphia.transactions.MorphiaTransaction;
import lombok.Getter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2024-04-17 14:54:05
 * @since 1.0.0
 */
public class DynamicDatastore implements Datastore {

    @Getter
    private final ConcurrentHashMap<String, Datastore> dynamicMongoDatastoreMap = new ConcurrentHashMap<>();
    private final DynamicMongodbProperties dynamicMongodbProperties;
    private final DynamicMongoClient dynamicMongoClient;
    private final GlobalMongodbProperties globalMongodbProperties;

    public DynamicDatastore(MongoClient mongoClient, DynamicMongodbProperties dynamicMongodbProperties, GlobalMongodbProperties globalMongodbProperties) {
        if (!(mongoClient instanceof DynamicMongoClient)) {
            throw new IllegalArgumentException("mongoClient must be instance of DynamicMongoClient");
        }
        this.dynamicMongoClient = (DynamicMongoClient) mongoClient;
        this.dynamicMongodbProperties = dynamicMongodbProperties;
        this.globalMongodbProperties = globalMongodbProperties;
        // 初始化

        dynamicMongodbProperties.getDataSources().forEach((key, value) -> {
            MongoClient datasourceMongoClient = dynamicMongoClient.getDynamicMongoClientMap().get(key);
            if (ObjectUtil.isNull(datasourceMongoClient)) {
                throw new RuntimeException("未找到mongo client，请检查是否配置了mongodb数据源: " + key);
            }
            dynamicMongoDatastoreMap.put(key, DatastoreBuildUtil.build(datasourceMongoClient, value, globalMongodbProperties));
        });
    }

    public Datastore getDatastore() {
        String peek = DynamicMongoContextHolder.peek();
        if (StrUtil.isBlank(peek)) {
            peek = dynamicMongodbProperties.getPrimary();
        }
        Datastore datastore = dynamicMongoDatastoreMap.get(peek);
        if (ObjectUtil.isNull(datastore)) {
            throw new RuntimeException("未找到mongodb数据源，请检查是否配置了mongodb数据源: " + peek);
        }
        return datastore;
    }

    @Override
    public Aggregation<Document> aggregate(String source) {
        return this.getDatastore().aggregate(source);
    }

    @Override
    public <T> Aggregation<T> aggregate(Class<T> source) {
        return this.getDatastore().aggregate(source);
    }

    @Override
    public AggregationPipeline createAggregation(Class<?> source) {
        return this.getDatastore().createAggregation(source);
    }

    @Override
    public <T> DeleteResult delete(T entity) {
        return this.getDatastore().delete(entity);
    }

    @Override
    public <T> DeleteResult delete(T entity, DeleteOptions options) {
        return this.getDatastore().delete(entity, options);
    }

    @Override
    public void enableDocumentValidation() {
        this.getDatastore().enableDocumentValidation();
    }

    @Override
    public void ensureCaps() {
        this.getDatastore().ensureCaps();
    }

    @Override
    public void ensureIndexes() {
        this.getDatastore().ensureIndexes();
    }

    @Override
    public <T> Query<T> find(Class<T> type) {
        return this.getDatastore().find(type);
    }

    @Override
    public <T> Query<T> find(Class<T> type, FindOptions options) {
        return this.getDatastore().find(type, options);
    }

    @Override
    public <T> Query<T> find(Class<T> type, Document nativeQuery) {
        return this.getDatastore().find(type, nativeQuery);
    }

    @Override
    public <T> Query<T> find(String collection, Class<T> type) {
        return this.getDatastore().find(collection, type);
    }

    @Override
    public <T> Query<T> find(String collection) {
        return this.getDatastore().find(collection);
    }

    @Override
    public CodecRegistry getCodecRegistry() {
        return this.getDatastore().getCodecRegistry();
    }

    @Override
    public <T> MongoCollection<T> getCollection(Class<T> type) {
        return this.getDatastore().getCollection(type);
    }

    @Override
    public MongoDatabase getDatabase() {
        return this.getDatastore().getDatabase();
    }

    @Override
    public String getLoggedQuery(FindOptions options) {
        return this.getDatastore().getLoggedQuery(options);
    }

    @Override
    public Mapper getMapper() {
        return this.getDatastore().getMapper();
    }

    @Override
    public <T> void insert(T entity, InsertOneOptions options) {
        this.getDatastore().insert(entity, options);
    }

    @Override
    public <T> void insert(List<T> entities, InsertManyOptions options) {
        this.getDatastore().insert(entities, options);
    }

    @Override
    public <T> T merge(T entity) {
        return this.getDatastore().merge(entity);
    }

    @Override
    public <T> T merge(T entity, InsertOneOptions options) {
        return this.getDatastore().merge(entity, options);
    }

    @Override
    public <T> Query<T> queryByExample(T example) {
        return this.getDatastore().queryByExample(example);
    }

    @Override
    public <T> void refresh(T entity) {
        this.getDatastore().refresh(entity);
    }

    @Override
    public <T> T replace(T entity, ReplaceOptions options) {
        return this.getDatastore().replace(entity, options);
    }

    @Override
    public <T> List<T> replace(List<T> entities, ReplaceOptions options) {
        return this.getDatastore().replace(entities, options);
    }

    @Override
    public <T> List<T> save(List<T> entities, InsertManyOptions options) {
        return this.getDatastore().save(entities, options);
    }

    @Override
    public <T> T save(T entity, InsertOneOptions options) {
        return this.getDatastore().save(entity, options);
    }

    @Override
    public void shardCollections() {
        this.getDatastore().shardCollections();
    }

    @Override
    public MorphiaSession startSession() {
        return this.getDatastore().startSession();
    }

    @Override
    public MorphiaSession startSession(ClientSessionOptions options) {
        return this.getDatastore().startSession(options);
    }

    @Override
    public <T> T withTransaction(MorphiaTransaction<T> transaction) {
        return this.getDatastore().withTransaction(transaction);
    }

    @Override
    public <T> T withTransaction(ClientSessionOptions options, MorphiaTransaction<T> transaction) {
        return this.getDatastore().withTransaction(options, transaction);
    }
}
