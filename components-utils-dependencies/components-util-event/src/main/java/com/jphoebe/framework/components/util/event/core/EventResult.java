package com.jphoebe.framework.components.util.event.core;

/**
 * 标明是一个事件，可以丢到事件总线中
 *
 * @author 蒋时华
 */
public interface EventResult<T, R> extends Event<T> {

    @Override
    default T getEventData() {
        return null;
    }

    /**
     * 事件数据
     *
     * @return
     */
    R getEventData(T request);

}
