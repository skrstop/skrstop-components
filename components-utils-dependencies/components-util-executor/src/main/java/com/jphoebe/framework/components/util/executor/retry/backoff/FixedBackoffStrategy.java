package com.jphoebe.framework.components.util.executor.retry.backoff;

import com.jphoebe.framework.components.util.executor.retry.config.RetryConfig;
import com.jphoebe.framework.components.util.executor.retry.exception.InvalidRetryConfigException;

import java.time.Duration;

public class FixedBackoffStrategy implements BackoffStrategy {

    @Override
    public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        return delayBetweenAttempts;
    }

    @Override
    public void validateConfig(RetryConfig config) {
        if (null == config.getDelayBetweenRetries()) {
            throw new InvalidRetryConfigException("Retry config must specify the delay between retries!");
        }
    }
}
