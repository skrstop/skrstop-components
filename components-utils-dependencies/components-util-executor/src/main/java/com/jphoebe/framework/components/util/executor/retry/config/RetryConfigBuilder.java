package com.jphoebe.framework.components.util.executor.retry.config;

import com.jphoebe.framework.components.util.executor.retry.backoff.*;
import com.jphoebe.framework.components.util.executor.retry.exception.InvalidRetryConfigException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static java.time.temporal.ChronoUnit.SECONDS;

public class RetryConfigBuilder {

    public static final String MUST_SPECIFY_BACKOFF__ERROR_MSG =
            "Retry config must specify a backoff strategy!";
    public static final String MUST_SPECIFY_MAX_TRIES__ERROR_MSG =
            "Retry config must specify a maximum number of tries!";
    public static final String CAN_ONLY_SPECIFY_ONE_BACKOFF_STRAT__ERROR_MSG =
            "Retry config cannot specify more than one backoff strategy!";
    public static final String CAN_ONLY_SPECIFY_ONE_EXCEPTION_STRAT__ERROR_MSG =
            "Retry config cannot specify more than one exception strategy!";
    public static final String ALREADY_SPECIFIED_NUMBER_OF_TRIES__ERROR_MSG =
            "Number of tries can only be specified once!";
    public static final String CAN_ONLY_SPECIFY_CUSTOM_EXCEPTION_STRAT__ERROR_MSG =
            "You cannot use built in exception logic and custom exception logic in the same config!";
    public static final String MUST_SPECIFY_MAX_TRIES_ABOVE_0__ERROR_MSG =
            "Cannot specify a maximum number of tries less than 1!";
    public static final String SHOULD_SPECIFY_RETRY_COUNT_AS_POSITIVE__ERROR_MSG =
            "Max number of retries must be a non-negative number.";
    public static final String SHOULD_SPECIFY_DELAY_BETWEEN_RETRIES_AS_POSTIVE__ERROR_MSG =
            "Delay between retries must be a non-negative Duration.";
    private boolean builtInExceptionStrategySpecified;
    private boolean validationEnabled;
    private Boolean retryOnAnyException = false;
    private Set<Class<? extends Exception>> retryOnSpecificExceptions = new HashSet<>();
    private Set<Class<? extends Exception>> retryOnAnyExceptionExcluding = new HashSet<>();
    private Integer maxNumberOfTries;
    private Duration delayBetweenRetries;
    private BackoffStrategy backoffStrategy;
    private Collection<Object> valuesToRetryOn;
    private Collection<Object> valuesToExpect;
    private Boolean retryOnValue = false;
    private Function<Exception, Boolean> customRetryOnLogic;
    private boolean retryOnCausedBy;

    public RetryConfigBuilder() {
        this.builtInExceptionStrategySpecified = false;
        this.validationEnabled = true;
    }

    public RetryConfigBuilder(boolean validationEnabled) {
        this();
        this.validationEnabled = validationEnabled;
    }

    public boolean isValidationEnabled() {
        return validationEnabled;
    }

    public void setValidationEnabled(boolean validationEnabled) {
        this.validationEnabled = validationEnabled;
    }

    public RetryConfigBuilder retryOnAnyException() {
        validateExceptionStrategyAddition();

        retryOnAnyException = true;

        builtInExceptionStrategySpecified = true;
        return this;
    }

    public RetryConfigBuilder failOnAnyException() {
        validateExceptionStrategyAddition();

        retryOnAnyException = false;
        retryOnSpecificExceptions = new HashSet<>();

        builtInExceptionStrategySpecified = true;
        return this;
    }

    public RetryConfigBuilder retryOnCausedBy() {
        retryOnCausedBy = true;
        return this;
    }

    @SafeVarargs
    public final RetryConfigBuilder retryOnSpecificExceptions(
            Class<? extends Exception>... exceptions) {
        validateExceptionStrategyAddition();

        Set<Class<? extends Exception>> setOfExceptions = new HashSet<>(Arrays.asList(exceptions));
        retryOnSpecificExceptions = setOfExceptions;

        builtInExceptionStrategySpecified = true;
        return this;
    }

    @SafeVarargs
    public final RetryConfigBuilder retryOnAnyExceptionExcluding(
            Class<? extends Exception>... exceptions) {
        validateExceptionStrategyAddition();

        Set<Class<? extends Exception>> setOfExceptions = new HashSet<>(Arrays.asList(exceptions));
        retryOnAnyExceptionExcluding = setOfExceptions;

        builtInExceptionStrategySpecified = true;
        return this;
    }

    public final RetryConfigBuilder retryOnReturnValue(Object value) {
        retryOnValue = true;
        valuesToRetryOn = Arrays.asList(value);

        return this;
    }

    @SafeVarargs
    public final RetryConfigBuilder retryOnReturnValues(Object... values) {
        retryOnValue = true;
        valuesToRetryOn = Arrays.asList(values);

        return this;
    }

    @SafeVarargs
    public final RetryConfigBuilder retryOnReturnValuesExcluding(Object... values) {
        retryOnValue = true;
        valuesToExpect = Arrays.asList(values);

        return this;
    }

    public RetryConfigBuilder retryOnCustomExceptionLogic(
            Function<Exception, Boolean> customRetryFunction) {
        customRetryOnLogic = customRetryFunction;
        return this;
    }

