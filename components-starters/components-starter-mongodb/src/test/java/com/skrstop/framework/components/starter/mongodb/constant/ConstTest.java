package com.skrstop.framework.components.starter.mongodb.constant;

import org.junit.Assert;
import org.junit.Test;

public class ConstTest {

    @Test
    public void globalConfigConst() {
        Assert.assertEquals("skrstop.mongodb.config", GlobalConfigConst.MONGODB_PREFIX);
        Assert.assertEquals("skrstop.mongodb.dynamic", GlobalConfigConst.MONGODB_DYNAMIC);
        Assert.assertEquals("default", GlobalConfigConst.DS_DEFAULT);
        Assert.assertEquals("master", GlobalConfigConst.DS_MASTER);
        Assert.assertEquals("slave", GlobalConfigConst.DS_SLAVE);
    }

    @Test
    public void mongodbConst() {
        Assert.assertEquals("DefaultMongoTransactionManager", MongodbConst.TRANSACTION_NAME_MONGO);
        Assert.assertEquals("DefaultReactiveMongoTransactionManager", MongodbConst.TRANSACTION_REACTIVE_NAME_MONGO);
        Assert.assertEquals("_id", MongodbConst.DEFAULT_ID_NAME);
        Assert.assertEquals("camelCase", MongodbConst.NAME_RULE_CAMEL_CASE);
        Assert.assertEquals("identity", MongodbConst.NAME_RULE_IDENTITY);
        Assert.assertEquals("kebabCase", MongodbConst.NAME_RULE_KEBAB_CASE);
        Assert.assertEquals("lowerCase", MongodbConst.NAME_RULE_LOWER_CASE);
        Assert.assertEquals("snakeCase", MongodbConst.NAME_RULE_SNAKE_CASE);
        Assert.assertEquals("fqcn", MongodbConst.NAME_RULE_FQCN);
        Assert.assertEquals("className", MongodbConst.DISCRIMINATOR_RULE_CLASS_NAME);
        Assert.assertEquals("lowerClassName", MongodbConst.DISCRIMINATOR_RULE_LOWER_CLASS_NAME);
        Assert.assertEquals("lowerSimpleName", MongodbConst.DISCRIMINATOR_RULE_LOWER_SIMPLE_NAME);
        Assert.assertEquals("simpleName", MongodbConst.DISCRIMINATOR_RULE_SIMPLE_NAME);
        Assert.assertEquals("fqcn", MongodbConst.DISCRIMINATOR_RULE_FQCN);
        Assert.assertEquals("fields", MongodbConst.PROPERTY_DISCOVERY_RULE_FIELDS);
        Assert.assertEquals("methods", MongodbConst.PROPERTY_DISCOVERY_RULE_METHODS);
        Assert.assertEquals("unspecified", MongodbConst.UUID_UNSPECIFIED);
        Assert.assertEquals("standard", MongodbConst.UUID_STANDARD);
        Assert.assertEquals("c_sharp_legacy", MongodbConst.UUID_C_SHARP_LEGACY);
        Assert.assertEquals("java_legacy", MongodbConst.UUID_JAVA_LEGACY);
        Assert.assertEquals("python_legacy", MongodbConst.UUID_PYTHON_LEGACY);
        Assert.assertEquals("utc", MongodbConst.DATE_STORAGE_UTC);
        Assert.assertEquals("system_default", MongodbConst.DATE_STORAGE_SYSTEM_DEFAULT);
    }
}
