package com.zoe.framework.components.util.executor.retry.backoff;


import com.zoe.framework.components.util.executor.retry.config.RetryConfig;

import java.time.Duration;

public interface BackoffStrategy {

    Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts);

    default void validateConfig(RetryConfig config) {
    }
}
