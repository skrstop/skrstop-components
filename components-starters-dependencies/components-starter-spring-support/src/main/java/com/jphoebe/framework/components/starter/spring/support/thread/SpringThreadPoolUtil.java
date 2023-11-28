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
package com.jphoebe.framework.components.starter.spring.support.thread;

import com.jphoebe.framework.components.util.constant.TimeConst;
import com.jphoebe.framework.components.util.executor.ThreadPoolUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * 线程池工具类
 *
 * @author 蒋时华
 */
public class SpringThreadPoolUtil extends ThreadPoolUtil {

    /**
     * 固定大小线程池，无队列
     *
     * @param corePoolSize 初始化线程池
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(int corePoolSize) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(-1);
        return threadPoolTaskExecutor;
    }

    /**
     * 固定大小线程池，自定义线程名前缀，无队列
     *
     * @param corePoolSize     初始化线程池
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(int corePoolSize, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(-1);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 固定大小线程池，自定义队列大小
     *
     * @param corePoolSize  初始化线程池
     * @param queueCapacity 线程池队列大小
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize, int queueCapacity) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        return threadPoolTaskExecutor;
    }

    /**
     * 固定大小线程池，自定义队列大小、线程名线坠
     *
     * @param corePoolSize     初始化线程池
     * @param queueCapacity    线程池队列大小
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize, int queueCapacity, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 固定大小线程池，自定义队列、线程池工厂
     *
     * @param corePoolSize  初始化线程池
     * @param queueCapacity 线程池队列大小
     * @param threadFactory 线程池工厂
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize, int queueCapacity, ThreadFactory threadFactory) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        return threadPoolTaskExecutor;
    }

    /**
     * 固定大小线程池，自定义队列、线程池工厂、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param queueCapacity    线程池队列大小
     * @param threadFactory    线程池工厂
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize, int queueCapacity, ThreadFactory threadFactory, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 固定大小线程池，自定义队列和拒绝策略
     *
     * @param corePoolSize  初始化线程池
     * @param queueCapacity 线程池队列大小
     * @param handler       拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize, int queueCapacity, RejectedExecutionHandler handler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        return threadPoolTaskExecutor;

    }

    /**
     * 固定大小线程池，自定义队列和拒绝策略、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param queueCapacity    线程池队列大小
     * @param handler          拒绝策略
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize, int queueCapacity, RejectedExecutionHandler handler, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;

    }

    /**
     * 固定大小线程池，自定义队列、线程池工厂、拒绝策略
     *
     * @param corePoolSize  初始化线程池
     * @param queueCapacity 线程池队列大小
     * @param threadFactory 线程池工厂
     * @param handler       拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize,
            int queueCapacity,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        return threadPoolTaskExecutor;

    }

    /**
     * 固定大小线程池，自定义队列、线程池工厂、拒绝策略、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param queueCapacity    线程池队列大小
     * @param threadFactory    线程池工厂
     * @param handler          拒绝策略
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newFixedThreadSpringPool(
            int corePoolSize,
            int queueCapacity,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(0);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;

    }

    /**
     * 缓冲线程池（1分钟无调用销毁），无队列
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(int corePoolSize, int maximumPoolSize) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(-1);
        return threadPoolTaskExecutor;

    }

    /**
     * 缓冲线程池（1分钟无调用销毁），无队列、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param maximumPoolSize  最大线程池
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(int corePoolSize, int maximumPoolSize, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(-1);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;

    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queueCapacity   线程池队列大小
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize, int maximumPoolSize, int queueCapacity) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param maximumPoolSize  最大线程池
     * @param queueCapacity    线程池队列大小
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize, int maximumPoolSize, int queueCapacity, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queueCapacity   线程池队列大小
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize, int maximumPoolSize, int queueCapacity, RejectedExecutionHandler handler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和拒绝策略、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param maximumPoolSize  最大线程池
     * @param queueCapacity    线程池队列大小
     * @param handler          拒绝策略
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize, int maximumPoolSize, int queueCapacity, RejectedExecutionHandler handler, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和线程池工厂
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queueCapacity   线程池队列大小
     * @param threadFactory   线程池工厂
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize,
            int maximumPoolSize,
            int queueCapacity,
            ThreadFactory threadFactory) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和线程池工厂、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param maximumPoolSize  最大线程池
     * @param queueCapacity    线程池队列大小
     * @param threadFactory    线程池工厂
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize,
            int maximumPoolSize,
            int queueCapacity,
            ThreadFactory threadFactory, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queueCapacity   线程池队列大小
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize,
            int maximumPoolSize,
            int queueCapacity,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程池工厂和拒绝策略、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param maximumPoolSize  最大线程池
     * @param queueCapacity    线程池队列大小
     * @param threadFactory    线程池工厂
     * @param handler          拒绝策略
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize,
            int maximumPoolSize,
            int queueCapacity,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(TimeConst.SECONDS_PER_MINUTE);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池，自定义空闲时间(秒)、自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param keepAliveTime   回收时间
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize,
            int maximumPoolSize,
            int keepAliveTime,
            int queueCapacity,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveTime);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        return threadPoolTaskExecutor;
    }

    /**
     * 缓冲线程池，自定义空闲时间(秒)、自定义队列、线程池工厂、拒绝策略、线程名前缀
     *
     * @param corePoolSize     初始化线程池
     * @param maximumPoolSize  最大线程池
     * @param keepAliveTime    回收时间
     * @param threadFactory    线程池工厂
     * @param handler          拒绝策略
     * @param threadNamePrefix 线程名前缀
     * @return the thread pool executor
     */
    public static ThreadPoolTaskExecutor newCachedThreadSpringPool(
            int corePoolSize,
            int maximumPoolSize,
            int keepAliveTime,
            int queueCapacity,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler, String threadNamePrefix) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveTime);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadFactory(threadFactory);
        threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        return threadPoolTaskExecutor;
    }

}
