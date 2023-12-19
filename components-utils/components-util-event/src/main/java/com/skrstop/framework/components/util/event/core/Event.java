package com.skrstop.framework.components.util.event.core;

/**
 * 标明是一个事件，可以丢到事件总线中
 *
 * @author 蒋时华
 */
public interface Event<T> {

    /**
     * 事件数据
     *
     * @return
     */
    T getEventData();

    /**
     * 事件名字
     *
     * @return
     */
    String getEventName();

}
