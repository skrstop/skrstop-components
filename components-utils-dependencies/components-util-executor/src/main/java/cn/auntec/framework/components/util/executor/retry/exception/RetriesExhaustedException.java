package cn.auntec.framework.components.util.executor.retry.exception;


import cn.auntec.framework.components.util.executor.retry.Status;

/**
 * This exception represents a call execution that never succeeded after exhausting all retries.
 */
public class RetriesExhaustedException extends RetryException {

    private Status status;

    public RetriesExhaustedException(String message, Throwable cause, Status status) {
        super(message, cause);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
