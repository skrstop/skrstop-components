package com.skrstop.framework.components.util.executor.retry;

import com.skrstop.framework.components.util.executor.retry.config.RetryConfigBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class AsyncCallExecutorTest {

    private RetryConfigBuilder base() {
        return new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff();
    }

    @Test
    public void testAsyncSuccessWithDedicatedExecutor() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            AsyncCallExecutor<String> executor = new CallExecutorBuilder<String>()
                    .config(base().build())
                    .buildAsync(executorService);
            Assert.assertSame(executorService, executor.getExecutorService());
            Assert.assertSame(executorService, executor.getThreadExecutorService());
            executor.setExecutorService(null);
            Assert.assertNull(executor.getExecutorService());
            executor.setExecutorService(executorService);
            Assert.assertNotNull(executor.getConfig());

            Future<Status<String>> future = executor.execute(() -> "async-ok", "async-name");
            Status<String> status = future.get(5, TimeUnit.SECONDS);
            Assert.assertTrue(status.wasSuccessful());
            Assert.assertEquals("async-ok", status.getResult());
            Assert.assertEquals("async-name", status.getCallName());
        } finally {
            executorService.shutdownNow();
        }
    }

    @Test
    public void testAsyncSuccessWithThreadFallback() throws Exception {
        AsyncCallExecutor<String> executor = new CallExecutorBuilder<String>()
                .config(base().build())
                .buildAsync();
        Assert.assertNull(executor.getExecutorService());
        AtomicInteger calls = new AtomicInteger();
        Future<Status<String>> future = executor.execute(() -> {
            calls.incrementAndGet();
            return "thread-ok";
        });
        Status<String> status = future.get(5, TimeUnit.SECONDS);
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(1, calls.get());
    }

    @Test
    public void testAsyncFailureCompletesExceptionally() throws Exception {
        AsyncCallExecutor<String> executor = new CallExecutorBuilder<String>()
                .config(base().build())
                .buildAsync();
        Future<Status<String>> future = executor.execute(() -> {
            throw new IllegalStateException("boom");
        });
        try {
            future.get(5, TimeUnit.SECONDS);
            Assert.fail("should throw");
        } catch (java.util.concurrent.ExecutionException e) {
            Assert.assertTrue(e.getCause() instanceof com.skrstop.framework.components.util.executor.retry.exception.RetriesExhaustedException);
        }
    }

    @Test
    public void testGetters() {
        AsyncCallExecutor<String> executor = new CallExecutorBuilder<String>()
                .config(base().build())
                .buildAsync();
        Assert.assertNull(executor.getAfterFailedTryListener());
        Assert.assertNull(executor.getBeforeNextTryListener());
        Assert.assertNull(executor.getOnFailureListener());
        Assert.assertNull(executor.getOnSuccessListener());
        Assert.assertNull(executor.getOnCompletionListener());
    }

}
