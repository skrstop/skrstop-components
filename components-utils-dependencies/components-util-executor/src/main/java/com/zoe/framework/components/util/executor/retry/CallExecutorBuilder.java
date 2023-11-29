package com.zoe.framework.components.util.executor.retry;

import com.zoe.framework.components.util.executor.retry.config.RetryConfig;
import com.zoe.framework.components.util.executor.retry.config.RetryConfigBuilder;
import com.zoe.framework.components.util.executor.retry.listener.RetryListener;

import java.util.concurrent.ExecutorService;

public class CallExecutorBuilder<T> {

    public static final RetryConfig DEFAULT_RETRY_CONFIG =
            new RetryConfigBuilder().fixedBackoff5Tries10Sec().build();
    private RetryConfig retryConfig = DEFAULT_RETRY_CONFIG;
    private RetryListener<T> afterFailedTryListener;
    private RetryListener<T> beforeNextTryListener;
    private RetryListener<T> onSuccessListener;
    private RetryListener<T> onFailureListener;
    private RetryListener<T> onCompletionListener;

    public CallExecutorBuilder() {
    }

    public CallExecutorBuilder<T> config(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
        return this;
    }

    public CallExecutorBuilder<T> onCompletionListener(RetryListener<T> listener) {
        this.onCompletionListener = listener;
        return this;
    }

    public CallExecutorBuilder<T> onSuccessListener(RetryListener<T> listener) {
        this.onSuccessListener = listener;
        return this;
    }

    public CallExecutorBuilder<T> onFailureListener(RetryListener<T> listener) {
        this.onFailureListener = listener;
        return this;
    }

    public CallExecutorBuilder<T> beforeNextTryListener(RetryListener<T> listener) {
        this.beforeNextTryListener = listener;
        return this;
    }

    public CallExecutorBuilder<T> afterFailedTryListener(RetryListener<T> listener) {
        this.afterFailedTryListener = listener;
        return this;
    }

    public CallExecutor<T> build() {
        return new CallExecutor<>(
                retryConfig,
                afterFailedTryListener,
                beforeNextTryListener,
                onFailureListener,
                onSuccessListener,
                onCompletionListener);
    }

    public AsyncCallExecutor<T> buildAsync() {
        return new AsyncCallExecutor<>(
                retryConfig,
                null,
                afterFailedTryListener,
                beforeNextTryListener,
                onFailureListener,
                onSuccessListener,
                onCompletionListener);
    }

    public AsyncCallExecutor<T> buildAsync(ExecutorService executorService) {
        return new AsyncCallExecutor<>(
                retryConfig,
                executorService,
                afterFailedTryListener,
                beforeNextTryListener,
                onFailureListener,
                onSuccessListener,
                onCompletionListener);
    }
}
