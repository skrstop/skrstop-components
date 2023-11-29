package com.zoe.framework.components.util.executor;

import com.zoe.framework.components.core.exception.defined.illegal.IllegalArgumentException;
import lombok.Builder;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Timeout Thread Executor
 *
 * @author 蒋时华
 * @version ${project.version} - 27/11/2018.
 */
public class TimeoutThreadExecutor<T> {

    private Callable<T> task;
    /**
     * 任务执行超时时间
     *
     * <p>默认: 1000毫秒
     */
    private long timeout;

    private String name;

    private boolean timeoutAble;

    @Builder
    public TimeoutThreadExecutor(String name, boolean timeoutAble, long timeout, Callable<T> task) {
        if (name != null && name.trim().length() > 0) {
            this.name = name;
        }
        if (timeout > 0) {
            this.timeout = timeout;
        } else {
            this.timeout = 1000;
        }
        this.timeoutAble = timeoutAble;
        this.task = task;
        if (this.task == null) {
            throw new IllegalArgumentException("任务对象不能为空");
        }
    }

    public T execute() throws TimeoutException, InterruptedException, ExecutionException {

        ExecutorService executorService =
                new ThreadPoolExecutor(
                        1,
                        1,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new DefaultThreadFactory(name),
                        new AbortPolicy());

        Future<T> future =
                executorService.submit(
                        new Callable<T>() {
                            @Override
                            public T call() throws Exception {
                                // check
                                if (!Thread.currentThread().isInterrupted()) {
                                    // execute
                                    return TimeoutThreadExecutor.this.task.call();
                                } else {
                                    throw new InterruptedException(Thread.currentThread().getName() + " 已经被中断执行");
                                }
                            }
                        });
        try {
            if (!timeoutAble) {
                return future.get();
            }
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } finally {
            try {
                executorService.shutdown();
            } catch (Exception ignore) {
            }
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDegrandable() {
        return timeoutAble;
    }

    public void setTimeoutAble(boolean timeoutAble) {
        this.timeoutAble = timeoutAble;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private final AtomicLong threadIndex = new AtomicLong(0);
        private final String threadNamePrefix;

        DefaultThreadFactory(final String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, threadNamePrefix + this.threadIndex.incrementAndGet());
        }
    }
}
