/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.skrstop.framework.components.util.event;

import cn.hutool.core.util.RandomUtil;
import com.skrstop.framework.components.util.event.core.Event;
import com.skrstop.framework.components.util.event.core.Subscriber;
import com.skrstop.framework.components.util.event.definition.EventTemplate;
import com.skrstop.framework.components.util.event.definition.SubscriberTemplate;
import com.skrstop.framework.components.util.executor.AsyncRuntimeExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simply event bus for internal event transport.
 *
 * @author 蒋时华
 */
@Slf4j
public class EventBus {

    /**
     * 是否启动事件总线，关闭后，可能tracer等会失效，但是可以提高性能
     */
    public static final String EVENT_BUS_ENABLE_KEY = "event.bus.enable";

    /**
     * 是否允许携带上下文附件，关闭后只能传递"."开头的key，"_" 开头的Key将不被保持和传递。<br>
     * 在性能测试等场景可能关闭此传递功能。
     */
    private static final boolean EVENT_BUS_ENABLE =
            Boolean.parseBoolean(System.getProperty(EVENT_BUS_ENABLE_KEY, "true"));

    /**
     * 某中事件的订阅者
     */
    private static final ConcurrentMap<Class<? extends Event>, CopyOnWriteArrayList<Subscriber>>
            SUBSCRIBER_MAP =
            new ConcurrentHashMap<Class<? extends Event>, CopyOnWriteArrayList<Subscriber>>();

    /*** 上一次轮训消费者 */
    private static volatile AtomicInteger LAST_ROTATION_SUBSCRIBER = new AtomicInteger(0);

    /**
     * 是否开启事件总线功能
     *
     * @return 是否开启事件总线功能
     */
    public static boolean isEnable() {
        return EVENT_BUS_ENABLE;
    }

    /**
     * 是否开启事件总线功能
     *
     * @param eventClass 事件类型
     * @return 是否开启事件总线功能
     */
    public static boolean isEnable(Class<? extends Event> eventClass) {
        return EVENT_BUS_ENABLE && isNotEmpty(SUBSCRIBER_MAP.get(eventClass));
    }

    /**
     * 判断一个集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    private static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    private static void registerEvent(Class eventClass, Subscriber subscriber) {
        CopyOnWriteArrayList<Subscriber> set = SUBSCRIBER_MAP.get(eventClass);
        if (set == null) {
            set = new CopyOnWriteArrayList<Subscriber>();
            CopyOnWriteArrayList<Subscriber> old = SUBSCRIBER_MAP.putIfAbsent(eventClass, set);
            if (old != null) {
                set = old;
            }
        }
        set.addIfAbsent(subscriber);
        log.debug("Register subscriber: {} of event: {}.", subscriber, eventClass);
    }

    /**
     * 注册一个订阅者
     *
     * @param eventClass 事件类型
     * @param subscriber 订阅者
     */
    public static <T extends Event> void register(Class<T> eventClass, Subscriber<T> subscriber) {
        registerEvent(eventClass, subscriber);
    }

    /**
     * 注册一个订阅者
     *
     * @param eventClass 事件类型
     * @param subscriber 订阅者
     */
    public static <T extends EventTemplate> void register(Class<T> eventClass, SubscriberTemplate subscriber) {
        registerEvent(eventClass, subscriber);
    }

    private static void unRegisterEvent(Class eventClass, Subscriber subscriber) {
        CopyOnWriteArrayList<Subscriber> set = SUBSCRIBER_MAP.get(eventClass);
        if (set != null) {
            set.remove(subscriber);
            log.debug("UnRegister subscriber: {} of event: {}.", subscriber, eventClass);
        }
    }

    /**
     * 卸载一个订阅者
     *
     * @param eventClass 事件类型
     * @param subscriber 订阅者
     */
    public static <T extends Event> void unRegister(Class<T> eventClass, Subscriber<T> subscriber) {
        unRegisterEvent(eventClass, subscriber);
    }

    /**
     * 卸载一个订阅者
     *
     * @param eventClass 事件类型
     * @param subscriber 订阅者
     */
    public static <T extends EventTemplate> void unRegister(Class<T> eventClass, SubscriberTemplate subscriber) {
        unRegisterEvent(eventClass, subscriber);
    }

    /**
     * 给事件总线中丢一个事件
     *
     * @param event 事件
     */
    public static void postAll(final Event event) {
        postAll(event, AsyncRuntimeExecutor.getAsyncThreadPool());
    }

    /**
     * 给事件总线中丢一个事件
     *
     * @param event 事件
     */
    public static void postAll(final Event event, ThreadPoolExecutor threadPoolExecutor) {
        if (!isEnable()) {
            return;
        }
        CopyOnWriteArrayList<Subscriber> subscribers = SUBSCRIBER_MAP.get(event.getClass());
        if (!isNotEmpty(subscribers)) {
            return;
        }
        for (final Subscriber subscriber : subscribers) {
            handleSubscriber(subscriber, event, threadPoolExecutor);
        }
    }

    /**
     * 给事件总线中丢一个事件
     * 消费者轮训消费
     *
     * @param event
     */
    public static void postRotation(final Event event) {
        postRotation(event, AsyncRuntimeExecutor.getAsyncThreadPool());
    }

    /**
     * 给事件总线中丢一个事件
     * 消费者轮训消费
     *
     * @param event
     */
    public static void postRotation(final Event event, ThreadPoolExecutor threadPoolExecutor) {
        if (!isEnable()) {
            return;
        }
        CopyOnWriteArrayList<Subscriber> subscribers = SUBSCRIBER_MAP.get(event.getClass());
        if (!isNotEmpty(subscribers)) {
            return;
        }
        if (LAST_ROTATION_SUBSCRIBER.get() >= subscribers.size()) {
            LAST_ROTATION_SUBSCRIBER.set(0);
        }
        final Subscriber subscriber = subscribers.get(LAST_ROTATION_SUBSCRIBER.get());
        LAST_ROTATION_SUBSCRIBER.incrementAndGet();
        handleSubscriber(subscriber, event, threadPoolExecutor);
    }

    /**
     * 给事件总线中丢一个事件
     * 消费者随机消费
     *
     * @param event
     */
    public static void postRandom(final Event event) {
        postRandom(event, AsyncRuntimeExecutor.getAsyncThreadPool());
    }

    /**
     * 给事件总线中丢一个事件
     * 消费者随机消费
     *
     * @param event
     */
    public static void postRandom(final Event event, ThreadPoolExecutor threadPoolExecutor) {
        if (!isEnable()) {
            return;
        }
        CopyOnWriteArrayList<Subscriber> subscribers = SUBSCRIBER_MAP.get(event.getClass());
        if (!isNotEmpty(subscribers)) {
            return;
        }
        int randomInt = RandomUtil.randomInt(subscribers.size());
        final Subscriber subscriber = subscribers.get(randomInt);
        handleSubscriber(subscriber, event, threadPoolExecutor);
    }

    private static void handleSubscriber(final Subscriber subscriber, final Event event, ThreadPoolExecutor threadPoolExecutor) {
        if (subscriber.isSync()) {
            handleEvent(subscriber, event);
        } else {
            // 异步
            threadPoolExecutor.execute(() -> handleEvent(subscriber, event));
        }
    }

    private static void handleEvent(final Subscriber subscriber, final Event event) {
        try {
            subscriber.onEvent(event);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            log.error("Handle {} error", event.getClass());
        }
    }

}
