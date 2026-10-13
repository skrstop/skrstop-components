package com.skrstop.framework.components.example.starters.simple;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.example.starters.simple.controller.ExampleDbController;
import com.skrstop.framework.components.example.starters.simple.entity.msyql.Example1;
import com.skrstop.framework.components.example.starters.simple.service.Example1MongoService;
import com.skrstop.framework.components.example.starters.simple.service.Example1MysqlService;
import com.skrstop.framework.components.example.starters.simple.service.Example2MongoService;
import com.skrstop.framework.components.starter.id.service.IdService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

public class DbServiceTest {

    @Test
    public void testDbController() {
        ExampleDbController controller = new ExampleDbController();
        Example1MysqlService service = Mockito.mock(Example1MysqlService.class);
        IdService idService = Mockito.mock(IdService.class);
        Mockito.when(idService.getId()).thenReturn(123L);
        Mockito.when(service.list()).thenReturn(List.of(new Example1()));
        Mockito.when(service.addDb0()).thenReturn(List.of(new Example1()));
        Mockito.when(service.addDb1()).thenReturn(List.of(new Example1()));
        Mockito.when(service.page(Mockito.any())).thenReturn(Mockito.mock(IPage.class));
        Mockito.when(service.listByKey(Mockito.anyString())).thenReturn(List.of(new Example1()));
        ReflectionTestUtils.setField(controller, "example1MysqlService", service);
        ReflectionTestUtils.setField(controller, "idService", idService);

        try (org.mockito.MockedStatic<com.skrstop.framework.components.util.value.data.RandomValueUtil> mocked =
                     org.mockito.Mockito.mockStatic(com.skrstop.framework.components.util.value.data.RandomValueUtil.class)) {
            // insert/update 各调用两次，覆盖 die/status 的 true/false 两侧
            mocked.when(() -> com.skrstop.framework.components.util.value.data.RandomValueUtil.getNum(1, 100))
                    .thenReturn(1, 1, 1, 100, 100, 100, 1, 1, 1, 100, 100, 100);
            mocked.when(() -> com.skrstop.framework.components.util.value.data.RandomValueUtil.getChineseName()).thenReturn("名字");
            controller.exampleMysqlInsert();
            controller.exampleMysqlInsert();
            controller.exampleMysqlUpdate();
            controller.exampleMysqlUpdate();
        }
        Assert.assertNotNull(controller.exampleMysqlQuery());
        Assert.assertNotNull(controller.exampleMysqlQuery(new com.skrstop.framework.components.starter.database.wrapper.PageQuery()));

        Map<String, List<Example1>> result = controller.exampleDynamic(false);
        Assert.assertEquals(2, result.size());
        try {
            controller.exampleDynamic(true);
            Assert.fail();
        } catch (SkrstopBusinessException expected) {
        }
        Assert.assertNotNull(controller.exampleDynamic2("db0"));

        Mockito.verify(service, Mockito.times(2)).save(Mockito.any(Example1.class));
        Mockito.verify(service, Mockito.times(2)).update(Mockito.any());
        Mockito.verify(service).page(Mockito.any());
        Mockito.verify(service).listByKey(Mockito.eq("db0"));
    }

    @Test
    public void testExample1MysqlService() {
        // 初始化 MybatisPlus TableInfo 与仓库的属性缓存（生产环境由 Spring 容器完成）
        org.apache.ibatis.builder.MapperBuilderAssistant assistant =
                new org.apache.ibatis.builder.MapperBuilderAssistant(new com.baomidou.mybatisplus.core.MybatisConfiguration(), "");
        com.baomidou.mybatisplus.core.metadata.TableInfoHelper.initTableInfo(assistant, Example1.class);
        Example1MysqlService service = new Example1MysqlService();
        com.skrstop.framework.components.example.starters.simple.mapper.mysql.Example1Mapper mapper =
                Mockito.mock(com.skrstop.framework.components.example.starters.simple.mapper.mysql.Example1Mapper.class);
        Mockito.when(mapper.selectList(Mockito.any())).thenReturn(List.of(new Example1()));
        Mockito.when(mapper.insert(Mockito.any(Example1.class))).thenReturn(1);
        ReflectionTestUtils.setField(service, "baseMapper", mapper);
        IdService idService = Mockito.mock(IdService.class);
        Mockito.when(idService.getId()).thenReturn(456L);
        ReflectionTestUtils.setField(service, "idService", idService);
        // 生产环境由 @PostConstruct init 构建；单测中直接注入空缓存，
        // 使 setCreateInfo 的属性名集合为空集合（实体无创建人/创建时间字段标记时行为一致）
        ReflectionTestUtils.setField(service, "propertyFieldCache", new java.util.HashMap<>());

        // 固定 RandomValueUtil 随机值，确定性地覆盖 die/status 的 true/false 两个分支
        try (org.mockito.MockedStatic<com.skrstop.framework.components.util.value.data.RandomValueUtil> mocked =
                     org.mockito.Mockito.mockStatic(com.skrstop.framework.components.util.value.data.RandomValueUtil.class)) {
            // addDb0/addDb1 各调用两次，覆盖 die/status 的 true/false 两侧
            mocked.when(() -> com.skrstop.framework.components.util.value.data.RandomValueUtil.getNum(1, 100))
                    .thenReturn(1, 1, 1, 100, 100, 100, 1, 1, 1, 100, 100, 100);
            mocked.when(() -> com.skrstop.framework.components.util.value.data.RandomValueUtil.getChineseName()).thenReturn("名字");
            Assert.assertNotNull(service.addDb0());
            Assert.assertNotNull(service.addDb0());
            Assert.assertNotNull(service.addDb1());
            Assert.assertNotNull(service.addDb1());
        }
        Assert.assertNotNull(service.listByKey("db0"));
    }

    @Test
    public void testMongoServices() {
        Example1MongoService mongoService = Mockito.spy(new Example1MongoService());
        Mockito.doReturn(null).when(mongoService).save(Mockito.any(com.skrstop.framework.components.example.starters.simple.entity.mongo.Example1Mongo.class));
        mongoService.saveMaster("id1");
        mongoService.saveSlave("id2");
        Mockito.verify(mongoService, Mockito.times(2))
                .save(Mockito.any(com.skrstop.framework.components.example.starters.simple.entity.mongo.Example1Mongo.class));

        Example2MongoService mongoService2 = Mockito.spy(new Example2MongoService());
        Mockito.doReturn(null).when(mongoService2).save(Mockito.any(com.skrstop.framework.components.example.starters.simple.entity.mongo.Example2Mongo.class));
        mongoService2.saveMaster(1L);
        mongoService2.saveSlave(2L);
        Mockito.verify(mongoService2, Mockito.times(2))
                .save(Mockito.any(com.skrstop.framework.components.example.starters.simple.entity.mongo.Example2Mongo.class));
    }
}
