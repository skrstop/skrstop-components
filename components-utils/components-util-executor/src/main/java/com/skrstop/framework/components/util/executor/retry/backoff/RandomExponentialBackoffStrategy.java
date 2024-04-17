package com.skrstop.framework.components.util.executor.retry.backoff;

import com.skrstop.framework.components.util.executor.retry.config.RetryConfig;
import com.skrstop.framework.components.util.executor.retry.exception.InvalidRetryConfigException;

import java.time.Duration;

public class RandomExponentialBackoffStrategy implements BackoffStrategy {

    private RandomBackoffStrategy randomBackoffStrategy;

    private ExponentialBackoffStrategy exponentialBackoffStrategy;

    public RandomExponentialBackoffStrategy() {
        this.randomBackoffStrategy = new RandomBackoffStrategy(10);
        this.exponentialBackoffStrategy = new ExponentialBackoffStrategy();
    }

    @Override
    public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        Duration durationWaitFromExpBackoff =
                exponentialBackoffStrategy.getDurationToWait(numberOfTriesFailed, delayBetweenAttempts);

        return randomBackoffStrategy.getDurationToWait(numberOfTriesFailed, durationWaitFromExpBackoff);
    }

    @Override
    public void validateConfig(RetryConfig config) {
        if (null == config.getDelayBetweenRetries()) {
            throw new InvalidRetryConfigException("Retry config must specify the delay between retries!");
        }
    }
}