    public RetryConfigBuilder withMaxNumberOfTries(int max) {
        if (max < 1) {
            throw new InvalidRetryConfigException(MUST_SPECIFY_MAX_TRIES_ABOVE_0__ERROR_MSG);
        }

        if (maxNumberOfTries != null) {
            throw new InvalidRetryConfigException(ALREADY_SPECIFIED_NUMBER_OF_TRIES__ERROR_MSG);
        }

        if (max < 0) {
            throw new InvalidRetryConfigException(SHOULD_SPECIFY_RETRY_COUNT_AS_POSITIVE__ERROR_MSG);
        }

        maxNumberOfTries = max;
        return this;
    }

    public RetryConfigBuilder retryIndefinitely() {
        if (maxNumberOfTries != null) {
            throw new InvalidRetryConfigException(ALREADY_SPECIFIED_NUMBER_OF_TRIES__ERROR_MSG);
        }

        maxNumberOfTries = Integer.MAX_VALUE;
        return this;
    }

    public RetryConfigBuilder withDelayBetweenTries(Duration duration) {
        if (duration.isNegative()) {
            throw new InvalidRetryConfigException(
                    SHOULD_SPECIFY_DELAY_BETWEEN_RETRIES_AS_POSTIVE__ERROR_MSG);
        }

        delayBetweenRetries = duration;
        return this;
    }

    public RetryConfigBuilder withDelayBetweenTries(long amount, ChronoUnit time) {
        delayBetweenRetries = Duration.of(amount, time);
        return this;
    }

    public RetryConfigBuilder withBackoffStrategy(BackoffStrategy backoffStrategy) {
        validateBackoffStrategyAddition();
        this.backoffStrategy = backoffStrategy;
        return this;
    }

    public RetryConfigBuilder withFixedBackoff() {
        validateBackoffStrategyAddition();
        backoffStrategy = new FixedBackoffStrategy();
        return this;
    }

    public RetryConfigBuilder withExponentialBackoff() {
        validateBackoffStrategyAddition();
        backoffStrategy = new ExponentialBackoffStrategy();
        return this;
    }

    public RetryConfigBuilder withFibonacciBackoff() {
        validateBackoffStrategyAddition();
        backoffStrategy = new FibonacciBackoffStrategy();
        return this;
    }

    public RetryConfigBuilder withNoWaitBackoff() {
        validateBackoffStrategyAddition();
        backoffStrategy = new NoWaitBackoffStrategy();
        return this;
    }

    public RetryConfigBuilder withRandomBackoff() {
        validateBackoffStrategyAddition();
        backoffStrategy = new RandomBackoffStrategy();
        return this;
    }

    public RetryConfigBuilder withRandomExponentialBackoff() {
        validateBackoffStrategyAddition();
        backoffStrategy = new RandomExponentialBackoffStrategy();
        return this;
    }

    public RetryConfig build() {
        RetryConfig retryConfig =
                new RetryConfig(
                        retryOnAnyException,
                        retryOnSpecificExceptions,
                        retryOnAnyExceptionExcluding,
                        maxNumberOfTries,
                        delayBetweenRetries,
                        backoffStrategy,
                        valuesToRetryOn,
                        valuesToExpect,
                        retryOnValue,
                        customRetryOnLogic,
                        retryOnCausedBy);

        validateConfig(retryConfig);

        return retryConfig;
    }

    private void validateConfig(RetryConfig retryConfig) {
        if (!validationEnabled) {
            return;
        }

        if (null == retryConfig.getBackoffStrategy()) {
            throw new InvalidRetryConfigException(MUST_SPECIFY_BACKOFF__ERROR_MSG);
        }

        if (null == retryConfig.getMaxNumberOfTries()) {
            throw new InvalidRetryConfigException(MUST_SPECIFY_MAX_TRIES__ERROR_MSG);
        }

        if (null != retryConfig.getCustomRetryOnLogic() && builtInExceptionStrategySpecified) {
            throw new InvalidRetryConfigException(CAN_ONLY_SPECIFY_CUSTOM_EXCEPTION_STRAT__ERROR_MSG);
        }

        backoffStrategy.validateConfig(retryConfig);
    }

    private void validateBackoffStrategyAddition() {
        if (!validationEnabled) {
            return;
        }

        if (null != backoffStrategy) {
            throw new InvalidRetryConfigException(CAN_ONLY_SPECIFY_ONE_BACKOFF_STRAT__ERROR_MSG);
        }
    }

    private void validateExceptionStrategyAddition() {
        if (!validationEnabled) {
            return;
        }

        if (builtInExceptionStrategySpecified) {
            throw new InvalidRetryConfigException(CAN_ONLY_SPECIFY_ONE_EXCEPTION_STRAT__ERROR_MSG);
        }
    }

    public RetryConfigBuilder fixedBackoff5Tries10Sec() {
        return new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(5)
                .withDelayBetweenTries(10, SECONDS)
                .withFixedBackoff();
    }

    public RetryConfigBuilder exponentialBackoff5Tries5Sec() {
        return new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(5)
                .withDelayBetweenTries(5, SECONDS)
                .withExponentialBackoff();
    }

    public RetryConfigBuilder fiboBackoff7Tries5Sec() {
        return new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(7)
                .withDelayBetweenTries(5, SECONDS)
                .withFibonacciBackoff();
    }

    public RetryConfigBuilder randomExpBackoff10Tries60Sec() {
        return new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(10)
                .withDelayBetweenTries(60, SECONDS)
                .withRandomExponentialBackoff();
    }
}
