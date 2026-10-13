package com.skrstop.framework.components.example.starters.simple;

import com.skrstop.framework.components.example.starters.simple.controller.ExampleMongodbController;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example1Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example2Mongo;
import com.skrstop.framework.components.example.starters.simple.service.Example1MongoService;
import com.skrstop.framework.components.example.starters.simple.service.Example2MongoService;
import dev.morphia.query.Query;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class MongodbControllerTest {

    @Test
    public void testMongodbController() {
        ExampleMongodbController controller = new ExampleMongodbController();
        Example1MongoService service1 = Mockito.mock(Example1MongoService.class);
        Example2MongoService service2 = Mockito.mock(Example2MongoService.class);
        ReflectionTestUtils.setField(controller, "example1MongoService", service1);
        ReflectionTestUtils.setField(controller, "example2MongoService", service2);

        controller.exampleMongodb1();
        Mockito.verify(service1).save(Mockito.any(Example1Mongo.class));
        Mockito.verify(service2).save(Mockito.any(Example2Mongo.class));

        // exampleMongodb2: find + update + findById
        Query<Example2Mongo> query = Mockito.mock(Query.class);
        Mockito.when(service2.find(Mockito.anyList())).thenReturn(query);
        Example2Mongo entity = new Example2Mongo();
        entity.setId(1971122348874665984L);
        entity.setVersion(14L);
        Mockito.when(service2.findById(Mockito.any())).thenReturn(entity);
        Assert.assertNotNull(controller.exampleMongodb2());
        Mockito.verify(service2).update(Mockito.same(query), Mockito.anyList());

        Assert.assertNotNull(controller.exampleMongodb3());

        Mockito.when(service2.findPage(Mockito.any(), Mockito.anyList()))
                .thenReturn(new com.skrstop.framework.components.core.common.response.page.ListSimplePageData<>());
        Assert.assertNotNull(controller.exampleMongodb4(new com.skrstop.framework.components.starter.mongodb.wrapper.PageQuery()));

        Assert.assertNotNull(controller.exampleMongodb5());
        Mockito.verify(service2).updateWithVersion(Mockito.eq(14L), Mockito.anyList(), Mockito.anyList());

        // multi1：两次调用分别覆盖 if 的 true/false 两侧
        Mockito.when(service1.findById(Mockito.any())).thenReturn(null, null);
        Mockito.when(service2.findById(Mockito.any())).thenReturn(null, null);
        List<Object> multi = controller.exampleMongodbMulti1();
        Assert.assertNotNull(multi);
        Mockito.when(service1.findById(Mockito.any())).thenReturn(new Example1Mongo(), new Example1Mongo());
        Mockito.when(service2.findById(Mockito.any())).thenReturn(new Example2Mongo(), new Example2Mongo());
        multi = controller.exampleMongodbMulti1();
        Assert.assertNotNull(multi);
        // saveMaster/saveSlave 方法体已在 DbServiceTest#testMongoServices 中覆盖
    }
}
