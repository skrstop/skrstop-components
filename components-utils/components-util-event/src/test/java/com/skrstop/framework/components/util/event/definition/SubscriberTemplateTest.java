package com.skrstop.framework.components.util.event.definition;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class SubscriberTemplateTest {

    @Test
    public void testConstructors() {
        Assert.assertEquals("subscriber", new SubscriberTemplate().getSubscriberName());
        Assert.assertTrue(new SubscriberTemplate().isSync());
        Assert.assertEquals("n", new SubscriberTemplate("n").getSubscriberName());
        Assert.assertFalse(new SubscriberTemplate(false).isSync());
        Assert.assertEquals("n2", new SubscriberTemplate(false, "n2").getSubscriberName());
        Assert.assertTrue(new SubscriberTemplate(true, "n3").isSync());
    }

    @Test
    public void testOnEventFull() {
        final AtomicInteger executed = new AtomicInteger();
        EventTemplate<String> event = new EventTemplate<String>("data", "evt") {
            @Override
            public void execute(String eventData) {
                executed.incrementAndGet();
            }
        };
        new SubscriberTemplate().onEvent(event);
        Assert.assertEquals(1, executed.get());
    }

    @Test
    public void testOnEventNull() {
        // null 事件：第一处判空仅用于日志，后续调用会抛 NPE（现有行为）
        try {
            new SubscriberTemplate().onEvent(null);
            Assert.fail("should throw");
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void testOnEventExecuteThrows() {
        // execute 抛异常 → onFailure 回调被触发
        final AtomicInteger failure = new AtomicInteger();
        EventTemplate<String> event = new EventTemplate<String>("data", "evt") {
            @Override
            public void execute(String eventData) {
                throw new IllegalStateException("boom");
            }

            @Override
            public void onFailure(Throwable e, String obj) {
                failure.incrementAndGet();
            }
        };
        new SubscriberTemplate().onEvent(event);
        Assert.assertEquals(1, failure.get());
    }

}
