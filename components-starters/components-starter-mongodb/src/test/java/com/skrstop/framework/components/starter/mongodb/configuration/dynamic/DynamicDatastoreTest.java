package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.MongoClient;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import dev.morphia.DeleteOptions;
import dev.morphia.InsertManyOptions;
import dev.morphia.InsertOneOptions;
import dev.morphia.ReplaceOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.transactions.MorphiaTransaction;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.mock.env.MockEnvironment;

import java.util.Collections;

public class DynamicDatastoreTest {

    private DynamicMongoClient dynamicMongoClientOf() {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        MongoClient mock = Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS);
        client.getDynamicMongoClientMap().put("master", mock);
        return client;
    }

    @SuppressWarnings("unchecked")
    private static ObjectProvider<org.springframework.boot.ssl.SslBundles> emptyProvider() {
        ObjectProvider<org.springframework.boot.ssl.SslBundles> provider = Mockito.mock(ObjectProvider.class);
        Mockito.when(provider.getIfAvailable()).thenReturn(null);
        return provider;
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsPlainClient() {
        new DynamicDatastore(Mockito.mock(MongoClient.class), new DynamicMongodbProperties(), new GlobalMongodbProperties());
    }

    @Test(expected = RuntimeException.class)
    public void constructorMissingClient() {
        // 数据源在 client 构造之后才加入，client map 中缺失对应 client
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        properties.getDataSources().put("master", new MongoProperties());
        new DynamicDatastore(client, properties, new GlobalMongodbProperties());
    }

    @Test
    public void constructorBuildsDatastores() {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        // 数据源在 client 构造之后加入，避免创建真实 MongoClient
        MongoProperties dsProperties = new MongoProperties();
        dsProperties.setDatabase("ds_db");
        properties.getDataSources().put("master", dsProperties);
        MongoClient mock = Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS);
        client.getDynamicMongoClientMap().put("master", mock);
        DynamicDatastore datastore = new DynamicDatastore(client, properties, new GlobalMongodbProperties());
        Assert.assertNotNull(datastore.getDatastore());
        Assert.assertNotNull(datastore.getDynamicMongoDatastoreMap());
        DynamicMongoContextHolder.clear();
    }

    @Test(expected = RuntimeException.class)
    public void getDatastoreUnknown() {
        DynamicDatastore datastore = new DynamicDatastore(dynamicMongoClientOf(), new DynamicMongodbProperties(), new GlobalMongodbProperties());
        DynamicMongoContextHolder.push("unknown");
        try {
            datastore.getDatastore();
        } finally {
            DynamicMongoContextHolder.clear();
        }
    }

    /**
     * 覆盖全部委托方法
     */
    @Test
    public void delegates() {
        DynamicDatastore datastore = new DynamicDatastore(dynamicMongoClientOf(), new DynamicMongodbProperties(), new GlobalMongodbProperties());
        dev.morphia.Datastore mock = Mockito.mock(dev.morphia.Datastore.class, Mockito.RETURNS_DEEP_STUBS);
        datastore.getDynamicMongoDatastoreMap().put("master", mock);

        Assert.assertNotNull(datastore.aggregate("coll"));
        Assert.assertNotNull(datastore.aggregate(Document.class));
        Assert.assertNotNull(datastore.createAggregation(Document.class));
        Assert.assertNotNull(datastore.delete(new Document()));
        Assert.assertNotNull(datastore.delete(new Document(), Mockito.mock(DeleteOptions.class)));
        datastore.enableDocumentValidation();
        datastore.ensureCaps();
        datastore.ensureIndexes();
        Assert.assertNotNull(datastore.find(Document.class));
        Assert.assertNotNull(datastore.find(Document.class, Mockito.mock(FindOptions.class)));
        Assert.assertNotNull(datastore.find(Document.class, new Document()));
        Assert.assertNotNull(datastore.find("coll", Document.class));
        Assert.assertNotNull(datastore.find("coll"));
        Assert.assertNotNull(datastore.getCodecRegistry());
        Assert.assertNotNull(datastore.getCollection(Document.class));
        Assert.assertNotNull(datastore.getDatabase());
        // getLoggedQuery 返回 String（final 类型，deep stub 返回 null），仅调用覆盖
        datastore.getLoggedQuery(Mockito.mock(FindOptions.class));
        Assert.assertNotNull(datastore.getMapper());
        datastore.insert(new Document(), Mockito.mock(InsertOneOptions.class));
        datastore.insert(Collections.singletonList(new Document()), Mockito.mock(InsertManyOptions.class));
        // 泛型 T 返回值 deep stub 返回 null，仅调用覆盖
        datastore.merge(new Document());
        datastore.merge(new Document(), Mockito.mock(InsertOneOptions.class));
        Assert.assertNotNull(datastore.queryByExample(new Document()));
        datastore.refresh(new Document());
        // 泛型 List<T> 返回值 deep stub 返回 null，仅调用覆盖
        datastore.replace(new Document(), Mockito.mock(ReplaceOptions.class));
        datastore.replace(Collections.singletonList(new Document()), Mockito.mock(ReplaceOptions.class));
        datastore.save(Collections.singletonList(new Document()), Mockito.mock(InsertManyOptions.class));
        datastore.save(new Document(), Mockito.mock(InsertOneOptions.class));
        datastore.shardCollections();
        Assert.assertNotNull(datastore.startSession());
        Assert.assertNotNull(datastore.startSession(Mockito.mock(ClientSessionOptions.class)));
        // 泛型 T 返回值，仅调用覆盖
        datastore.withTransaction(Mockito.mock(MorphiaTransaction.class));
        datastore.withTransaction(Mockito.mock(ClientSessionOptions.class), Mockito.mock(MorphiaTransaction.class));

        Mockito.verify(mock, Mockito.atLeastOnce()).enableDocumentValidation();
        Mockito.verify(mock, Mockito.atLeastOnce()).ensureCaps();
        Mockito.verify(mock, Mockito.atLeastOnce()).ensureIndexes();
        Mockito.verify(mock, Mockito.atLeastOnce()).shardCollections();
    }
}
