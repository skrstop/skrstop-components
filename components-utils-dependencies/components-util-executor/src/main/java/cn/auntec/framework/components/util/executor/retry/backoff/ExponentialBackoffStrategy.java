package cn.auntec.framework.components.util.executor.retry.backoff;

import cn.auntec.framework.components.util.executor.retry.config.RetryConfig;
import cn.auntec.framework.components.util.executor.retry.exception.InvalidRetryConfigException;

import java.time.Duration;

/**
 * AKA binary exponential backoff
 */
public class ExponentialBackoffStrategy implements BackoffStrategy {

    @Override
    public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        double exponentialMultiplier = Math.pow(2.0, numberOfTriesFailed - 1);
        double result = exponentialMultiplier * delayBetweenAttempts.toMillis();
        long millisToWait = (long) Math.min(result, Long.MAX_VALUE);
        return Duration.ofMillis(millisToWait);
    }

    @Override
    public void validateConfig(RetryConfig config) {
        if (null == config.getDelayBetweenRetries()) {
            throw new InvalidRetryConfigException("Retry config must specify the delay between retries!");
        }
    }
}
