package com.skrstop.framework.components.starter.mongodb.configuration.generator;

import org.junit.Assert;
import org.junit.Test;

public class IdentifierGeneratorTest {

    @Test
    public void defaultGenerator() {
        DefaultIdentifierGenerator generator = new DefaultIdentifierGenerator();
        Assert.assertNotNull(generator.nextId());
        Assert.assertTrue(generator.nextId().longValue() > 0);
        Assert.assertNotNull(generator.nextUUID());
        Assert.assertEquals(32, generator.nextUUID().length());
    }
}
