package com.jphoebe.framework.components.util.executor.retry.exception;

/**
 * This exception represents when a call throws an exception that was not specified as one to retry
 * on in the RetryConfig.
 */
public class UnexpectedException extends RetryException {

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
