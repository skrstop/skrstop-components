package com.zoe.framework.components.util.executor.retry.backoff;

import java.time.Duration;

public class NoWaitBackoffStrategy implements BackoffStrategy {

    @Override
    public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        return Duration.ZERO;
    }
}
