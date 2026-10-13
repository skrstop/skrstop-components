package com.skrstop.framework.components.starter.annotation.handle.server.processor;

import com.skrstop.framework.components.starter.annotation.anno.server.SProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

public class ProcessorContainerConfigurationAssertTest {

    static class CtxAssert implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return "ctx".equals(currentValue);
        }
    }

    @SProcessor(processorName = "selfBeanNameAssert", assertBeanName = "selfAssert")
    static class SelfAssert implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return "self".equals(currentValue);
        }
    }

    @SProcessor(processorName = "ctxBeanNameAssert", assertBeanName = "ctxAssert")
    static class BeanWithCtxAssert {
    }

    @SProcessor(processorName = "missingBeanAssert", assertBeanName = "nope")
    static class BeanWithMissingAssert {
    }

    @SProcessor(processorName = "badBeanAssert", assertBeanName = "badBean")
    static class BeanWithBadAssert {
    }

    @SProcessor(processorName = "selfClassAssert", assertBeanClass = SelfClassAssert.class)
    static class SelfClassAssert implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return true;
        }
    }

    @SProcessor(processorName = "ctxClassAssert", assertBeanClass = CtxAssert.class)
    static class BeanWithCtxClassAssert {
    }

    @SProcessor(processorName = "missingClassAssert", assertBeanClass = MissingAssert.class)
    static class BeanWithMissingClassAssert {
    }

    @SProcessor(processorName = "badClassAssert", assertBeanClass = BadAssert.class)
    static class BeanWithBadClassAssert {
    }

    @SProcessor(processorName = "plainNoAssert")
    static class PlainNoAssert {
    }

    // 空容器名 → 使用默认容器
    @SProcessor(processorName = "blankContainer", containerName = "")
    static class BlankContainerBean {
    }

    // bean 自身实现 ProcessorAssert，无任何断言配置
    @SProcessor(processorName = "selfImplAssert")
    static class SelfImplAssert implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return true;
        }
    }

    @SProcessor(processorName = "badAssertClassProcessor", assertBeanClass = BadAssert.class, assertClass = BadAssert.class)
    static class BeanWithBadAssertClass {
    }

    @SProcessor(processorName = "assertClassProcessor2", assertBeanClass = AssertClassAssert.class, assertClass = AssertClassAssert.class)
    static class BeanWithAssertClass2 {
    }

    @SProcessor(processorName = "noCtxBeanNameAssert", assertBeanName = "whatever")
    static class BeanWithAssertNameNoCtx {
    }

    static class MissingAssert {
    }

    static class BadAssert {
    }

    static class AssertClassAssert implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return "assertClass".equals(currentValue);
        }
    }

    @SProcessor(processorName = "assertClassProcessor", assertBeanClass = AssertClassAssert.class, assertClass = AssertClassAssert.class)
    static class BeanWithAssertClass {
    }

    private final StaticApplicationContext ctx = new StaticApplicationContext();

    @After
    public void tearDown() {
        ProcessorContext.getConstainers().clear();
    }

    @Test
    public void testAssertBeanNamePaths() {
        ctx.getBeanFactory().registerSingleton("ctxAssert", new CtxAssert());
        ctx.getBeanFactory().registerSingleton("badBean", new Object());
        ctx.refresh();
        ProcessorContainerConfigurationSim configuration = new ProcessorContainerConfigurationSim();
        configuration.setApplicationContext(ctx);

        // assertBeanName == beanName → bean 自身实现断言
        configuration.postProcessAfterInitialization(new SelfAssert(), "selfAssert");
        ProcessorEntity selfEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("selfBeanNameAssert");
        Assert.assertTrue(selfEntity.getProcessorAssert() instanceof SelfAssert);

        // assertBeanName != beanName → 从容器取
        configuration.postProcessAfterInitialization(new BeanWithCtxAssert(), "bwca");
        ProcessorEntity ctxEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("ctxBeanNameAssert");
        Assert.assertTrue(ctxEntity.getProcessorAssert() instanceof CtxAssert);
        Assert.assertTrue(ctxEntity.getProcessorAssert().isSupported("ctx"));

        // assertBeanName 指向不存在的 bean
        try {
            configuration.postProcessAfterInitialization(new BeanWithMissingAssert(), "bwma");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }

        // assertBeanName 指向未实现 ProcessorAssert 的 bean
        try {
            configuration.postProcessAfterInitialization(new BeanWithBadAssert(), "bwba");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        ctx.close();
    }

    @Test
    public void testAssertBeanClassPaths() {
        ctx.getBeanFactory().registerSingleton("ctxAssert", new CtxAssert());
        ctx.getBeanFactory().registerSingleton("badAssert", new BadAssert());
        ctx.refresh();
        ProcessorContainerConfigurationSim configuration = new ProcessorContainerConfigurationSim();
        configuration.setApplicationContext(ctx);

        // assertBeanClass == bean.getClass() → bean 自身实现断言
        configuration.postProcessAfterInitialization(new SelfClassAssert(), "sca");
        ProcessorEntity selfEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("selfClassAssert");
        Assert.assertTrue(selfEntity.getProcessorAssert() instanceof SelfClassAssert);

        // assertBeanClass 从容器取
        configuration.postProcessAfterInitialization(new BeanWithCtxClassAssert(), "bwcca");
        ProcessorEntity ctxEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("ctxClassAssert");
        Assert.assertTrue(ctxEntity.getProcessorAssert() instanceof CtxAssert);

        // assertBeanClass 未注册
        try {
            configuration.postProcessAfterInitialization(new BeanWithMissingClassAssert(), "bwmca");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }

        // assertBeanClass 未实现 ProcessorAssert
        try {
            configuration.postProcessAfterInitialization(new BeanWithBadClassAssert(), "bwbca");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }

        // 无任何断言设置 → DefaultAssert
        configuration.postProcessAfterInitialization(new PlainNoAssert(), "pna");
        ProcessorEntity plainEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("plainNoAssert");
        Assert.assertTrue(plainEntity.getProcessorAssert()
                instanceof com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.DefaultAssert);
        ctx.close();
    }

    @Test
    public void testAssertClassPath() {
        // 无 applicationContext，assertBeanClass 非 void，assertClass 实现 ProcessorAssert
        ProcessorContainerConfigurationSim configuration = new ProcessorContainerConfigurationSim();
        configuration.postProcessAfterInitialization(new BeanWithAssertClass(), "bwac");
        ProcessorEntity entity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("assertClassProcessor");
        Assert.assertTrue(entity.getProcessorAssert() instanceof AssertClassAssert);
        Assert.assertTrue(entity.getProcessorAssert().isSupported("assertClass"));
        // 第二个相同 assertClass → processorAssertMap 缓存命中
        configuration.postProcessAfterInitialization(new BeanWithAssertClass2(), "bwac2");
        ProcessorEntity entity2 = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("assertClassProcessor2");
        Assert.assertTrue(entity2.getProcessorAssert() instanceof AssertClassAssert);

        // assertClass 未实现 ProcessorAssert → 条件不成立 → bean instanceof → DefaultAssert
        configuration.postProcessAfterInitialization(new BeanWithBadAssertClass(), "bwbac");
        ProcessorEntity badEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("badAssertClassProcessor");
        Assert.assertTrue(badEntity.getProcessorAssert()
                instanceof com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.DefaultAssert);

        // 空容器名 → 默认容器
        configuration.postProcessAfterInitialization(new BlankContainerBean(), "bcb");
        ProcessorEntity blankEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("blankContainer");
        Assert.assertNotNull(blankEntity);
    }

    @Test
    public void testSelfImplAssertAndNoCtx() {
        // bean 自身实现 ProcessorAssert → instanceof 分支
        ProcessorContainerConfigurationSim configuration = new ProcessorContainerConfigurationSim();
        configuration.postProcessAfterInitialization(new SelfImplAssert(), "sia");
        ProcessorEntity entity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("selfImplAssert");
        Assert.assertTrue(entity.getProcessorAssert() instanceof SelfImplAssert);

        // 有 assertBeanName 但 applicationContext 为 null → 跳过
        configuration.postProcessAfterInitialization(new BeanWithAssertNameNoCtx(), "bwannc");
        ProcessorEntity noCtxEntity = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME).getProcessorEntitiesKey().get("noCtxBeanNameAssert");
        Assert.assertTrue(noCtxEntity.getProcessorAssert()
                instanceof com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.DefaultAssert);
    }

    static class ProcessorContainerConfigurationSim
            extends ProcessorContainerConfiguration {
    }
}
