package com.zoe.framework.components.util.executor.retry;

import com.zoe.framework.components.util.executor.retry.config.RetryConfig;
import com.zoe.framework.components.util.executor.retry.exception.RetriesExhaustedException;
import com.zoe.framework.components.util.executor.retry.exception.UnexpectedException;
import com.zoe.framework.components.util.executor.retry.listener.RetryListener;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation that does a single, synchronous retry in the same thread that it is called
 * from.
 *
 * @param <T> The type that is returned by the Callable (eg: Boolean, Void, Object, etc)
 */
@Slf4j
public class CallExecutor<T> implements RetryExecutor<T, Status<T>> {

    private RetryConfig config;

    private RetryListener<T> afterFailedTryListener;

    private RetryListener<T> beforeNextTryListener;

    private RetryListener<T> onFailureListener;

    private RetryListener<T> onSuccessListener;

    private RetryListener<T> onCompletionListener;

    private Exception lastKnownExceptionThatCausedRetry;

    private Status<T> status = new Status<>();

    /**
     * Use {@link CallExecutorBuilder} to build {@link CallExecutor}
     */
    CallExecutor(
            RetryConfig config,
            RetryListener<T> afterFailedTryListener,
            RetryListener<T> beforeNextTryListener,
            RetryListener<T> onFailureListener,
            RetryListener<T> onSuccessListener,
            RetryListener<T> onCompletionListener) {
        this.config = config;
        this.afterFailedTryListener = afterFailedTryListener;
        this.beforeNextTryListener = beforeNextTryListener;
        this.onFailureListener = onFailureListener;
        this.onSuccessListener = onSuccessListener;
        this.onCompletionListener = onCompletionListener;
        this.status.setId(UUID.randomUUID().toString());
    }

    @Override
    public Status<T> execute(Callable<T> callable) {
        return execute(callable, null);
    }

    @Override
    public Status<T> execute(Callable<T> callable, String callName) {
        log.trace("Starting retry4j execution with callable {}", config, callable);
        log.debug("Starting retry4j execution with executor state {}", this);

        long start = System.currentTimeMillis();
        status.setStartTime(start);

        int maxTries = config.getMaxNumberOfTries();
        long millisBetweenTries =
                config.getDelayBetweenRetries() != null ? config.getDelayBetweenRetries().toMillis() : 0L;
        this.status.setCallName(callName);

        AttemptStatus<T> attemptStatus = new AttemptStatus<>();
        attemptStatus.setSuccessful(false);

        int tries;

        try {
            for (tries = 0; tries < maxTries && !attemptStatus.wasSuccessful(); tries++) {
                if (tries > 0) {
                    handleBeforeNextTry(millisBetweenTries, tries);
                    log.trace("Retry4j retrying for time number {}", tries);
                }

                log.trace("Retry4j executing callable {}", callable);
                attemptStatus = tryCall(callable);

                if (!attemptStatus.wasSuccessful()) {
                    handleFailedTry(tries + 1);
                }
            }

            refreshRetryStatus(attemptStatus.wasSuccessful(), tries);
            status.setEndTime(System.currentTimeMillis());

            postExecutionCleanup(callable, maxTries, attemptStatus);

            log.debug(
                    "Finished retry4j execution in {} ms", status.getTotalElapsedDuration().toMillis());
            log.trace("Finished retry4j execution with executor state {}", this);
        } finally {
            if (null != onCompletionListener) {
                onCompletionListener.onEvent(status);
            }
        }

        return status;
    }

    private void postExecutionCleanup(
            Callable<T> callable, int maxTries, AttemptStatus<T> attemptStatus) {
        if (!attemptStatus.wasSuccessful()) {
            String failureMsg =
                    String.format("Call '%s' failed after %d tries!", callable.toString(), maxTries);
            if (null != onFailureListener) {
                onFailureListener.onEvent(status);
            } else {
                log.trace("Throwing retries exhausted exception");
                throw new RetriesExhaustedException(failureMsg, lastKnownExceptionThatCausedRetry, status);
            }
        } else {
            status.setResult(attemptStatus.getResult());
            if (null != onSuccessListener) {
                onSuccessListener.onEvent(status);
            }
        }
    }

