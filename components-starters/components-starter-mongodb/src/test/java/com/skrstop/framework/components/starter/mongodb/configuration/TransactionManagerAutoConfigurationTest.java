package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

public class TransactionManagerAutoConfigurationTest {

    @Test
    public void mongoTransactionManager() {
        TransactionManagerAutoConfiguration configuration = new TransactionManagerAutoConfiguration();
        MongoTransactionManager manager =
                configuration.mongoTransactionManager(Mockito.mock(MongoDatabaseFactory.class));
        Assert.assertNotNull(manager);
    }

    @Test
    public void reactiveTransactionManager() {
        TransactionManagerAutoConfiguration configuration = new TransactionManagerAutoConfiguration();
        // byte-buddy 与该接口的 CodecRegistryProvider 兼容问题，改用 JDK 动态代理
        ReactiveMongoDatabaseFactory factory = (ReactiveMongoDatabaseFactory) java.lang.reflect.Proxy.newProxyInstance(
                ReactiveMongoDatabaseFactory.class.getClassLoader(),
                new Class<?>[]{ReactiveMongoDatabaseFactory.class},
                (proxy, method, args) -> null);
        ReactiveMongoTransactionManager manager =
                configuration.mongoReactiveTransactionManager(factory);
        Assert.assertNotNull(manager);
    }

    @Test
    public void constants() {
        Assert.assertNotNull(MongodbConst.TRANSACTION_NAME_MONGO);
        Assert.assertNotNull(MongodbConst.TRANSACTION_REACTIVE_NAME_MONGO);
    }
}
