package com.skrstop.framework.components.util.executor;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class RetriableThreadExecutorTest {

    @Test(timeout = 10000)
    public void testSuccessFirstAttempt() {
        AtomicInteger completed = new AtomicInteger();
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "success-task",
                () -> "done",
                new RetriableThreadExecutor.RetriableAttribute(2, 10, TimeUnit.MILLISECONDS),
                new RetriableThreadExecutor.ExecutorCallback<String>() {
                    @Override
                    public void onCompleted(String result) {
                        Assert.assertEquals("done", result);
                        completed.incrementAndGet();
                    }

                    @Override
                    public void onFailed(String message) {
                        Assert.fail("should not fail");
                    }
                });
        executor.execute();
        Assert.assertEquals(1, completed.get());
        executor.stopTimer();
    }

    @Test(timeout = 15000)
    public void testRetryThenSuccess() throws Exception {
        AtomicInteger completed = new AtomicInteger();
        AtomicInteger beforeCall = new AtomicInteger();
        Callable<String> task = () -> {
            if (beforeCall.incrementAndGet() < 3) {
                throw new IllegalStateException("fail first two");
            }
            return "ok";
        };
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "retry-task",
                task,
                new RetriableThreadExecutor.RetriableAttribute(5, 10, TimeUnit.MILLISECONDS),
                new RetriableThreadExecutor.ExecutorCallback<String>() {
                    @Override
                    public void onCompleted(String result) {
                        completed.incrementAndGet();
                    }

                    @Override
                    public void onFailed(String message) {
                        Assert.fail("should not fail");
                    }
                });
        executor.execute();
        long deadline = System.currentTimeMillis() + 10000;
        while (completed.get() == 0 && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        Assert.assertEquals(1, completed.get());
        Assert.assertTrue(beforeCall.get() >= 3);
        executor.stopTimer();
    }

    @Test(timeout = 20000)
    public void testRetryExhaustedOnFailed() throws Exception {
        AtomicInteger failed = new AtomicInteger();
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "fail-task",
                () -> {
                    throw new IllegalStateException("always fail");
                },
                new RetriableThreadExecutor.RetriableAttribute(1, 10, TimeUnit.MILLISECONDS),
                new RetriableThreadExecutor.ExecutorCallback<String>() {
                    @Override
                    public void onCompleted(String result) {
                        Assert.fail("should not complete");
                    }

                    @Override
                    public void onFailed(String message) {
                        Assert.assertNotNull(message);
                        failed.incrementAndGet();
                    }
                });
        executor.execute();
        long deadline = System.currentTimeMillis() + 15000;
        while (failed.get() == 0 && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        Assert.assertEquals(1, failed.get());
        executor.stopTimer();
    }

    @Test(timeout = 10000)
    public void testNullCallbackSuccess() {
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "no-callback",
                () -> "done",
                new RetriableThreadExecutor.RetriableAttribute(2, 10, TimeUnit.MILLISECONDS),
                null);
        executor.execute();
        executor.stopTimer();
    }

    @Test(timeout = 20000)
    public void testNullCallbackExhausted() throws Exception {
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "no-callback-fail",
                () -> {
                    throw new IllegalStateException("fail");
                },
                new RetriableThreadExecutor.RetriableAttribute(1, 10, TimeUnit.MILLISECONDS),
                null);
        executor.execute();
        executor.upgradeRetryTimes();
        Thread.sleep(2000);
        executor.stopTimer();
    }

    @Test(timeout = 20000)
    public void testNullCallbackRetryThenStop() throws Exception {
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "no-callback-retry",
                () -> {
                    throw new IllegalStateException("fail");
                },
                new RetriableThreadExecutor.RetriableAttribute(5, 10, TimeUnit.MILLISECONDS),
                null);
        executor.execute();
        Thread.sleep(1500);
        executor.stopTimer();
        executor.upgradeRetryTimes();
    }


    @Test
    public void testStopTimerWhenNull() throws Exception {
        RetriableThreadExecutor<String> executor = new RetriableThreadExecutor<>(
                "null-timer", () -> "x",
                new RetriableThreadExecutor.RetriableAttribute(1, 10, TimeUnit.MILLISECONDS), null);
        Field f = RetriableThreadExecutor.class.getDeclaredField("hashedWheelTimer");
        f.setAccessible(true);
        f.set(executor, null);
        executor.stopTimer();
    }

}
