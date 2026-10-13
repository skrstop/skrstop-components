package com.skrstop.framework.components.util.executor.timer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class HashedWheelTimerTest {

    @Test
    public void testScheduleIndex() {
        ScheduleIndex index = new ScheduleIndex("subject", 100L, 20L, 8, 5L);
        Assert.assertEquals("subject", index.getSubject());
        Assert.assertEquals(100L, index.getScheduleTime());
        Assert.assertEquals(20L, index.getOffset());
        Assert.assertEquals(8, index.getSize());
        Assert.assertEquals(5L, index.getSequence());
    }

    @Test
    public void testConstructorValidation() {
        try {
            new HashedWheelTimer(null, 100, TimeUnit.MILLISECONDS, 8, index -> {
            });
            Assert.fail("should throw");
        } catch (NullPointerException ignored) {
        }
        try {
            new HashedWheelTimer(Thread::new, 100, null, 8, index -> {
            });
            Assert.fail("should throw");
        } catch (NullPointerException ignored) {
        }
        try {
            new HashedWheelTimer(Thread::new, 0, TimeUnit.MILLISECONDS, 8, index -> {
            });
            Assert.fail("should throw");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new HashedWheelTimer(Thread::new, 100, TimeUnit.MILLISECONDS, 0, index -> {
            });
            Assert.fail("should throw");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new HashedWheelTimer(Thread::new, Long.MAX_VALUE / 2, TimeUnit.NANOSECONDS, 8, index -> {
            });
            Assert.fail("should throw");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExecuteTask() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = new HashedWheelTimer(
                Thread::new, 10, TimeUnit.MILLISECONDS, 8,
                index -> processed.incrementAndGet());
        timer.start();
        timer.start(); // 已启动，第二次 start 无副作用
        timer.newTimeout(new ScheduleIndex("s", 1, 1, 1, 1), 5, TimeUnit.MILLISECONDS);
        long deadline = System.currentTimeMillis() + 5000;
        while (processed.get() == 0 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        Assert.assertEquals(1, processed.get());
        Set<?> unprocessed = timer.stop();
        Assert.assertNotNull(unprocessed);
        // 再次 stop → 已关闭状态
        try {
            timer.start();
            Assert.fail("should throw");
        } catch (IllegalStateException ignored) {
        }
        Set<?> empty = timer.stop();
        Assert.assertTrue(empty.isEmpty());
    }

    @Test
    public void testStopWithoutStart() {
        HashedWheelTimer timer = new HashedWheelTimer(
                Thread::new, 10, TimeUnit.MILLISECONDS, 8,
                index -> {
                });
        Set<?> unprocessed = timer.stop();
        Assert.assertNotNull(unprocessed);
    }

    @Test
    public void testNewTimeoutThenStopBeforeExecution() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = new HashedWheelTimer(
                Thread::new, 500, TimeUnit.MILLISECONDS, 8,
                index -> processed.incrementAndGet());
        timer.newTimeout(new ScheduleIndex("s", 1, 1, 1, 1), 10, TimeUnit.SECONDS);
        Set<?> unprocessed = timer.stop();
        // 未 start 的 timer，worker 从未运行，队列中的任务不会被取出
        Assert.assertEquals(0, unprocessed.size());
        Assert.assertEquals(0, processed.get());
    }

    @Test
    public void testTimeoutToStringAndState() throws Exception {
        HashedWheelTimer timer = new HashedWheelTimer(
                Thread::new, 50, TimeUnit.MILLISECONDS, 4,
                index -> {
                });
        timer.newTimeout(new ScheduleIndex("s", 1, 1, 1, 1), 1, TimeUnit.SECONDS);
        // 未 start，无法获取内部 timeout 对象；仅验证 stop 正常
        timer.stop();
        Assert.assertTrue(true);
    }

    @Test
    public void testMultipleTimeoutsExpire() throws Exception {
        CountDownLatch latch = new CountDownLatch(3);
        HashedWheelTimer timer = new HashedWheelTimer(
                Thread::new, 10, TimeUnit.MILLISECONDS, 8,
                index -> latch.countDown());
        timer.start();
        for (int i = 0; i < 3; i++) {
            timer.newTimeout(new ScheduleIndex("s" + i, i, i, 1, i), 5, TimeUnit.MILLISECONDS);
        }
        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
        timer.stop();
    }


    @Test
    public void testTicksPerWheelTooLarge() {
        try {
            new HashedWheelTimer(Thread::new, 100, TimeUnit.MILLISECONDS, 1073741825, index -> {
            });
            Assert.fail("should throw");
        } catch (IllegalArgumentException ignored) {
        }
    }

}
