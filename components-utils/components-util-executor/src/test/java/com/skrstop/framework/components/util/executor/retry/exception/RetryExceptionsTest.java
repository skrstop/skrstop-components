package com.skrstop.framework.components.util.executor.retry.exception;

import com.skrstop.framework.components.util.executor.retry.Status;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class RetryExceptionsTest {

    @Test
    public void testRetryException() {
        RetryException e1 = new RetryException("msg");
        Assert.assertEquals("msg", e1.getMessage());
        RuntimeException cause = new RuntimeException("cause");
        RetryException e2 = new RetryException("msg", cause);
        Assert.assertSame(cause, e2.getCause());
    }

    @Test
    public void testInvalidRetryConfigException() {
        InvalidRetryConfigException e = new InvalidRetryConfigException("bad");
        Assert.assertEquals("bad", e.getMessage());
        Assert.assertTrue(e instanceof RetryException);
    }

    @Test
    public void testUnexpectedException() {
        RuntimeException cause = new RuntimeException("c");
        UnexpectedException e = new UnexpectedException("u", cause);
        Assert.assertEquals("u", e.getMessage());
        Assert.assertSame(cause, e.getCause());
    }

    @Test
    public void testRetriesExhaustedException() {
        Status status = new Status();
        RuntimeException cause = new RuntimeException("c");
        RetriesExhaustedException e = new RetriesExhaustedException("exhausted", cause, status);
        Assert.assertEquals("exhausted", e.getMessage());
        Assert.assertSame(cause, e.getCause());
        Assert.assertSame(status, e.getStatus());
    }

}
