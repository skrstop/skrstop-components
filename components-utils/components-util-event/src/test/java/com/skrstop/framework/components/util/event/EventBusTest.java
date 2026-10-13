package com.skrstop.framework.components.util.event;

import com.skrstop.framework.components.util.event.core.Subscriber;
import com.skrstop.framework.components.util.event.definition.EventTemplate;
import com.skrstop.framework.components.util.event.definition.SubscriberTemplate;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class EventBusTest {

    private static class TestEvent extends EventTemplate<String> {
        TestEvent() {
            super("data", "testEvent");
        }

        @Override
        public void execute(String eventData) {
        }
    }

    @Test
    public void testIsEnable() {
        Assert.assertTrue(EventBus.isEnable());
        // 未注册订阅者
        Assert.assertFalse(EventBus.isEnable(TestEvent.class));

        EventBus.register(TestEvent.class, new SubscriberTemplate() {
            @Override
            public void onEvent(EventTemplate event) {
            }
        });
        Assert.assertTrue(EventBus.isEnable(TestEvent.class));
    }

    @Test
    public void testRegisterUnregisterAndPost() {
        final AtomicInteger count = new AtomicInteger();
        Subscriber<TestEvent> subscriber = new Subscriber<TestEvent>() {
            @Override
            public void onEvent(TestEvent event) {
                count.incrementAndGet();
                throw new RuntimeException("handle error");
            }
        };
        EventBus.register(TestEvent.class, subscriber);
        EventBus.postAll(new TestEvent());
        Assert.assertEquals(1, count.get());
        EventBus.postRotation(new TestEvent());
        Assert.assertEquals(2, count.get());
        EventBus.postRandom(new TestEvent());
        Assert.assertEquals(3, count.get());

        EventBus.unRegister(TestEvent.class, subscriber);
        EventBus.postAll(new TestEvent());
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testPostAsync() throws Exception {
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        Subscriber<TestEvent> asyncSubscriber = new Subscriber<TestEvent>(false) {
            @Override
            public void onEvent(TestEvent event) {
                latch.countDown();
            }
        };
        EventBus.register(TestEvent.class, asyncSubscriber);
        EventBus.postAll(new TestEvent());
        Assert.assertTrue(latch.await(3, java.util.concurrent.TimeUnit.SECONDS));
        EventBus.unRegister(TestEvent.class, asyncSubscriber);
    }

    private static class PoolEvent extends EventTemplate<String> {
        @Override
        public void execute(String eventData) {
        }
    }

    @Test
    public void testPostWithCustomPool() {
        final AtomicInteger count = new AtomicInteger();
        Subscriber<PoolEvent> subscriber = new Subscriber<PoolEvent>() {
            @Override
            public void onEvent(PoolEvent event) {
                count.incrementAndGet();
            }
        };
        EventBus.register(PoolEvent.class, subscriber);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 0L,
                java.util.concurrent.TimeUnit.MILLISECONDS, new java.util.concurrent.LinkedBlockingQueue<>());
        EventBus.postAll(new PoolEvent(), pool);
        EventBus.postRotation(new PoolEvent(), pool);
        EventBus.postRandom(new PoolEvent(), pool);
        pool.shutdown();
        Assert.assertEquals(3, count.get());
        EventBus.unRegister(PoolEvent.class, subscriber);
    }

    @Test
    public void testPostNoSubscribers() {
        // 未注册任何订阅者时不抛异常
        EventBus.postAll(new EventTemplate<String>() {
            @Override
            public void execute(String eventData) {
            }
        });
        EventBus.postRotation(new EventTemplate<String>() {
            @Override
            public void execute(String eventData) {
            }
        });
        EventBus.postRandom(new EventTemplate<String>() {
            @Override
            public void execute(String eventData) {
            }
        });
    }

    @Test
    public void testPostRotationWrapsAround() {
        final AtomicInteger count = new AtomicInteger();
        Subscriber<TestEvent> s1 = new Subscriber<TestEvent>() {
            @Override
            public void onEvent(TestEvent event) {
                count.incrementAndGet();
            }
        };
        Subscriber<TestEvent> s2 = new Subscriber<TestEvent>() {
            @Override
            public void onEvent(TestEvent event) {
                count.incrementAndGet();
            }
        };
        EventBus.register(TestEvent.class, s1);
        EventBus.register(TestEvent.class, s2);
        // 多次轮询消费，覆盖轮询重置分支
        for (int i = 0; i < 6; i++) {
            EventBus.postRotation(new TestEvent());
        }
        Assert.assertEquals(6, count.get());
        EventBus.unRegister(TestEvent.class, s1);
        EventBus.unRegister(TestEvent.class, s2);
    }


    @Test
    public void testConstructor() {
        Assert.assertNotNull(new EventBus());
    }

    private static class NeverRegisteredEvent extends EventTemplate<String> {
        @Override
        public void execute(String eventData) {
        }
    }

    @Test
    public void testUnRegisterNeverRegistered() {
        // 从未注册过的事件类型 → unRegister 不抛异常
        EventBus.unRegister(NeverRegisteredEvent.class, new SubscriberTemplate() {
            @Override
            public void onEvent(EventTemplate event) {
            }
        });
    }

    @Test
    public void testTemplateSubscriberOverloads() {
        SubscriberTemplate subscriber = new SubscriberTemplate() {
            @Override
            public void onEvent(EventTemplate event) {
            }
        };
        EventBus.register(TestEvent.class, subscriber);
        Assert.assertTrue(EventBus.isEnable(TestEvent.class));
        EventBus.unRegister(TestEvent.class, subscriber);
        Assert.assertFalse(EventBus.isEnable(TestEvent.class));
    }

    @Test
    public void testConcurrentRegisterSameEvent() throws Exception {
        // 并发注册同一事件类型 → 覆盖 putIfAbsent 竞争分支
        final Class<EventTemplate<String>> fresh = (Class<EventTemplate<String>>) (Class) new EventTemplate<String>() {
            @Override
            public void execute(String eventData) {
            }
        }.getClass();
        java.util.concurrent.CountDownLatch done = new java.util.concurrent.CountDownLatch(96);
        for (int round = 0; round < 12; round++) {
            java.util.concurrent.CountDownLatch start = new java.util.concurrent.CountDownLatch(1);
            for (int i = 0; i < 8; i++) {
                new Thread(() -> {
                    try {
                        start.await();
                        EventBus.register(fresh, new Subscriber<EventTemplate<String>>() {
                            @Override
                            public void onEvent(EventTemplate<String> event) {
                            }
                        });
                    } catch (InterruptedException ignored) {
                    } finally {
                        done.countDown();
                    }
                }).start();
            }
            start.countDown();
        }
        Assert.assertTrue(done.await(10, java.util.concurrent.TimeUnit.SECONDS));
    }


}
