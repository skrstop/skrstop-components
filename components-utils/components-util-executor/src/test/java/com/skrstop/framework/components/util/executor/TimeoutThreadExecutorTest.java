package com.skrstop.framework.components.util.executor;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author 蒋时华
 */
public class TimeoutThreadExecutorTest {

    @Test
    public void testExecuteSuccess() throws Exception {
        TimeoutThreadExecutor<String> executor = TimeoutThreadExecutor.<String>builder()
                .name("test")
                .timeoutAble(true)
                .timeout(2000L)
                .task(() -> "ok")
                .build();
        Assert.assertEquals("ok", executor.execute());
        Assert.assertEquals("test", executor.getName());
        Assert.assertEquals(2000L, executor.getTimeout());
        executor.setName("test2");
        Assert.assertEquals("test2", executor.getName());
        executor.setTimeout(3000L);
        Assert.assertEquals(3000L, executor.getTimeout());
        executor.setTimeoutAble(false);
        Assert.assertFalse(executor.isDegrandable());
    }

    @Test
    public void testExecuteNoTimeout() throws Exception {
        TimeoutThreadExecutor<String> executor = TimeoutThreadExecutor.<String>builder()
                .task(() -> "done")
                .build();
        Assert.assertEquals("done", executor.execute());
    }

    @Test
    public void testExecuteTimeout() {
        TimeoutThreadExecutor<String> executor = TimeoutThreadExecutor.<String>builder()
                .name("slow")
                .timeoutAble(true)
                .timeout(50L)
                .task(() -> {
                    Thread.sleep(2000);
                    return "late";
                })
                .build();
        try {
            executor.execute();
            Assert.fail("should throw");
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testInvalidConfig() {
        // null 任务 → IllegalArgumentException
        try {
            TimeoutThreadExecutor.<String>builder()
                    .name("bad")
                    .timeoutAble(true)
                    .timeout(1000L)
                    .task(null)
                    .build();
            Assert.fail("should throw");
        } catch (com.skrstop.framework.components.core.exception.defined.illegal.IllegalArgumentException ignored) {
        }
        // timeout <= 0 → 默认 1000
        TimeoutThreadExecutor<String> defaultTimeout = TimeoutThreadExecutor.<String>builder()
                .timeout(0L)
                .task(() -> "x")
                .build();
        Assert.assertEquals(1000L, defaultTimeout.getTimeout());
        Assert.assertNull(defaultTimeout.getName());
    }


    @Test
    public void testInterruptedTaskCallable() throws Exception {
        TimeoutThreadExecutor<String> executor = new TimeoutThreadExecutor<String>("int", true, 2000L, () -> "x") {
            @Override
            Callable<String> newTaskCallable() {
                return () -> {
                    throw new InterruptedException("interrupted");
                };
            }
        };
        try {
            executor.execute();
            Assert.fail("should throw");
        } catch (ExecutionException e) {
            Assert.assertTrue(e.getCause() instanceof InterruptedException);
        }
        // 内部 callable 未中断分支：直接调用
        TimeoutThreadExecutor<String> executor2 = new TimeoutThreadExecutor<>("direct", true, 1000L, () -> "direct");
        Assert.assertEquals("direct", executor2.newTaskCallable().call());
    }

    @Test
    public void testShutdownFailureIgnored() throws Exception {
        TimeoutThreadExecutor<String> executor = new TimeoutThreadExecutor<String>("shutdown", true, 2000L, () -> "ok") {
            @Override
            ExecutorService newExecutorService() {
                return new java.util.concurrent.ThreadPoolExecutor(
                        1, 1, 0L, TimeUnit.MILLISECONDS,
                        new java.util.concurrent.LinkedBlockingQueue<>()) {
                    @Override
                    public void shutdown() {
                        throw new IllegalStateException("boom");
                    }
                };
            }
        };
        Assert.assertEquals("ok", executor.execute());
    }


    @Test
    public void testBlankNameIgnored() throws Exception {
        TimeoutThreadExecutor<String> executor = TimeoutThreadExecutor.<String>builder()
                .name("   ")
                .timeout(10L)
                .task(() -> "x")
                .build();
        Assert.assertNull(executor.getName());
        Assert.assertEquals("x", executor.execute());
    }

    @Test
    public void testOriginalCallableInterruptedBranch() throws Exception {
        TimeoutThreadExecutor<String> executor = TimeoutThreadExecutor.<String>builder()
                .name("orig")
                .timeout(1000L)
                .task(() -> "x")
                .build();
        Thread.currentThread().interrupt();
        try {
            executor.newTaskCallable().call();
            Assert.fail("should throw");
        } catch (InterruptedException ignored) {
        } finally {
            Thread.interrupted();
        }
    }

}
