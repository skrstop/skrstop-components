package com.skrstop.framework.components.util.executor.retry;

import com.skrstop.framework.components.util.executor.retry.config.RetryConfig;
import com.skrstop.framework.components.util.executor.retry.listener.RetryListener;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Implementation that kicks off each retry request in its own separate thread that does not block
 * the thread the execution is called from. If you provide an ExecutorService, it will be used when
 * creating threads.
 *
 * @param <T> The type that is returned by the Callable (eg: Boolean, Void, Object, etc)
 */
public class AsyncCallExecutor<T> implements RetryExecutor<T, CompletableFuture<Status<T>>> {

    private RetryConfig config;

    private ExecutorService executorService;

    private RetryListener<T> afterFailedTryListener;

    private RetryListener<T> beforeNextTryListener;

    private RetryListener<T> onFailureListener;

    private RetryListener<T> onSuccessListener;

    private RetryListener<T> onCompletionListener;

    /**
     * Use {@link CallExecutorBuilder} to build AsyncCallExecutor
     */
    AsyncCallExecutor(
            RetryConfig config,
            ExecutorService executorService,
            RetryListener<T> afterFailedTryListener,
            RetryListener<T> beforeNextTryListener,
            RetryListener<T> onFailureListener,
            RetryListener<T> onSuccessListener,
            RetryListener<T> onCompletionListener) {
        this.config = config;
        this.executorService = executorService;
        this.afterFailedTryListener = afterFailedTryListener;
        this.beforeNextTryListener = beforeNextTryListener;
        this.onFailureListener = onFailureListener;
        this.onSuccessListener = onSuccessListener;
        this.onCompletionListener = onCompletionListener;
    }

    @Override
    public CompletableFuture<Status<T>> execute(Callable<T> callable) {
        return execute(callable, null);
    }

    @Override
    public CompletableFuture<Status<T>> execute(Callable<T> callable, String callName) {
        CallExecutor<T> synchronousCallExecutor =
                new CallExecutor<>(
                        config,
                        afterFailedTryListener,
                        beforeNextTryListener,
                        onFailureListener,
                        onSuccessListener,
                        onCompletionListener);

        CompletableFuture<Status<T>> completableFuture = new CompletableFuture<>();

        if (executorService != null) {
            executorService.submit(
                    () -> executeFuture(callable, callName, synchronousCallExecutor, completableFuture));
        } else {
            (new Thread(
                    () -> executeFuture(callable, callName, synchronousCallExecutor, completableFuture)))
                    .start();
        }

        return completableFuture;
    }

    private void executeFuture(
            Callable<T> callable,
            String callName,
            CallExecutor<T> synchronousCallExecutor,
            CompletableFuture<Status<T>> completableFuture) {
        try {
            Status<T> status = synchronousCallExecutor.execute(callable, callName);
            completableFuture.complete(status);
        } catch (Throwable t) {
            completableFuture.completeExceptionally(t);
        }
    }

    public RetryConfig getConfig() {
        return config;
    }

    public RetryListener<T> getAfterFailedTryListener() {
        return afterFailedTryListener;
    }

    public RetryListener<T> getBeforeNextTryListener() {
        return beforeNextTryListener;
    }

    public RetryListener<T> getOnFailureListener() {
        return onFailureListener;
    }

    public RetryListener<T> getOnSuccessListener() {
        return onSuccessListener;
    }

    public RetryListener<T> getOnCompletionListener() {
        return onCompletionListener;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Deprecated
    public ExecutorService getThreadExecutorService() {
        return executorService;
    }
}
