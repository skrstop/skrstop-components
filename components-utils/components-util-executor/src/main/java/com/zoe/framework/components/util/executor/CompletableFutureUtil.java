package com.zoe.framework.components.util.executor;

import com.zoe.framework.components.core.exception.util.ThrowableStackTraceUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * CompletableFutureService class
 *
 * @author 蒋时华
 * @date 2018/11/26
 */
@Slf4j
@Builder
public class CompletableFutureUtil {

    private Executor executor;
    private final int defaultCorePoolSize = 5;
    private final int defaultMaximumPoolSize = 20;
    private final int defaultQueueSize = 20;

    public CompletableFutureUtil(Executor executor) {
        this.executor = executor;
    }

    public CompletableFutureUtil() {
        this.executor = ThreadPoolUtil.newCachedThreadPool(defaultCorePoolSize, defaultMaximumPoolSize, ThreadPoolUtil.buildQueue(defaultQueueSize));
    }

    /**
     * 没有返回值
     *
     * @param runnable
     * @return
     */
    public CompletableFuture run(Runnable runnable) {
        CompletableFuture completableFuture = CompletableFuture.runAsync(runnable, executor);
        return completableFuture;
    }

    /**
     * 等待执行结束
     *
     * @param completableFuture
     * @throws Throwable
     */
    public void get(CompletableFuture completableFuture) throws Throwable {
        try {
            completableFuture.get();
        } catch (Exception e) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
            throw e.getCause();
        }
    }

    /**
     * 有返回值
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(supplier, executor);
        return completableFuture;
    }

    /**
     * 等待执行完毕，并且返回值
     *
     * @param completableFuture
     * @param <T>
     * @return
     * @throws Throwable
     */
    public <T> T accept(CompletableFuture<T> completableFuture) throws Throwable {
        try {
            return completableFuture.get();
        } catch (Exception e) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
            throw e.getCause();
        }
    }

    /**
     * 同步处理返回值
     *
     * @param completableFuture
     * @param action
     * @param <T>
     * @throws Throwable
     */
    public <T> void thenAccept(CompletableFuture<T> completableFuture, Consumer<T> action) throws Throwable {
        try {
            completableFuture.thenAccept(action);
        } catch (Exception e) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
            throw e.getCause();
        }
    }

    /**
     * 异步处理返回值
     *
     * @param completableFuture
     * @param action
     * @param <T>
     * @throws Throwable
     */
    public <T> void thenAcceptSync(CompletableFuture<T> completableFuture, Consumer<T> action) throws Throwable {
        try {
            completableFuture.thenAcceptAsync(action, executor);
        } catch (Exception e) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
            throw e.getCause();
        }
    }


}
