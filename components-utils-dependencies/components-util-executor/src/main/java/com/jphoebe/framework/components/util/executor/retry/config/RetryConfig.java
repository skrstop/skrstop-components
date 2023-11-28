package com.jphoebe.framework.components.util.executor.retry.config;


import com.jphoebe.framework.components.util.executor.retry.backoff.BackoffStrategy;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class RetryConfig {

    private final boolean retryOnAnyException;
    private final Set<Class<? extends Exception>> retryOnSpecificExceptions;
    private final Set<Class<? extends Exception>> retryOnAnyExceptionExcluding;
    private final Integer maxNumberOfTries;
    private final Duration delayBetweenRetries;
    private final BackoffStrategy backoffStrategy;
    private final Collection<Object> valuesToRetryOn;
    private final Collection<Object> valuesToExpect;
    private final boolean retryOnValue;
    private final Function<Exception, Boolean> customRetryOnLogic;
    private final boolean retryOnCausedBy;

    RetryConfig(
            boolean retryOnAnyException,
            Set<Class<? extends Exception>> retryOnSpecificExceptions,
            Set<Class<? extends Exception>> retryOnAnyExceptionExcluding,
            Integer maxNumberOfTries,
            Duration delayBetweenRetries,
            BackoffStrategy backoffStrategy,
            Collection<Object> valuesToRetryOn,
            Collection<Object> valuesToExpect,
            boolean retryOnValue,
            Function<Exception, Boolean> customRetryOnLogic,
            boolean retryOnCausedBy) {
        this.retryOnAnyException = retryOnAnyException;
        this.retryOnSpecificExceptions = retryOnSpecificExceptions;
        this.retryOnAnyExceptionExcluding = retryOnAnyExceptionExcluding;
        this.maxNumberOfTries = maxNumberOfTries;
        this.delayBetweenRetries = delayBetweenRetries;
        this.backoffStrategy = backoffStrategy;
        this.valuesToRetryOn = valuesToRetryOn;
        this.valuesToExpect = valuesToExpect;
        this.retryOnValue = retryOnValue;
        this.customRetryOnLogic = customRetryOnLogic;
        this.retryOnCausedBy = retryOnCausedBy;
    }

    public Collection<Object> getValuesToRetryOn() {
        return valuesToRetryOn;
    }

    public Collection<Object> getValuesToExpect() {
        return valuesToExpect;
    }

    public boolean shouldRetryOnValue() {
        return retryOnValue;
    }

    public boolean isRetryOnAnyException() {
        return retryOnAnyException;
    }

    public Set<Class<? extends Exception>> getRetryOnSpecificExceptions() {
        return retryOnSpecificExceptions;
    }

    public Set<Class<? extends Exception>> getRetryOnAnyExceptionExcluding() {
        return retryOnAnyExceptionExcluding;
    }

    public boolean shouldRetryOnCausedBy() {
        return retryOnCausedBy;
    }

    public Integer getMaxNumberOfTries() {
        return maxNumberOfTries;
    }

    public BackoffStrategy getBackoffStrategy() {
        return backoffStrategy;
    }

    public Function<Exception, Boolean> getCustomRetryOnLogic() {
        return customRetryOnLogic;
    }

    public Duration getDelayBetweenRetries() {
        return delayBetweenRetries;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RetryConfig{");
        sb.append("retryOnAnyException=").append(retryOnAnyException);
        sb.append(", retryOnSpecificExceptions=").append(retryOnSpecificExceptions);
        sb.append(", retryOnAnyExceptionExcluding=").append(retryOnAnyExceptionExcluding);
        sb.append(", maxNumberOfTries=").append(maxNumberOfTries);
        sb.append(", delayBetweenRetries=").append(delayBetweenRetries);
        sb.append(", backoffStrategy=").append(backoffStrategy);
        sb.append(", valuesToRetryOn=").append(valuesToRetryOn);
        sb.append(", valuesToExpect=").append(valuesToExpect);
        sb.append(", retryOnValue=").append(retryOnValue);
        sb.append(", customRetryOnLogic=").append(customRetryOnLogic);
        sb.append('}');
        return sb.toString();
    }
}
