package cn.auntec.framework.components.util.event.example;

import cn.auntec.framework.components.util.event.EventBus;
import cn.auntec.framework.components.util.event.definition.EventTemplate;
import cn.auntec.framework.components.util.event.definition.SubscriberTemplate;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-12-16 18:13:51
 */
public class EventBusExample {

    // 测试事件
    static class TestEvent extends EventTemplate<String> {
        public TestEvent(String eventData) {
            super(eventData);
        }

        @Override
        public void init(String eventData) {
            super.init(eventData);
            System.out.println("init");
        }

        @Override
        public void before(String eventData) {
            super.before(eventData);
            System.out.println("before");
        }

        @Override
        public void after(String eventData) {
            super.after(eventData);
            System.out.println("after");
        }

        @Override
        public void finish(String eventData) {
            super.finish(eventData);
            System.out.println("finish");
        }

        @SneakyThrows
        @Override
        public void execute(String eventData) {
            System.out.println(eventData);
            System.out.println("execute");
            TimeUnit.SECONDS.sleep(5);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 注册订阅者
        // new SubscriberTemplate(false) 异步处理， 默认同步处理
        EventBus.register(TestEvent.class, new SubscriberTemplate(false));
        System.out.println("开始投递事件");
        EventBus.postAll(new TestEvent("第一个事件"));
        System.out.println("休息一会");
        TimeUnit.SECONDS.sleep(10);
    }


}