    private AttemptStatus<T> tryCall(Callable<T> callable) throws UnexpectedException {
        AttemptStatus attemptStatus = new AttemptStatus();

        try {
            T callResult = callable.call();

            boolean shouldRetryOnThisResult =
                    config.shouldRetryOnValue()
                            && ((config.getValuesToExpect() != null && !isOneOfValuesToExpect(callResult))
                            || isOneOfValuesToRetryOn(callResult));
            if (shouldRetryOnThisResult) {
                attemptStatus.setSuccessful(false);
            } else {
                attemptStatus.setResult(callResult);
                attemptStatus.setSuccessful(true);
            }
        } catch (Exception e) {
            if (shouldThrowException(e)) {
                log.trace("Throwing expected exception {}", e);
                throw new UnexpectedException("Unexpected exception thrown during retry execution!", e);
            } else {
                lastKnownExceptionThatCausedRetry = e;
                attemptStatus.setSuccessful(false);
            }
        }

        return attemptStatus;
    }

    private boolean isOneOfValuesToExpect(T callResult) {
        Collection<Object> valuesToExpect = config.getValuesToExpect();
        if (valuesToExpect != null) {
            for (Object o : valuesToExpect) {
                if (o.equals(callResult)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOneOfValuesToRetryOn(T callResult) {
        Collection<Object> valuesToRetryOn = config.getValuesToRetryOn();
        if (valuesToRetryOn != null) {
            for (Object o : valuesToRetryOn) {
                if (o.equals(callResult)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleBeforeNextTry(final long millisBetweenTries, final int tries) {
        sleep(millisBetweenTries, tries);
        if (null != beforeNextTryListener) {
            beforeNextTryListener.onEvent(status);
        }
    }

    private void handleFailedTry(int tries) {
        refreshRetryStatus(false, tries);

        if (null != afterFailedTryListener) {
            afterFailedTryListener.onEvent(status);
        }
    }

    private void refreshRetryStatus(boolean success, int tries) {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - status.getStartTime();

        status.setTotalTries(tries);
        status.setTotalElapsedDuration(Duration.of(elapsed, ChronoUnit.MILLIS));
        status.setSuccessful(success);
        status.setLastExceptionThatCausedRetry(lastKnownExceptionThatCausedRetry);
    }

    private void sleep(long millis, int tries) {
        Duration duration = Duration.of(millis, ChronoUnit.MILLIS);
        long millisToSleep = config.getBackoffStrategy().getDurationToWait(tries, duration).toMillis();

        log.trace("Retry4j executor sleeping for {} ms", millisToSleep);
        try {
            TimeUnit.MILLISECONDS.sleep(millisToSleep);
        } catch (InterruptedException ignored) {
        }
    }

    private boolean shouldThrowException(Exception e) {
        if (this.config.getCustomRetryOnLogic() != null) {
            // custom retry logic
            return !this.config.getCustomRetryOnLogic().apply(e);
        } else {
            // config says to always retry
            if (this.config.isRetryOnAnyException()) {
                return false;
            }

            Set<Class<?>> exceptionsToMatch = new HashSet<>();
            exceptionsToMatch.add(e.getClass());
            if (this.config.shouldRetryOnCausedBy()) {
                exceptionsToMatch.clear();
                exceptionsToMatch.addAll(getExceptionCauses(e));
            }

            return !exceptionsToMatch.stream().anyMatch(ex -> matchesException(ex));
        }
    }

    private boolean matchesException(Class<?> thrownExceptionClass) {
        // config says to retry only on specific exceptions
        for (Class<? extends Exception> exceptionToRetryOn :
                this.config.getRetryOnSpecificExceptions()) {
            if (exceptionToRetryOn.isAssignableFrom(thrownExceptionClass)) {
                return true;
            }
        }

        // config says to retry on all except specific exceptions
        if (!this.config.getRetryOnAnyExceptionExcluding().isEmpty()) {
            for (Class<? extends Exception> exceptionToNotRetryOn :
                    this.config.getRetryOnAnyExceptionExcluding()) {
                if (exceptionToNotRetryOn.isAssignableFrom(thrownExceptionClass)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Set<Class<?>> getExceptionCauses(Exception exception) {
        Throwable parent = exception;
        Set<Class<?>> causes = new HashSet<>();
        while (parent.getCause() != null) {
            causes.add(parent.getCause().getClass());
            parent = parent.getCause();
        }
        return causes;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallExecutor{");
        sb.append("config=").append(config);
        sb.append(", afterFailedTryListener=").append(afterFailedTryListener);
        sb.append(", beforeNextTryListener=").append(beforeNextTryListener);
        sb.append(", onFailureListener=").append(onFailureListener);
        sb.append(", onSuccessListener=").append(onSuccessListener);
        sb.append(", lastKnownExceptionThatCausedRetry=").append(lastKnownExceptionThatCausedRetry);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
