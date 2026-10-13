package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import org.junit.Assert;
import org.junit.Test;

public class GlobalMongodbPropertiesTest {

    @Test
    public void defaultsAndSetters() {
        GlobalMongodbProperties p = new GlobalMongodbProperties();
        Assert.assertNull(p.getMapPackage());
        Assert.assertFalse(p.isApplyCaps());
        Assert.assertFalse(p.isApplyDocumentValidations());
        Assert.assertFalse(p.isApplyIndexes());
        Assert.assertFalse(p.isAutoImportModels());
        Assert.assertEquals("", p.getCodecProvider());
        Assert.assertEquals(MongodbConst.NAME_RULE_IDENTITY, p.getCollectionNaming());
        Assert.assertEquals(MongodbConst.DATE_STORAGE_SYSTEM_DEFAULT, p.getDateStorage());
        Assert.assertEquals(MongodbConst.DISCRIMINATOR_RULE_CLASS_NAME, p.getDiscriminator());
        Assert.assertEquals("_class", p.getDiscriminatorKey());
        Assert.assertFalse(p.isEnablePolymorphicQueries());
        Assert.assertFalse(p.isIgnoreFinals());
        Assert.assertEquals(MongodbConst.PROPERTY_DISCOVERY_RULE_FIELDS, p.getPropertyDiscovery());
        Assert.assertEquals(MongodbConst.NAME_RULE_IDENTITY, p.getPropertyNaming());
        Assert.assertEquals("dev.morphia.query.DefaultQueryFactory", p.getQueryFactory());
        Assert.assertTrue(p.isStoreEmpties());
        Assert.assertTrue(p.isStoreNulls());
        Assert.assertEquals(MongodbConst.UUID_JAVA_LEGACY, p.getUuidRepresentation());

        p.setMapPackage("com.x");
        p.setApplyCaps(true);
        p.setApplyDocumentValidations(true);
        p.setApplyIndexes(true);
        p.setAutoImportModels(true);
        p.setCodecProvider("cp");
        p.setCollectionNaming(MongodbConst.NAME_RULE_SNAKE_CASE);
        p.setDateStorage(MongodbConst.DATE_STORAGE_UTC);
        p.setDiscriminator(MongodbConst.DISCRIMINATOR_RULE_SIMPLE_NAME);
        p.setDiscriminatorKey("k");
        p.setEnablePolymorphicQueries(true);
        p.setIgnoreFinals(true);
        p.setPropertyDiscovery(MongodbConst.PROPERTY_DISCOVERY_RULE_METHODS);
        p.setPropertyNaming(MongodbConst.NAME_RULE_SNAKE_CASE);
        p.setQueryFactory("qf");
        p.setStoreEmpties(false);
        p.setStoreNulls(false);
        p.setUuidRepresentation(MongodbConst.UUID_STANDARD);
        Assert.assertEquals("com.x", p.getMapPackage());
        Assert.assertTrue(p.isApplyCaps());
        Assert.assertTrue(p.isApplyDocumentValidations());
        Assert.assertTrue(p.isApplyIndexes());
        Assert.assertTrue(p.isAutoImportModels());
        Assert.assertEquals("cp", p.getCodecProvider());
        Assert.assertEquals(MongodbConst.NAME_RULE_SNAKE_CASE, p.getCollectionNaming());
        Assert.assertEquals(MongodbConst.DATE_STORAGE_UTC, p.getDateStorage());
        Assert.assertEquals(MongodbConst.DISCRIMINATOR_RULE_SIMPLE_NAME, p.getDiscriminator());
        Assert.assertEquals("k", p.getDiscriminatorKey());
        Assert.assertTrue(p.isEnablePolymorphicQueries());
        Assert.assertTrue(p.isIgnoreFinals());
        Assert.assertEquals(MongodbConst.PROPERTY_DISCOVERY_RULE_METHODS, p.getPropertyDiscovery());
        Assert.assertEquals(MongodbConst.NAME_RULE_SNAKE_CASE, p.getPropertyNaming());
        Assert.assertEquals("qf", p.getQueryFactory());
        Assert.assertFalse(p.isStoreEmpties());
        Assert.assertFalse(p.isStoreNulls());
        Assert.assertEquals(MongodbConst.UUID_STANDARD, p.getUuidRepresentation());
    }
}
