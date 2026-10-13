package com.skrstop.framework.components.example.starters.simple;

import com.skrstop.framework.components.example.starters.simple.constant.ProcessorContainerNameConst;
import com.skrstop.framework.components.example.starters.simple.controller.ExampleAnnotationController;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessor1Service;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessor2Service;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessorDefaultService;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorContainer;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorContext;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorEntity;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationControllerTest {

    @Test
    public void testAnnotationEndpoints() {
        ExampleAnnotationController controller = new ExampleAnnotationController();
        controller.exampleAnnotationAccessControl();
        controller.exampleAnnotationAccessLimit();
        controller.exampleAnnotationAccessLimitCustom();
        controller.exampleAnnotationIntranetLimit();
        Assert.assertNotNull(controller.exampleAnnotationPrivacyInfo());
    }

    @Test
    public void testExecuteTimeLog() throws InterruptedException {
        new ExampleAnnotationController().exampleAnnotationExecuteTimeLog();
    }

    @Test
    public void testServiceLock() throws InterruptedException {
        ExampleAnnotationController controller = new ExampleAnnotationController();
        controller.exampleAnnotationServiceLock();
        controller.exampleAnnotationServiceLock2();
    }

    @Test
    public void testProcessorEndpoint() {
        // 手工装配 ProcessorContext，模拟 SProcessor 启动时注册的容器
        ProcessorContainer container = new ProcessorContainer();
        container.getProcessorEntities().add(ProcessorEntity.builder()
                .containerName(ProcessorContainerNameConst.PROCESSOR_POOL)
                .processor(new ExampleProcessor1Service())
                .processorAssert(new ExampleProcessor1Service.AssertProcessor())
                .key("processor1Class")
                .build());
        container.getProcessorEntities().add(ProcessorEntity.builder()
                .containerName(ProcessorContainerNameConst.PROCESSOR_POOL)
                .processor(new ExampleProcessor2Service())
                .processorAssert(new ExampleProcessor2Service())
                .key("processor2Class")
                .build());
        container.getProcessorEntitiesDefault().add(ProcessorEntity.builder()
                .containerName(ProcessorContainerNameConst.PROCESSOR_POOL)
                .processor(new ExampleProcessorDefaultService())
                .processorAssert(new ExampleProcessorDefaultService())
                .key("processorDefaultClass")
                .defaultProcessor(true)
                .build());
        container.init();
        ProcessorContext.getConstainers().put(ProcessorContainerNameConst.PROCESSOR_POOL, container);

        new ExampleAnnotationController().exampleAnnotationProcessor();
        Assert.assertTrue(true);
    }
}
