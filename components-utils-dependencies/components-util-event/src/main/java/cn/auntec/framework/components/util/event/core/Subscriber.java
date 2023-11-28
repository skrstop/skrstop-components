package cn.auntec.framework.components.util.event.core;

import cn.auntec.framework.components.util.event.EventBus;
import lombok.Getter;

/**
 * Subscriber of event.
 *
 * @author 蒋时华
 * @see EventBus
 */
public abstract class Subscriber<T extends Event> {
    /**
     * 接到事件是否同步执行
     */
    @Getter
    protected boolean sync = true;

    /**
     * 消费者名字
     */
    @Getter
    protected String subscriberName = "subscriber";

    /**
     * 事件订阅者
     */
    protected Subscriber() {
    }

    /**
     * 事件订阅者
     */
    protected Subscriber(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    /**
     * 事件订阅者
     *
     * @param sync 是否同步
     */
    protected Subscriber(boolean sync) {
        this.sync = sync;
    }

    /**
     * 事件订阅者
     *
     * @param sync 是否同步
     */
    protected Subscriber(boolean sync, String subscriberName) {
        this.sync = sync;
        this.subscriberName = subscriberName;
    }

    /**
     * 事件处理，请处理异常
     *
     * @param event 事件
     */
    public abstract void onEvent(T event);
}
