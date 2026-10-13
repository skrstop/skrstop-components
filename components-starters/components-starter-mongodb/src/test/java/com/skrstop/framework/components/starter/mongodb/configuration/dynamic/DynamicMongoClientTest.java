package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.bulk.ClientBulkWriteOptions;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class DynamicMongoClientTest {

    @SuppressWarnings("unchecked")
    private static ObjectProvider<org.springframework.boot.ssl.SslBundles> emptyProvider() {
        ObjectProvider<org.springframework.boot.ssl.SslBundles> provider = Mockito.mock(ObjectProvider.class);
        Mockito.when(provider.getIfAvailable()).thenReturn(null);
        return provider;
    }

    @Test
    public void constructorWithEmptyDataSources() {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        Environment environment = new MockEnvironment();
        DynamicMongoClient client = new DynamicMongoClient(properties, environment, emptyProvider());
        Assert.assertTrue(client.getDynamicMongoClientMap().isEmpty());
        client.close();
    }

    @Test
    public void constructorWithDataSource() {
        // 数据源指向不可达地址（仅创建客户端实例，不进行任何业务连接）
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        org.springframework.boot.autoconfigure.mongo.MongoProperties ds = new org.springframework.boot.autoconfigure.mongo.MongoProperties();
        ds.setHost("127.0.0.1");
        ds.setPort(1);
        properties.getDataSources().put("master", ds);
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        Assert.assertEquals(1, client.getDynamicMongoClientMap().size());
        client.close();
    }

    @Test
    public void getMongoClientDefaultsToPrimary() {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        MongoClient mock = Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS);
        client.getDynamicMongoClientMap().put("master", mock);
        Assert.assertSame(mock, client.getMongoClient());
    }

    @Test
    public void getMongoClientFromContext() {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        MongoClient mock = Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS);
        client.getDynamicMongoClientMap().put("slave", mock);
        DynamicMongoContextHolder.push("slave");
        try {
            Assert.assertSame(mock, client.getMongoClient());
        } finally {
            DynamicMongoContextHolder.clear();
        }
    }

    @Test(expected = RuntimeException.class)
    public void getMongoClientUnknownSource() {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        DynamicMongoContextHolder.push("unknown");
        try {
            client.getMongoClient();
        } finally {
            DynamicMongoContextHolder.clear();
        }
    }

    /**
     * 覆盖全部委托方法
     */
    @Test
    public void delegates() throws Exception {
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        DynamicMongoClient client = new DynamicMongoClient(properties, new MockEnvironment(), emptyProvider());
        MongoClient mock = Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS);
        client.getDynamicMongoClientMap().put("master", mock);

        Assert.assertNotNull(client.getCodecRegistry());
        Assert.assertNotNull(client.getReadPreference());
        Assert.assertNotNull(client.getWriteConcern());
        Assert.assertNotNull(client.getReadConcern());
        Assert.assertNotNull(client.getTimeout(TimeUnit.MILLISECONDS));
        Assert.assertNotNull(client.withCodecRegistry(Mockito.mock(CodecRegistry.class)));
        Assert.assertNotNull(client.withReadPreference(com.mongodb.ReadPreference.primary()));
        Assert.assertNotNull(client.withWriteConcern(com.mongodb.WriteConcern.ACKNOWLEDGED));
        Assert.assertNotNull(client.withReadConcern(com.mongodb.ReadConcern.DEFAULT));
        Assert.assertNotNull(client.withTimeout(1L, TimeUnit.MILLISECONDS));
        Assert.assertNotNull(client.getDatabase("db"));
        Assert.assertNotNull(client.startSession());
        Assert.assertNotNull(client.startSession(Mockito.mock(ClientSessionOptions.class)));
        Assert.assertNotNull(client.listDatabaseNames());
        Assert.assertNotNull(client.listDatabaseNames(null));
        Assert.assertNotNull(client.listDatabases());
        Assert.assertNotNull(client.listDatabases((com.mongodb.client.ClientSession) null));
        Assert.assertNotNull(client.listDatabases(org.bson.Document.class));
        Assert.assertNotNull(client.listDatabases(null, org.bson.Document.class));
        Assert.assertNotNull(client.watch());
        Assert.assertNotNull(client.watch(org.bson.Document.class));
        Assert.assertNotNull(client.watch(Collections.emptyList()));
        Assert.assertNotNull(client.watch(Collections.emptyList(), org.bson.Document.class));
        Assert.assertNotNull(client.watch((com.mongodb.client.ClientSession) null));
        Assert.assertNotNull(client.watch((com.mongodb.client.ClientSession) null, org.bson.Document.class));
        Assert.assertNotNull(client.watch((com.mongodb.client.ClientSession) null, Collections.emptyList()));
        Assert.assertNotNull(client.watch((com.mongodb.client.ClientSession) null, Collections.emptyList(), org.bson.Document.class));
        Assert.assertNotNull(client.bulkWrite(Collections.emptyList()));
        Assert.assertNotNull(client.bulkWrite(Collections.emptyList(), Mockito.mock(ClientBulkWriteOptions.class)));
        Assert.assertNotNull(client.bulkWrite(null, Collections.emptyList()));
        Assert.assertNotNull(client.bulkWrite(null, Collections.emptyList(), Mockito.mock(ClientBulkWriteOptions.class)));
        Assert.assertNotNull(client.getClusterDescription());
        client.appendMetadata(Mockito.mock(com.mongodb.MongoDriverInformation.class));
        client.close();

        Mockito.verify(mock, Mockito.atLeastOnce()).close();
        Mockito.verify(mock, Mockito.atLeastOnce()).getCodecRegistry();
        Mockito.verify(mock, Mockito.atLeastOnce()).getClusterDescription();
        Mockito.verify(mock, Mockito.atLeastOnce()).appendMetadata(Mockito.any());
    }
}
