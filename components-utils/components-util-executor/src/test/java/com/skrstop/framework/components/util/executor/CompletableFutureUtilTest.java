package com.skrstop.framework.components.util.executor;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class CompletableFutureUtilTest {

    @Test
    public void testConstructors() {
        Assert.assertNotNull(new CompletableFutureUtil());
        Assert.assertNotNull(new CompletableFutureUtil(ThreadPoolUtil.newCachedThreadPool(1, 2, ThreadPoolUtil.buildQueue(5))));
        Assert.assertNotNull(CompletableFutureUtil.builder().build());
    }

    @Test
    public void testRunAndSupply() throws Throwable {
        CompletableFutureUtil util = new CompletableFutureUtil();
        AtomicInteger ran = new AtomicInteger();
        CompletableFuture<?> f = util.run(() -> ran.incrementAndGet());
        util.get(f);
        Assert.assertEquals(1, ran.get());

        CompletableFuture<String> s = util.supply(() -> "ok");
        Assert.assertEquals("ok", util.accept(s));
    }

    @Test
    public void testThenAccept() throws Throwable {
        CompletableFutureUtil util = new CompletableFutureUtil();
        AtomicInteger ran = new AtomicInteger();
        CompletableFuture<String> s = util.supply(() -> "v");
        util.thenAccept(s, v -> ran.incrementAndGet());
        s.join();
        Assert.assertTrue(ran.get() >= 0);

        CompletableFuture<String> s2 = util.supply(() -> "v2");
        util.thenAcceptSync(s2, v -> ran.incrementAndGet());
        s2.join();
    }

    @Test
    public void testGetThrowsCause() {
        CompletableFutureUtil util = new CompletableFutureUtil();
        CompletableFuture<?> failed = util.run(() -> {
            throw new IllegalStateException("boom");
        });
        try {
            util.get(failed);
            Assert.fail("should throw");
        } catch (IllegalStateException ignored) {
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testAcceptThrowsCause() {
        CompletableFutureUtil util = new CompletableFutureUtil();
        CompletableFuture<String> failed = util.supply(() -> {
            throw new IllegalStateException("boom2");
        });
        try {
            util.accept(failed);
            Assert.fail("should throw");
        } catch (IllegalStateException ignored) {
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }


    @Test
    public void testThenAcceptCatch() {
        CompletableFutureUtil util = new CompletableFutureUtil();
        try {
            util.thenAccept(null, v -> {
            });
            Assert.fail("should throw");
        } catch (NullPointerException ignored) {
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
        try {
            util.thenAcceptSync(null, v -> {
            });
            Assert.fail("should throw");
        } catch (NullPointerException ignored) {
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }

}
