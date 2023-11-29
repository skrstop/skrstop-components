package com.zoe.framework.components.util.executor.retry.backoff;

import com.zoe.framework.components.util.executor.retry.config.RetryConfig;
import com.zoe.framework.components.util.executor.retry.exception.InvalidRetryConfigException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FibonacciBackoffStrategy implements BackoffStrategy {

    public static final int MAX_NUM_OF_FIB_NUMBERS = 25;
    private List<Integer> fibonacciNumbers;

    public FibonacciBackoffStrategy() {
        fibonacciNumbers = new ArrayList<>();

        fibonacciNumbers.add(0);
        fibonacciNumbers.add(1);

        for (int i = 0; i < MAX_NUM_OF_FIB_NUMBERS; i++) {
            int nextFibNum = fibonacciNumbers.get(i) + fibonacciNumbers.get(i + 1);
            fibonacciNumbers.add(nextFibNum);
        }
    }

    @Override
    public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        int fibNumber;
        try {
            fibNumber = fibonacciNumbers.get(numberOfTriesFailed);
        } catch (IndexOutOfBoundsException e) {
            fibNumber = fibonacciNumbers.get(MAX_NUM_OF_FIB_NUMBERS - 1);
        }
        return Duration.ofMillis(delayBetweenAttempts.toMillis() * fibNumber);
    }

    @Override
    public void validateConfig(RetryConfig config) {
        if (null == config.getDelayBetweenRetries()) {
            throw new InvalidRetryConfigException("Retry config must specify the delay between retries!");
        }
    }

    public List<Integer> getFibonacciNumbers() {
        return fibonacciNumbers;
    }
}
