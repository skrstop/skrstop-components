package com.zoe.framework.components.util.event.example;

import com.zoe.framework.components.util.event.EventBus;
import com.zoe.framework.components.util.event.core.Event;
import com.zoe.framework.components.util.event.core.Subscriber;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-12-16 18:16:57
 */
public class EventBusTest2 {

    // 测试事件
    public static class TestEvent implements Event<String> {
        private String message;

        public TestEvent(String message) {
            this.message = message;
        }

        public void print() {
            System.out.println(message);
        }

        @Override
        public String getEventData() {
            return this.message;
        }

        @Override
        public String getEventName() {
            return "event";
        }
    }

    // 测试订阅者
    public static class TestSubscriber extends Subscriber<TestEvent> {
        public TestSubscriber() {
        }

        public TestSubscriber(boolean sync) {
            super(sync);
        }

        @SneakyThrows
        @Override
        public void onEvent(TestEvent event) {
            TimeUnit.SECONDS.sleep(5);
            event.print();
        }
    }

    @Test
    public void testEvent() {
        // 注册订阅者
        // new TestSubscriber(false) 异步处理， 默认同步处理
        EventBus.register(TestEvent.class, new TestSubscriber());
        System.out.println("开始投递事件");
        EventBus.postAll(new TestEvent("第一个事件"));
        System.out.println("休息一会");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
