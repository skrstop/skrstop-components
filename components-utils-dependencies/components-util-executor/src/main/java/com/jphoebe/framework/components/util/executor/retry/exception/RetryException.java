package com.jphoebe.framework.components.util.executor.retry.exception;

public class RetryException extends RuntimeException {

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryException(String message) {
        super(message);
    }
}
