package com.skrstop.framework.components.util.event.definition;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class EventTemplateTest {

    private static class TestEvent extends EventTemplate<String> {
        final AtomicInteger inited = new AtomicInteger();
        final AtomicInteger before = new AtomicInteger();
        final AtomicInteger executed = new AtomicInteger();
        final AtomicInteger after = new AtomicInteger();
        final AtomicInteger finished = new AtomicInteger();

        TestEvent() {
            super("data", "name");
        }

        @Override
        public void init(String eventData) {
            inited.incrementAndGet();
        }

        @Override
        public void before(String eventData) {
            before.incrementAndGet();
        }

        @Override
        public void execute(String eventData) {
            executed.incrementAndGet();
        }

        @Override
        public void after(String eventData) {
            after.incrementAndGet();
        }

        @Override
        public void finish(String eventData) {
            finished.incrementAndGet();
        }
    }

    @Test
    public void testConstructors() {
        EventTemplate<String> e1 = new EventTemplate<String>() {
            @Override
            public void execute(String eventData) {
            }
        };
        Assert.assertEquals("event", e1.getEventName());
        Assert.assertNull(e1.getEventData());

        TestEvent e2 = new TestEvent();
        Assert.assertEquals("name", e2.getEventName());
        Assert.assertEquals("data", e2.getEventData());

        EventTemplate<String> e3 = new EventTemplate<String>("d", "n") {
            @Override
            public void execute(String eventData) {
            }
        };
        Assert.assertEquals("n", e3.getEventName());
        Assert.assertEquals("d", e3.getEventData());

        e3.setEventName("n2");
        Assert.assertEquals("n2", e3.getEventName());
    }

    @Test
    public void testLifecycleHooks() {
        TestEvent event = new TestEvent();
        event.init("d");
        event.before("d");
        event.execute("d");
        event.after("d");
        event.finish("d");
        event.onSuccess("d");
        event.onFailure(new RuntimeException("e"), "d");
        Assert.assertEquals(1, event.inited.get());
        Assert.assertEquals(1, event.before.get());
        Assert.assertEquals(1, event.executed.get());
        Assert.assertEquals(1, event.after.get());
        Assert.assertEquals(1, event.finished.get());
    }

}
