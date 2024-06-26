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
package com.skrstop.framework.components.util.executor;

import cn.hutool.core.thread.ThreadUtil;
import com.skrstop.framework.components.util.constant.TimeConst;

import java.util.concurrent.*;

/**
 * 线程池工具类
 *
 * @author 蒋时华
 */
public class ThreadPoolUtil extends ThreadUtil {

    /**
     * 普通任务优先级，默认0
     */
    public static int THREAD_PRIORITY_NORMAL = 0;

    /**
     * 高任务优先级，默认10
     */
    public static int THREAD_PRIORITY_HIGH = 10;

    /**
     * 低任务优先级，默认-10
     */
    public static int THREAD_PRIORITY_LOW = -10;

    /**
     * 固定大小线程池，无队列
     *
     * @param corePoolSize 初始化线程池
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize
                , corePoolSize
                , 0
                , TimeUnit.MILLISECONDS
                , new SynchronousQueue<Runnable>()
        );
    }

    /**
     * 固定大小线程池，自定义队列
     *
     * @param corePoolSize 初始化线程池
     * @param queue        线程池队列
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(
            int corePoolSize, BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(
                corePoolSize
                , corePoolSize
                , 0
                , TimeUnit.MILLISECONDS
                , queue
        );
    }

    /**
     * 固定大小线程池，自定义队列和线程池工厂
     *
     * @param corePoolSize  初始化线程池
     * @param queue         线程池队列
     * @param threadFactory 线程池工厂
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(
            int corePoolSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                corePoolSize, corePoolSize, 0, TimeUnit.MILLISECONDS, queue, threadFactory);
    }

    /**
     * 固定大小线程池，自定义队列和拒绝策略
     *
     * @param corePoolSize 初始化线程池
     * @param queue        线程池队列
     * @param handler      拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(
            int corePoolSize, BlockingQueue<Runnable> queue, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize, corePoolSize, 0, TimeUnit.MILLISECONDS, queue, handler);
    }

    /**
     * 固定大小线程池，自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize  初始化线程
     * @param queue         线程池队列
     * @param threadFactory 线程池工厂
     * @param handler       拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(
            int corePoolSize,
            BlockingQueue<Runnable> queue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize, corePoolSize, 0, TimeUnit.MILLISECONDS, queue, threadFactory, handler);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），无队列
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                TimeConst.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>());
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, TimeConst.MILLISECONDS_PER_MINUTE, TimeUnit.MILLISECONDS, queue);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> queue, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, TimeConst.MILLISECONDS_PER_MINUTE, TimeUnit.MILLISECONDS, queue, handler);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和线程池工厂
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            BlockingQueue<Runnable> queue,
            ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                TimeConst.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            BlockingQueue<Runnable> queue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                TimeConst.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize  初始化线程
     * @param queue         线程池队列
     * @param threadFactory 线程池工厂
     * @param handler       拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize,
            BlockingQueue<Runnable> queue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize,
                TimeConst.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 缓冲线程池，自定义空闲时间(毫秒)、自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param keepAliveTime   回收时间
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            int keepAliveTime,
            BlockingQueue<Runnable> queue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 缓冲线程池，自定义空闲时间、自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param keepAliveTime   回收时间
     * @param timeUnit        回收时间单位
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            int keepAliveTime,
            TimeUnit timeUnit,
            BlockingQueue<Runnable> queue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 构建队列
     *
     * @param size 队列大小
     * @return 队列
     */
    public static BlockingQueue<Runnable> buildQueue(int size) {
        return buildQueue(size, false);
    }

    /**
     * 构建队列
     *
     * @param size       队列大小
     * @param isPriority 是否优先级队列
     * @return 队列
     */
    public static BlockingQueue<Runnable> buildQueue(int size, boolean isPriority) {
        BlockingQueue<Runnable> queue;
        if (size == 0) {
            // 默认无队列
            queue = new SynchronousQueue<Runnable>();
        } else {
            // 有限队列或无限队列
            if (isPriority) {
                queue =
                        size < 0
                                ? new PriorityBlockingQueue<Runnable>()
                                : new PriorityBlockingQueue<Runnable>(size);
            } else {
                queue =
                        size < 0
                                ? new LinkedBlockingQueue<Runnable>()
                                : new LinkedBlockingQueue<Runnable>(size);
            }
        }
        return queue;
    }

    /**
     * 拒绝策略：抛异常拒绝 {@link RejectedExecutionException}
     *
     * @return
     */
    public static RejectedExecutionHandler policyAbort() {
        return new ThreadPoolExecutor.AbortPolicy();
    }

    /**
     * 拒绝策略：直接丢弃，不抛异常
     *
     * @return
     */
    public static RejectedExecutionHandler policyDiscard() {
        return new ThreadPoolExecutor.DiscardPolicy();
    }

    /**
     * 拒绝策略：丢弃老的，不抛异常
     *
     * @return
     */
    public static RejectedExecutionHandler policyDiscardOldest() {
        return new ThreadPoolExecutor.DiscardOldestPolicy();
    }

    /**
     * 拒绝策略：如果主线程还存在，直接在主线程中运行，否则丢弃
     *
     * @return
     */
    public static RejectedExecutionHandler policyCallerRuns() {
        return new ThreadPoolExecutor.CallerRunsPolicy();
    }

}
