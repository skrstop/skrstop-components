package com.skrstop.framework.components.starter.id;

import com.skrstop.framework.components.starter.id.configuration.GlobalIdProperties;
import com.skrstop.framework.components.starter.id.configuration.IdServiceAutoConfiguration;
import com.skrstop.framework.components.starter.id.constant.GenerateTypeConst;
import com.skrstop.framework.components.starter.id.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.starter.id.service.strategy.LocalIdServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * id starter 单元测试
 */
public class IdServiceTest {

    @Test
    public void testGlobalIdProperties() {
        GlobalIdProperties properties = new GlobalIdProperties();
        Assert.assertTrue(properties.isEnable());
        Assert.assertEquals(GenerateTypeConst.LOCAL, properties.getGenerateType());
        Assert.assertEquals(1L, properties.getWorkerId());
        Assert.assertEquals(1L, properties.getDataCenterId());
        properties.setEnable(false);
        properties.setGenerateType(GenerateTypeConst.UNIFORM_DISTRIBUTION);
        properties.setWorkerId(2L);
        properties.setDataCenterId(3L);
        Assert.assertFalse(properties.isEnable());
        Assert.assertEquals(GenerateTypeConst.UNIFORM_DISTRIBUTION, properties.getGenerateType());
        Assert.assertEquals(2L, properties.getWorkerId());
        Assert.assertEquals(3L, properties.getDataCenterId());
        Assert.assertEquals("skrstop.id.config", GlobalConfigConst.ID_PREFIX);
    }

    @Test
    public void testGenerateTypeConst() {
        Assert.assertEquals(1, GenerateTypeConst.LOCAL);
        Assert.assertEquals(2, GenerateTypeConst.UNIFORM_DISTRIBUTION);
    }

    @Test
    public void testIdServiceAutoConfiguration() {
        IdServiceAutoConfiguration configuration = new IdServiceAutoConfiguration();
        IdService service = configuration.idGenerationService(new GlobalIdProperties());
        Assert.assertTrue(service instanceof LocalIdServiceImpl);
    }

    @Test
    public void testIdServiceAutoConfigurationNonLocal() {
        IdServiceAutoConfiguration configuration = new IdServiceAutoConfiguration();
        GlobalIdProperties properties = new GlobalIdProperties();
        properties.setGenerateType(GenerateTypeConst.UNIFORM_DISTRIBUTION);
        Assert.assertTrue(configuration.idGenerationService(properties) instanceof LocalIdServiceImpl);
    }

    @Test
    public void testLocalIdServiceImpl() {
        GlobalIdProperties properties = new GlobalIdProperties();
        properties.setWorkerId(1L);
        properties.setDataCenterId(1L);
        LocalIdServiceImpl service = new LocalIdServiceImpl(properties);
        long id1 = service.getId();
        long id2 = service.getId();
        Assert.assertTrue(id1 > 0);
        Assert.assertTrue(id2 > id1);
    }

    @Test
    public void testIdServiceDefaultMethods() {
        IdService service = new LocalIdServiceImpl(new GlobalIdProperties());
        Assert.assertEquals(36, service.getUuid().length());
        Assert.assertEquals(32, service.getUuidWithoutDash().length());
        Assert.assertEquals(24, service.getObjectId().length());
        Assert.assertEquals(21, service.getNanoId().length());
        Assert.assertEquals(8, service.getNanoId(8).length());
    }
}
