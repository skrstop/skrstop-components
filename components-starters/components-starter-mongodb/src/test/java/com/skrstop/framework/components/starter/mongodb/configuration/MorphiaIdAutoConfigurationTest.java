package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.DefaultIdentifierGenerator;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.IdentifierGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MorphiaIdAutoConfigurationTest {

    @Test
    public void withNullIdService() {
        MorphiaIdAutoConfiguration configuration = new MorphiaIdAutoConfiguration();
        IdentifierGenerator generator = configuration.identifierGenerator(new GlobalMongodbProperties(), null);
        Assert.assertTrue(generator instanceof DefaultIdentifierGenerator);
        Assert.assertNotNull(generator.nextId());
        Assert.assertNotNull(generator.nextUUID());
    }

    @Test
    public void withIdService() {
        MorphiaIdAutoConfiguration configuration = new MorphiaIdAutoConfiguration();
        IdService idService = Mockito.mock(IdService.class);
        Mockito.when(idService.getId()).thenReturn(123L);
        Mockito.when(idService.getUuidWithoutDash()).thenReturn("uuid");
        IdentifierGenerator generator = configuration.identifierGenerator(new GlobalMongodbProperties(), idService);
        Assert.assertEquals(123L, generator.nextId().longValue());
        Assert.assertEquals("uuid", generator.nextUUID());
    }
}
