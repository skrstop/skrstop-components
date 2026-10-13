package com.skrstop.framework.components.util.executor.retry;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

/**
 * @author 蒋时华
 */
public class StatusTest {

    @Test
    public void testGettersAndSetters() {
        Status<String> status = new Status<>();
        status.setId("id");
        status.setStartTime(1L);
        status.setEndTime(2L);
        status.setCallName("call");
        status.setTotalTries(3);
        status.setTotalElapsedDuration(Duration.ofMillis(5));
        Exception e = new RuntimeException("r");
        status.setLastExceptionThatCausedRetry(e);
        status.setResult("res");
        status.setSuccessful(true);

        Assert.assertEquals("id", status.getId());
        Assert.assertEquals(1L, status.getStartTime());
        Assert.assertEquals(2L, status.getEndTime());
        Assert.assertEquals("call", status.getCallName());
        Assert.assertEquals(3, status.getTotalTries());
        Assert.assertEquals(Duration.ofMillis(5), status.getTotalElapsedDuration());
        Assert.assertSame(e, status.getLastExceptionThatCausedRetry());
        Assert.assertEquals("res", status.getResult());
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertTrue(status.toString().contains("Status"));
        Assert.assertTrue(status.toString().contains("call"));
    }

    @Test
    public void testAttemptStatus() {
        AttemptStatus<String> attemptStatus = new AttemptStatus<>();
        Assert.assertFalse(attemptStatus.wasSuccessful());
        Assert.assertNull(attemptStatus.getResult());
        attemptStatus.setSuccessful(true);
        attemptStatus.setResult("v");
        Assert.assertTrue(attemptStatus.wasSuccessful());
        Assert.assertEquals("v", attemptStatus.getResult());
    }

}
