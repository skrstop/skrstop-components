package com.skrstop.framework.components.starter.annotation.handle.server;

import com.skrstop.framework.components.starter.annotation.anno.server.SProcessor;
import com.skrstop.framework.components.starter.annotation.exception.aspect.ProcessorException;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorContainer;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorContext;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorEntity;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.DefaultAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author 蒋时华
 */
public class ProcessorContextTest {

    private final ProcessorContainerConfigurationSim configuration =
            new ProcessorContainerConfigurationSim();

    static class ProcessorContainerConfigurationSim
            extends com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorContainerConfiguration {
    }

    @After
    public void tearDown() {
        ProcessorContext.getConstainers().clear();
    }

    @SProcessor(processorName = "p1", description = "d1", ordered = 1)
    static class Processor1 implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return "match".equals(currentValue);
        }
    }

    @SProcessor(processorName = "pDefault", defaultProcessor = true, ordered = 2)
    static class DefaultProcessor1 implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return true;
        }
    }

    @SProcessor(processorName = "dup")
    static class DupProcessor {
    }

    @SProcessor(processorName = "dup")
    static class DupProcessor2 {
    }

    @SProcessor(containerName = "other")
    static class OtherProcessor {
    }

    // defaultProcessor 但无断言实现 → DefaultAssert → isSupported=false
    @SProcessor(processorName = "dFalse", defaultProcessor = true)
    static class FalseDefaultProcessor {
    }

    @SProcessor(processorName = "assertSelf")
    static class SelfAssertProcessor implements com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert<String> {
        @Override
        public boolean isSupported(String currentValue) {
            return "self".equals(currentValue);
        }
    }

    @Test
    public void testRegisterAndSort() {
        configuration.postProcessAfterInitialization(new Processor1(), "processor1");
        configuration.postProcessAfterInitialization(new DefaultProcessor1(), "default1");

        ProcessorContainer container = ProcessorContext.getConstainers()
                .get(ProcessorContext.DEFAULT_CONTAINERS_NAME);
        Assert.assertNotNull(container);
        Assert.assertEquals(1, container.getProcessorEntities().size());
        Assert.assertEquals(1, container.getProcessorEntitiesDefault().size());

        // 排序
        container.getProcessorEntities().clear();
        ProcessorEntity low = ProcessorEntity.builder().ordered(5).build();
        ProcessorEntity high = ProcessorEntity.builder().ordered(1).build();
        container.getProcessorEntities().add(low);
        container.getProcessorEntities().add(high);
        container.getProcessorEntitiesDefault().add(low);
        container.getProcessorEntitiesDefault().add(high);
        container.init();
        Assert.assertSame(high, container.getProcessorEntities().get(0));
        Assert.assertSame(high, container.getProcessorEntitiesDefault().get(0));
    }

    @Test
    public void testNonSProcessorBeanIgnored() {
        Object result = configuration.postProcessAfterInitialization(new Object(), "plain");
        Assert.assertNotNull(result);
        Assert.assertTrue(ProcessorContext.getConstainers().isEmpty());
    }

    @Test
    public void testDefaultAssertUsed() {
        // 无断言设置 → 注册时使用 DefaultAssert
        configuration.postProcessAfterInitialization(new DupProcessor(), "dup1");
        // 只有非 default 处理器，值不匹配且 default 列表为空 → 抛 ProcessorException
        configuration.postProcessAfterInitialization(new Processor1(), "p1");
        try {
            ProcessorContext.getProcessors("no-match", (Object) "x", true);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        // DefaultAssert.isSupported → false 覆盖
        Assert.assertFalse(new DefaultAssert().isSupported(new Object()));
    }

    @Test
    public void testSelfAssertProcessor() {
        configuration.postProcessAfterInitialization(new SelfAssertProcessor(), "sa");
        // self 断言（bean 自身实现 ProcessorAssert → bean instanceof 分支）
        Object one = ProcessorContext.getProcessorOne(null, (Object) "self", true);
        Assert.assertNotNull(one);
        List<Object> list = ProcessorContext.getProcessors(null, (Object) "self", true);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testProcessorAssertClassBranch() {
        // assertClass 分支：assertBeanClass 为 void → 走 bean instanceof
        configuration.postProcessAfterInitialization(new Processor1(), "p1");
        // 命中
        Assert.assertNotNull(ProcessorContext.getProcessorOne(null, (Object) "match", true));
        // 不命中且有 default
        configuration.postProcessAfterInitialization(new DefaultProcessor1(), "pd");
        Assert.assertNotNull(ProcessorContext.getProcessorOne(null, (Object) "no-match", true));
        // containDefault=false → 异常
        try {
            ProcessorContext.getProcessorOneWithoutDefault(null, (Object) "x", Object.class);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorsWithoutDefault(null, (Object) "x", Object.class);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
    }

    @Test
    public void testContainerNotExists() {
        try {
            ProcessorContext.getProcessors("nothing", new Object(), true);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorOne("nothing", new Object(), true);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorOneByName("nothing");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorOneDefault("nothing");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorsDefault("nothing");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorsAll("nothing", null, Object.class);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
    }

    @Test
    public void testGetProcessorOneByName() {
        configuration.postProcessAfterInitialization(new Processor1(), "processor1");
        // processorName 非空时 key 使用 processorName
        Assert.assertNotNull(ProcessorContext.getProcessorOneByName("p1"));
        Assert.assertNotNull(ProcessorContext.getProcessorOneByName(null, "p1"));
        try {
            ProcessorContext.getProcessorOneByName("missing");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        // 其他容器
        try {
            ProcessorContext.getProcessorOneByName("other-container", "processor1");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
    }

    @Test
    public void testMultipleContainers() {
        configuration.postProcessAfterInitialization(new Processor1(), "processor1");
        configuration.postProcessAfterInitialization(new OtherProcessor(), "other1");
        Assert.assertNotNull(ProcessorContext.getConstainers().get("other"));
        // other 容器空查询
        try {
            ProcessorContext.getProcessors("other", (Object) "no-match", true);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
    }

    @Test
    public void testDuplicateKeyThrows() {
        configuration.postProcessAfterInitialization(new DupProcessor(), "dup");
        try {
            configuration.postProcessAfterInitialization(new DupProcessor2(), "dup");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testGetProcessorsAll() {
        configuration.postProcessAfterInitialization(new Processor1(), "p1");
        configuration.postProcessAfterInitialization(new DefaultProcessor1(), "pd");

        // null → 全部
        Assert.assertEquals(2, ProcessorContext.getProcessorsAllWithDefault(Object.class).size());
        // true → 仅 default
        Assert.assertEquals(1, ProcessorContext.getProcessorsAll(true, Object.class).size());
        // false → 仅非 default
        Assert.assertEquals(1, ProcessorContext.getProcessorsAll(false, Object.class).size());
        // 指定容器名
        Assert.assertEquals(2, ProcessorContext.getProcessorsAll(ProcessorContext.DEFAULT_CONTAINERS_NAME, null, Object.class).size());
    }

    @Test
    public void testGetProcessorOneDefault() {
        configuration.postProcessAfterInitialization(new DefaultProcessor1(), "pd");
        Assert.assertNotNull(ProcessorContext.getProcessorOneDefault("no-match"));
        Assert.assertNotNull(ProcessorContext.getProcessorOneDefault(ProcessorContext.DEFAULT_CONTAINERS_NAME, "no-match"));
        try {
            ProcessorContext.getProcessorOneDefault("other", "no-match");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
    }

    @Test
    public void testGetProcessorsDefaultList() {
        configuration.postProcessAfterInitialization(new DefaultProcessor1(), "pd");
        Assert.assertEquals(1, ProcessorContext.getProcessorsDefault("no-match").size());
        Assert.assertEquals(1, ProcessorContext.getProcessorsDefault(ProcessorContext.DEFAULT_CONTAINERS_NAME, "no-match").size());
        try {
            ProcessorContext.getProcessorsDefault("other", "no-match");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
    }

    @Test
    public void testOrderAndContextAccessors() {
        Assert.assertEquals(org.springframework.core.Ordered.LOWEST_PRECEDENCE, configuration.getOrder());
        configuration.setApplicationContext(new org.springframework.context.support.StaticApplicationContext());
        Assert.assertNotNull(configuration);
        Assert.assertEquals("defaultProcessorContainer", ProcessorContext.DEFAULT_CONTAINERS_NAME);
    }

    @Test
    public void testDelegateOverloads() {
        configuration.postProcessAfterInitialization(new Processor1(), "processor1");
        configuration.postProcessAfterInitialization(new DefaultProcessor1(), "default1");
        configuration.postProcessAfterInitialization(new FalseDefaultProcessor(), "dfp");
        new ProcessorContext();
        Object match = "match";
        // getProcessorOne 系列委托
        Assert.assertNotNull(ProcessorContext.getProcessorOne(match, String.class));
        Assert.assertNotNull(ProcessorContext.getProcessorOne(null, match, String.class));
        Assert.assertNotNull(ProcessorContext.getProcessorOne(ProcessorContext.DEFAULT_CONTAINERS_NAME, match, String.class));
        Assert.assertNotNull(ProcessorContext.getProcessorOne(match, true));
        // default 兜底
        Assert.assertNotNull(ProcessorContext.getProcessorOne("no-match", true));
        // 无 default 匹配 → 抛出（other 容器只有 DefaultAssert 处理器）
        configuration.postProcessAfterInitialization(new OtherProcessor(), "other1");
        try {
            ProcessorContext.getProcessorOne("other", (Object) "x", true);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        // getProcessorOneWithoutDefault 委托
        Assert.assertNotNull(ProcessorContext.getProcessorOneWithoutDefault(match, String.class));
        Assert.assertNotNull(ProcessorContext.getProcessorOneWithoutDefault(null, match, String.class));
        // getProcessors 系列
        Assert.assertEquals(1, ProcessorContext.getProcessors(match, String.class).size());
        Assert.assertEquals(1, ProcessorContext.getProcessors(null, match, String.class).size());
        Assert.assertEquals(1, ProcessorContext.getProcessorsWithoutDefault(
                ProcessorContext.DEFAULT_CONTAINERS_NAME, match, String.class).size());
        Assert.assertEquals(1, ProcessorContext.getProcessorsWithoutDefault(match, String.class).size());
        // containDefault=true → default 兜底返回
        Assert.assertEquals(1, ProcessorContext.getProcessors("no-match", true).size());
        Assert.assertEquals(1, ProcessorContext.getProcessors(match, true).size());
        // containDefault=false 且无匹配 → 抛出
        try {
            ProcessorContext.getProcessors("no-match", false);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorsWithoutDefault((Object) "no-match", String.class);
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        // byName 系列委托
        Assert.assertNotNull(ProcessorContext.getProcessorOneByName("p1", String.class));
        Assert.assertNotNull(ProcessorContext.getProcessorOneByName(null, "p1", String.class));
        // default 系列
        Assert.assertNotNull(ProcessorContext.getProcessorOneDefault((Object) "any", String.class));
        Assert.assertNotNull(ProcessorContext.getProcessorOneDefault(null, (Object) "any", String.class));
        Assert.assertEquals(1, ProcessorContext.getProcessorsDefault((Object) "any", String.class).size());
        Assert.assertEquals(1, ProcessorContext.getProcessorsDefault(null, (Object) "any", String.class).size());
        // other 容器无 default → 抛出
        try {
            ProcessorContext.getProcessorOneDefault("other", (Object) "x");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        try {
            ProcessorContext.getProcessorsDefault("other", (Object) "x");
            Assert.fail();
        } catch (ProcessorException ignored) {
        }
        // all 系列
        Assert.assertEquals(3, ProcessorContext.getProcessorsAllWithDefault(String.class).size());
        Assert.assertEquals(3, ProcessorContext.getProcessorsAllWithDefault(
                ProcessorContext.DEFAULT_CONTAINERS_NAME, String.class).size());
        Assert.assertEquals(2, ProcessorContext.getProcessorsAll(true, String.class).size());
        Assert.assertEquals(1, ProcessorContext.getProcessorsAll(false, String.class).size());
    }

}