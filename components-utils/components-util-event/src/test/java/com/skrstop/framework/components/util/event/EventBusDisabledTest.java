package com.skrstop.framework.components.util.event;

import com.skrstop.framework.components.util.event.definition.EventTemplate;
import org.junit.Assert;
import org.junit.Test;

/**
 * 在 event.bus.enable=false 的 JVM 中运行（surefire 独立执行），覆盖关闭分支
 *
 * @author 蒋时华
 */
public class EventBusDisabledTest {

    private static class TestEvent extends EventTemplate<String> {
        @Override
        public void execute(String eventData) {
        }
    }

    @Test
    public void testBusDisabled() {
        System.out.println("BUSPROP=" + System.getProperty("event.bus.enable"));
        Assert.assertFalse(EventBus.isEnable());
        Assert.assertFalse(EventBus.isEnable(TestEvent.class));
        // 关闭状态下 post 不做任何事
        EventBus.postAll(new TestEvent());
        EventBus.postRotation(new TestEvent());
        EventBus.postRandom(new TestEvent());
    }

}
