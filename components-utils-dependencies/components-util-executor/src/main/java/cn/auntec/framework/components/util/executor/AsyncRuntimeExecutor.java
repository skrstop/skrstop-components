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
package cn.auntec.framework.components.util.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步执行运行时
 *
 * @author 蒋时华
 */
@Slf4j
public class AsyncRuntimeExecutor {

    /**
     * 默认回调线程池最小
     */
    public static final String ASYNC_POOL_CORE = "async.pool.core";

    public static final int ASYNC_POOL_CORE_VALUE = 10;
    /**
     * 默认回调线程池最大
     */
    public static final String ASYNC_POOL_MAX = "async.pool.max";

    public static final int ASYNC_POOL_MAX_VALUE = 200;
    /**
     * 默认回调线程池队列
     */
    public static final String ASYNC_POOL_QUEUE = "async.pool.queue";

    public static final int ASYNC_POOL_QUEUE_VALUE = 256;
    /**
     * 默认回调线程池回收时间
     */
    public static final String ASYNC_POOL_TIME = "async.pool.time";

    public static final int ASYNC_POOL_TIME_VALUE = 60000;
    /**
     * callback业务线程池（callback+async）
     */
    private static volatile ThreadPoolExecutor asyncThreadPool;

    /**
     * 得到callback用的线程池 默认开始创建
     *
     * @return callback用的线程池
     */
    public static ThreadPoolExecutor getAsyncThreadPool() {
        return getAsyncThreadPool(true);
    }

    /**
     * 得到callback用的线程池
     *
     * @param build 没有时是否构建
     * @return callback用的线程池
     */
    public static ThreadPoolExecutor getAsyncThreadPool(boolean build) {
        if (asyncThreadPool == null && build) {
            synchronized (AsyncRuntimeExecutor.class) {
                if (asyncThreadPool == null && build) {
                    // 一些系统参数，可以从配置或者注册中心获取。
                    int coresize = getIntValue(ASYNC_POOL_CORE, ASYNC_POOL_CORE_VALUE);
                    int maxsize = getIntValue(ASYNC_POOL_MAX, ASYNC_POOL_MAX_VALUE);
                    int queuesize = getIntValue(ASYNC_POOL_QUEUE, ASYNC_POOL_QUEUE_VALUE);
                    int keepAliveTime = getIntValue(ASYNC_POOL_TIME, ASYNC_POOL_TIME_VALUE);

                    BlockingQueue<Runnable> queue = ThreadPoolUtil.buildQueue(queuesize);
                    NamedThreadFactory threadFactory = new NamedThreadFactory("ACMEDCARE-CB", true);

                    RejectedExecutionHandler handler =
                            new RejectedExecutionHandler() {
                                private int i = 1;

                                @Override
                                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                    if (i++ % 7 == 0) {
                                        i = 1;
                                        if (log.isWarnEnabled()) {
                                            log.warn(
                                                    "Task:{} has been reject because of threadPool exhausted!"
                                                            + " pool:{}, active:{}, queue:{}, taskcnt: {}",
                                                    r,
                                                    executor.getPoolSize(),
                                                    executor.getActiveCount(),
                                                    executor.getQueue().size(),
                                                    executor.getTaskCount());

                                        }
                                    }
                                    throw new RejectedExecutionException(
                                            "Callback handler thread pool has bean exhausted");
                                }
                            };
                    asyncThreadPool =
                            ThreadPoolUtil.newCachedThreadPool(
                                    coresize, maxsize, keepAliveTime, queue, threadFactory, handler);
                }
            }
        }
        return asyncThreadPool;
    }

    private static int getIntValue(String key, int defaultValue) {
        return Integer.parseInt(System.getProperty(key, String.valueOf(defaultValue)));
    }

    /**
     * Common NamedThreadFactory
     *
     * @author 蒋时华
     */
    private static class NamedThreadFactory implements ThreadFactory {

        /**
         * 系统全局线程池计数器
         */
        private static final AtomicInteger POOL_COUNT = new AtomicInteger();

        /**
         * 当前线程池计数器
         */
        final AtomicInteger threadCount = new AtomicInteger(1);
        /**
         * 线程组
         */
        private final ThreadGroup group;
        /**
         * 线程名前缀
         */
        private final String namePrefix;
        /**
         * 是否守护线程，true的话随主线程退出而退出，false的话则要主动退出
         */
        private final boolean isDaemon;
        /**
         * 线程名第一前缀
         */
        private final String firstPrefix = "Acmedcare-";

        /**
         * 构造函数，默认非守护线程
         *
         * @param secondPrefix 第二前缀，前面会自动加上第一前缀，后面会自动加上-T-
         */
        public NamedThreadFactory(String secondPrefix) {
            this(secondPrefix, false);
        }

        /**
         * 构造函数
         *
         * @param secondPrefix 第二前缀，前面会自动加上第一前缀，后面会自动加上-T-
         * @param daemon       是否守护线程，true的话随主线程退出而退出，false的话则要主动退出
         */
        public NamedThreadFactory(String secondPrefix, boolean daemon) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = firstPrefix + secondPrefix + "-" + POOL_COUNT.getAndIncrement() + "-T";
            isDaemon = daemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadCount.getAndIncrement(), 0);
            t.setDaemon(isDaemon);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
