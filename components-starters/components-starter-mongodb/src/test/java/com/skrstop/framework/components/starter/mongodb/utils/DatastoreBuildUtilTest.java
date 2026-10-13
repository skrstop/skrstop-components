package com.skrstop.framework.components.starter.mongodb.utils;

import com.mongodb.client.MongoClient;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.MorphiaAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

public class DatastoreBuildUtilTest {

    @Test
    public void buildWithDatabaseName() {
        GlobalMongodbProperties properties = new GlobalMongodbProperties();
        MongoProperties mongoProperties = new MongoProperties();
        mongoProperties.setDatabase("test_db");
        MongoClient mongoClient = Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS);
        dev.morphia.Datastore datastore = DatastoreBuildUtil.build(mongoClient, mongoProperties, properties);
        Assert.assertNotNull(datastore);
    }

    @Test
    public void buildWithUriWithoutQuery() {
        GlobalMongodbProperties properties = new GlobalMongodbProperties();
        MongoProperties mongoProperties = new MongoProperties();
        mongoProperties.setUri("mongodb://localhost:27017/uri_db");
        dev.morphia.Datastore datastore =
                DatastoreBuildUtil.build(Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS), mongoProperties, properties);
        Assert.assertNotNull(datastore);
    }

    @Test
    public void buildWithUriWithQuery() {
        GlobalMongodbProperties properties = new GlobalMongodbProperties();
        MongoProperties mongoProperties = new MongoProperties();
        mongoProperties.setUri("mongodb://localhost:27017/qs_db?authSource=admin");
        dev.morphia.Datastore datastore =
                DatastoreBuildUtil.build(Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS), mongoProperties, properties);
        Assert.assertNotNull(datastore);
    }

    @Test(expected = RuntimeException.class)
    public void buildWithoutDatabaseThrows() {
        GlobalMongodbProperties properties = new GlobalMongodbProperties();
        MongoProperties mongoProperties = new MongoProperties();
        DatastoreBuildUtil.build(Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS), mongoProperties, properties);
    }

    @Test
    public void morphiaAutoConfigurationBean() {
        MorphiaAutoConfiguration configuration = new MorphiaAutoConfiguration();
        MongoProperties mongoProperties = new MongoProperties();
        mongoProperties.setDatabase("bean_db");
        dev.morphia.Datastore datastore = configuration.datastore(
                Mockito.mock(MongoClient.class, Mockito.RETURNS_DEEP_STUBS), mongoProperties, new GlobalMongodbProperties());
        Assert.assertNotNull(datastore);
    }
}
