package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.mongodb.configuration.generator.DefaultIdentifierGenerator;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.IdentifierGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MorphiaNoIdAutoConfigurationTest {

    @Test
    public void identifierGenerator() {
        MorphiaNoIdAutoConfiguration configuration = new MorphiaNoIdAutoConfiguration();
        IdentifierGenerator generator = configuration.identifierGenerator(new GlobalMongodbProperties());
        Assert.assertTrue(generator instanceof DefaultIdentifierGenerator);
        Assert.assertNotNull(generator.nextId());
        Assert.assertNotNull(generator.nextUUID());
    }
}
