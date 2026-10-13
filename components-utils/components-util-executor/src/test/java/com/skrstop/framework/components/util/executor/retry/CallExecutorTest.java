package com.skrstop.framework.components.util.executor.retry;

import com.skrstop.framework.components.util.executor.retry.config.RetryConfig;
import com.skrstop.framework.components.util.executor.retry.config.RetryConfigBuilder;
import com.skrstop.framework.components.util.executor.retry.exception.RetriesExhaustedException;
import com.skrstop.framework.components.util.executor.retry.exception.UnexpectedException;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class CallExecutorTest {

    private RetryConfigBuilder base() {
        return new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff();
    }

    @Test
    public void testSuccessFirstTry() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>()
                .config(base().build())
                .build()
                .execute(() -> {
                    calls.incrementAndGet();
                    return "ok";
                }, "named-call");
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals("ok", status.getResult());
        Assert.assertEquals(1, status.getTotalTries());
        Assert.assertEquals("named-call", status.getCallName());
        Assert.assertNotNull(status.getTotalElapsedDuration());
        Assert.assertNotNull(status.getId());
    }

    @Test
    public void testSuccessSecondTryWithListeners() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        AtomicInteger afterFailed = new AtomicInteger();
        AtomicInteger beforeNext = new AtomicInteger();
        AtomicInteger onSuccess = new AtomicInteger();
        AtomicInteger onCompletion = new AtomicInteger();
        AtomicInteger onFailure = new AtomicInteger();

        CallExecutor<String> executor = new CallExecutorBuilder<String>()
                .config(base().build())
                .afterFailedTryListener(status -> afterFailed.incrementAndGet())
                .beforeNextTryListener(status -> beforeNext.incrementAndGet())
                .onSuccessListener(status -> onSuccess.incrementAndGet())
                .onCompletionListener(status -> onCompletion.incrementAndGet())
                .onFailureListener(status -> onFailure.incrementAndGet())
                .build();

        Status<String> status = executor.execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new IllegalStateException("fail once");
            }
            return "ok2";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(1, afterFailed.get());
        Assert.assertEquals(1, beforeNext.get());
        Assert.assertEquals(1, onSuccess.get());
        Assert.assertEquals(1, onCompletion.get());
        Assert.assertEquals(0, onFailure.get());
        Assert.assertNotNull(executor.getConfig());
        Assert.assertNotNull(executor.getAfterFailedTryListener());
        Assert.assertNotNull(executor.getBeforeNextTryListener());
        Assert.assertNotNull(executor.getOnFailureListener());
        Assert.assertNotNull(executor.getOnSuccessListener());
        Assert.assertNotNull(executor.getOnCompletionListener());
        Assert.assertTrue(executor.toString().contains("CallExecutor"));
    }

    @Test
    public void testRetriesExhaustedThrows() {
        CallExecutor<String> executor = new CallExecutorBuilder<String>()
                .config(base().build())
                .build();
        try {
            executor.execute(() -> {
                throw new IllegalStateException("always fail");
            });
            Assert.fail("should throw");
        } catch (RetriesExhaustedException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalStateException);
            Assert.assertNotNull(e.getStatus());
            Assert.assertFalse(e.getStatus().wasSuccessful());
        }
    }

    @Test
    public void testRetriesExhaustedWithOnFailureListener() {
        AtomicInteger onFailure = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>()
                .config(base().build())
                .onFailureListener(s -> onFailure.incrementAndGet())
                .build()
                .execute(() -> {
                    throw new IllegalStateException("always fail");
                });
        Assert.assertEquals(1, onFailure.get());
        Assert.assertFalse(status.wasSuccessful());
    }

    @Test
    public void testUnexpectedExceptionWhenNotRetriable() {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .failOnAnyException()
                .build();
        try {
            new CallExecutorBuilder<String>().config(config).build()
                    .execute(() -> {
                        throw new IllegalStateException("not expected");
                    });
            Assert.fail("should throw");
        } catch (UnexpectedException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalStateException);
        }
    }

    @Test
    public void testRetryOnSpecificExceptions() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnSpecificExceptions(IllegalStateException.class)
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new IllegalStateException("retry me");
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testRetryOnAnyExceptionExcluding() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnAnyExceptionExcluding(UnsupportedOperationException.class)
                .build();
        // 排除的异常 → 不重试，抛出 UnexpectedException
        try {
            new CallExecutorBuilder<String>().config(config).build()
                    .execute(() -> {
                        throw new UnsupportedOperationException("no retry");
                    });
            Assert.fail("should throw");
        } catch (UnexpectedException ignored) {
        }
        // 其它异常 → 重试后成功
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new IllegalStateException("retry me");
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testRetryOnCausedBy() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnAnyException()
                .retryOnCausedBy()
                .build();
        // 默认 failOnAnyException，cause 匹配 Exception 子类 → 重试
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new RuntimeException("wrap", new IllegalStateException("cause"));
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testRetryOnReturnValue() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnReturnValue("bad")
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                return "bad";
            }
            return "good";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals("good", status.getResult());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testRetryOnReturnValues() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnReturnValues("a", "b")
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                return "b";
            }
            return "good";
        });
        Assert.assertEquals("good", status.getResult());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testRetryOnReturnValuesExcluding() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnReturnValuesExcluding("good")
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                return "not-expected";
            }
            return "good";
        });
        Assert.assertEquals("good", status.getResult());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testCustomRetryLogic() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnCustomExceptionLogic(e -> e instanceof IllegalStateException)
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new IllegalStateException("retry me");
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
        // 自定义逻辑返回 false → UnexpectedException
        RetryConfig noRetry = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnCustomExceptionLogic(e -> false)
                .build();
        try {
            new CallExecutorBuilder<String>().config(noRetry).build()
                    .execute(() -> {
                        throw new IllegalStateException("no");
                    });
            Assert.fail("should throw");
        } catch (UnexpectedException ignored) {
        }
    }

    @Test
    public void testExhaustedWithRetryOnValue() {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(2)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnReturnValue("bad")
                .build();
        try {
            new CallExecutorBuilder<String>().config(config).build().execute(() -> "bad");
            Assert.fail("should throw");
        } catch (RetriesExhaustedException ignored) {
        }
    }

    @Test
    public void testInterruptedSleepIsIgnored() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withFixedBackoff()
                .build();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> "ok");
        Assert.assertTrue(status.wasSuccessful());
    }


    @Test
    public void testNullDelayBetweenRetries() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(3)
                .withNoWaitBackoff()
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new IllegalStateException("fail once");
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testSpecificExceptionNoMatchThrows() {
        RetryConfig config = new RetryConfigBuilder()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .retryOnSpecificExceptions(NumberFormatException.class)
                .build();
        try {
            new CallExecutorBuilder<String>().config(config).build()
                    .execute(() -> {
                        throw new IllegalStateException("not registered");
                    });
            Assert.fail("should throw");
        } catch (UnexpectedException ignored) {
        }
    }

    @Test
    public void testDeepCauseChain() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .retryOnAnyException()
                .retryOnCausedBy()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new RuntimeException("l1", new RuntimeException("l2", new IllegalStateException("leaf")));
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testToString() throws Exception {
        CallExecutor<String> executor = new CallExecutorBuilder<String>().config(base().build()).build();
        Assert.assertTrue(executor.toString().contains("CallExecutor"));
    }


    @Test(timeout = 10000)
    public void testInterruptedDuringRetrySleep() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .retryOnAnyException()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(300))
                .withFixedBackoff()
                .build();
        AtomicInteger calls = new AtomicInteger();
        try {
            Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
                if (calls.incrementAndGet() < 2) {
                    // 设置中断标志 → 重试 sleep 立即抛 InterruptedException 被忽略
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("fail once");
                }
                return "ok";
            });
            Assert.assertTrue(status.wasSuccessful());
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    public void testCausedByWithSpecificExceptions() throws Exception {
        RetryConfig config = new RetryConfigBuilder()
                .retryOnSpecificExceptions(IllegalStateException.class)
                .retryOnCausedBy()
                .withMaxNumberOfTries(3)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .withNoWaitBackoff()
                .build();
        AtomicInteger calls = new AtomicInteger();
        Status<String> status = new CallExecutorBuilder<String>().config(config).build().execute(() -> {
            if (calls.incrementAndGet() < 2) {
                throw new RuntimeException("wrap", new IllegalStateException("cause"));
            }
            return "ok";
        });
        Assert.assertTrue(status.wasSuccessful());
        Assert.assertEquals(2, calls.get());
    }

}
