package com.zoe.framework.components.util.event.definition;

import com.zoe.framework.components.util.event.core.CallbackResult;
import com.zoe.framework.components.util.event.core.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * event 事件默认实现
 *
 * @author 蒋时华
 * @date 2020-05-20 09:56:13
 */
public abstract class EventTemplate<T> implements Event<T>, CallbackResult<T> {

    @Getter
    private T eventData;

    @Getter
    @Setter
    private String eventName = "event";

    public EventTemplate() {

    }

    public EventTemplate(T eventData) {
        this.eventData = eventData;
    }

    public EventTemplate(T eventData, String eventName) {
        this.eventData = eventData;
        this.eventName = eventName;
    }

    /**
     * 初始化触发
     */
    public void init(T eventData) {

    }

    /**
     * 执行之前触发
     */
    public void before(T eventData) {

    }

    /**
     * 事件执行
     */
    public abstract void execute(T eventData);

    /**
     * 执行之后触发
     */
    public void after(T eventData) {

    }

    /**
     * 事件结束后执行
     */
    public void finish(T eventData) {

    }

    @Override
    public void onSuccess(T response) {

    }

    @Override
    public void onFailure(Throwable e, T obj) {

    }


}
