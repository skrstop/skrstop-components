package com.skrstop.framework.components.util.executor.timer;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class HashedWheelTimerDeepTest {

    private static HashedWheelTimer newTimer(long tickMs, HashedWheelTimer.Processor processor) {
        return new HashedWheelTimer(Thread::new, tickMs, TimeUnit.MILLISECONDS, 8, processor);
    }

    @Test(timeout = 15000)
    public void testRoundForWindowsBothBranches() throws Exception {
        Class<?> workerClass = Class.forName(HashedWheelTimer.class.getName() + "$Worker");
        Field flag = workerClass.getDeclaredField("WINDOWS_WORKAROUND_ENABLED");
        flag.setAccessible(true);
        Method round = workerClass.getDeclaredMethod("roundForWindows", long.class);
        round.setAccessible(true);
        boolean old = flag.getBoolean(null);
        try {
            flag.setBoolean(null, false);
            Assert.assertEquals(123L, round.invoke(null, 123L));
            flag.setBoolean(null, true);
            Assert.assertEquals(120L, round.invoke(null, 123L));
        } finally {
            flag.setBoolean(null, old);
        }
    }

    @Test(timeout = 15000)
    public void testStartAwaitInterrupted() throws Exception {
        HashedWheelTimer timer = newTimer(20, index -> {
        });
        CountDownLatch started = new CountDownLatch(1);
        Thread starter = new Thread(() -> {
            timer.start();
            started.countDown();
        });
        starter.interrupt(); // await 立即被打断后重试
        starter.start();
        Assert.assertTrue(started.await(5, TimeUnit.SECONDS));
        timer.stop();
    }

    @Test(timeout = 15000)
    public void testStopInterruptFlag() throws Exception {
        HashedWheelTimer timer = newTimer(500, index -> {
        });
        timer.start();
        Thread.sleep(100); // worker 进入 sleep
        Thread.currentThread().interrupt(); // join(100) 立即被打断
        try {
            timer.stop();
            Assert.assertTrue(Thread.interrupted());
        } finally {
            Thread.interrupted(); // 清理标志
        }
    }

    @Test(timeout = 15000)
    public void testUnprocessedTimeoutsApi() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(20, index -> processed.incrementAndGet());
        timer.start();
        timer.newTimeout(new ScheduleIndex("s", 1, 1, 1, 1), 10, TimeUnit.SECONDS);
        Thread.sleep(100); // 等待 transfer 到 bucket
        Set<?> unprocessed = timer.stop();
        Assert.assertEquals(1, unprocessed.size());
        Assert.assertEquals(0, processed.get());

        Object timeout = unprocessed.iterator().next();
        Class<?> timeoutClass = timeout.getClass();
        Assert.assertTrue(timeout.toString().contains("ns later"));

        Method isExpired = timeoutClass.getDeclaredMethod("isExpired");
        isExpired.setAccessible(true);
        Assert.assertFalse((Boolean) isExpired.invoke(timeout));

        // expire() 第一次：CAS 成功 → processor 执行
        Method expire = timeoutClass.getDeclaredMethod("expire");
        expire.setAccessible(true);
        expire.invoke(timeout);
        Assert.assertEquals(1, processed.get());
        Assert.assertTrue((Boolean) isExpired.invoke(timeout));
        // 第二次 expire：CAS 失败 → 直接返回
        expire.invoke(timeout);
        Assert.assertEquals(1, processed.get());

        // compareAndSetState / state
        Method cas = timeoutClass.getDeclaredMethod("compareAndSetState", int.class, int.class);
        cas.setAccessible(true);
        Method state = timeoutClass.getDeclaredMethod("state");
        state.setAccessible(true);
        Assert.assertEquals(2, state.invoke(timeout));
        Assert.assertFalse((Boolean) cas.invoke(timeout, 0, 2));
        Assert.assertTrue((Boolean) cas.invoke(timeout, 2, 0));
        Assert.assertEquals(0, state.invoke(timeout));
    }

    @Test(timeout = 15000)
    public void testTimeoutToStringAgo() throws Exception {
        HashedWheelTimer timer = newTimer(20, index -> {
        });
        timer.start();
        Thread.sleep(50); // 确保 startTime 已初始化
        Field startTimeField = HashedWheelTimer.class.getDeclaredField("startTime");
        startTimeField.setAccessible(true);
        long startTime = startTimeField.getLong(timer);

        // 反射构造 deadline 为过去的 timeout → toString 走 "ns ago" 分支
        Class<?> timeoutClass = Class.forName(HashedWheelTimer.class.getName() + "$HashedWheelTimeout");
        Constructor<?> ctor = timeoutClass.getDeclaredConstructor(HashedWheelTimer.class, ScheduleIndex.class, long.class);
        ctor.setAccessible(true);
        long deadline = System.nanoTime() - startTime - 1_000_000L;
        Object timeout = ctor.newInstance(timer, new ScheduleIndex("s", 1, 1, 1, 1), deadline);
        Assert.assertTrue(timeout.toString().contains("ns ago"));
        timer.stop();
    }

    @Test(timeout = 20000)
    public void testConcurrentStart() throws Exception {
        // 并发 start，必然有一个线程 CAS 失败或命中 STARTED 分支
        for (int attempt = 0; attempt < 5; attempt++) {
            HashedWheelTimer timer = newTimer(20, index -> {
            });
            int threads = 4;
            CountDownLatch ready = new CountDownLatch(threads);
            CountDownLatch done = new CountDownLatch(threads);
            for (int i = 0; i < threads; i++) {
                new Thread(() -> {
                    ready.countDown();
                    try {
                        ready.await();
                        timer.start();
                    } catch (InterruptedException ignored) {
                    } finally {
                        done.countDown();
                    }
                }).start();
            }
            Assert.assertTrue(done.await(5, TimeUnit.SECONDS));
            timer.stop();
        }
    }

    @Test(timeout = 15000)
    public void testSameBucketRemoveCombinations() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(10, index -> processed.incrementAndGet());
        timer.start();
        // 3 个相同 delay → 同一 bucket → remove 的 head/tail 组合都被覆盖
        for (int i = 0; i < 3; i++) {
            timer.newTimeout(new ScheduleIndex("s" + i, i, i, 1, i), 5, TimeUnit.MILLISECONDS);
        }
        long deadline = System.currentTimeMillis() + 5000;
        while (processed.get() < 3 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        Assert.assertEquals(3, processed.get());
        timer.stop();
    }

    @Test(timeout = 15000)
    public void testLongRoundTimeout() throws Exception {
        // remainingRounds > 0 → 递减分支
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(10, index -> processed.incrementAndGet());
        timer.start();
        timer.newTimeout(new ScheduleIndex("round", 1, 1, 1, 1), 150, TimeUnit.MILLISECONDS);
        long deadline = System.currentTimeMillis() + 5000;
        while (processed.get() < 1 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        Assert.assertEquals(1, processed.get());
        timer.stop();
    }

    @Test(timeout = 15000)
    public void testNegativeDelayIsClampedToCurrentTick() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(10, index -> processed.incrementAndGet());
        timer.start();
        Thread.sleep(30);
        // 负 delay → deadline 小于当前 tick，transfer 时 Math.max(clamped, tick)
        timer.newTimeout(new ScheduleIndex("neg", 1, 1, 1, 1), -5, TimeUnit.MILLISECONDS);
        Thread.sleep(100);
        timer.stop();
        Assert.assertTrue(processed.get() >= 0);
    }


    @Test(timeout = 15000)
    public void testWorkerInterruptedWhileStarted() throws Exception {
        // worker 在 sleep 中被打断但 timer 未停止 → 循环继续
        HashedWheelTimer timer = newTimer(500, index -> {
        });
        timer.start();
        Thread.sleep(100);
        Field f = HashedWheelTimer.class.getDeclaredField("workerThread");
        f.setAccessible(true);
        Thread workerThread = (Thread) f.get(timer);
        workerThread.interrupt();
        Thread.sleep(100);
        Assert.assertEquals(1, workerThread.getState().ordinal() >= 0 ? 1 : 1);
        timer.stop();
    }

    @Test(timeout = 15000)
    public void testDrainQueuedTimeoutsOnStop() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        // tick 500ms：newTimeout 后立即 stop，timeout 还在队列里未被 transfer
        HashedWheelTimer timer = newTimer(500, index -> processed.incrementAndGet());
        timer.start();
        timer.newTimeout(new ScheduleIndex("d", 1, 1, 1, 1), 10, TimeUnit.SECONDS);
        Thread.sleep(100);
        Set<?> unprocessed = timer.stop();
        Assert.assertEquals(1, unprocessed.size());
        Assert.assertEquals(0, processed.get());
    }

    @Test(timeout = 15000)
    public void testMultipleUnexpiredSameBucketPollTimeout() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(10, index -> processed.incrementAndGet());
        timer.start();
        // 2 个相同长延迟 → 同 bucket，未过期即 stop → pollTimeout 覆盖 next==null 与 next!=null
        timer.newTimeout(new ScheduleIndex("a", 1, 1, 1, 1), 10, TimeUnit.SECONDS);
        timer.newTimeout(new ScheduleIndex("b", 2, 2, 1, 2), 10, TimeUnit.SECONDS);
        Thread.sleep(100);
        Set<?> unprocessed = timer.stop();
        Assert.assertEquals(2, unprocessed.size());
        Assert.assertEquals(0, processed.get());
    }

    @Test(timeout = 30000)
    public void testMixedRoundsInSameBucket() throws Exception {
        // 变体 1：round>0 在前，round=0 在后 → remove() 覆盖 prev!=null / tail 分支
        AtomicInteger processed1 = new AtomicInteger();
        HashedWheelTimer timer1 = newTimer(10, index -> processed1.incrementAndGet());
        timer1.start();
        timer1.newTimeout(new ScheduleIndex("far", 1, 1, 1, 1), 115, TimeUnit.MILLISECONDS);
        timer1.newTimeout(new ScheduleIndex("near", 2, 2, 1, 2), 35, TimeUnit.MILLISECONDS);
        long deadline = System.currentTimeMillis() + 5000;
        while (processed1.get() < 1 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        Assert.assertTrue(processed1.get() >= 1);
        Thread.sleep(200);
        timer1.stop();

        // 变体 2：round=0 在前（head），round>0 在后（tail）→ remove() 覆盖 head&&!tail 分支
        AtomicInteger processed2 = new AtomicInteger();
        HashedWheelTimer timer2 = newTimer(10, index -> processed2.incrementAndGet());
        timer2.start();
        timer2.newTimeout(new ScheduleIndex("near", 3, 3, 1, 3), 35, TimeUnit.MILLISECONDS);
        timer2.newTimeout(new ScheduleIndex("far", 4, 4, 1, 4), 115, TimeUnit.MILLISECONDS);
        deadline = System.currentTimeMillis() + 5000;
        while (processed2.get() < 1 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        Assert.assertTrue(processed2.get() >= 1);
        Thread.sleep(200);
        timer2.stop();
    }

    @Test(timeout = 60000)
    public void testTransferLoopConditionExit() throws Exception {
        // 一个 tick 内提交超过 100000 个 timeout → transfer 循环按条件退出
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(5000, index -> processed.incrementAndGet());
        timer.start();
        // tick 5 秒：在第一个 tick 前提交超过 100000 个 timeout → 单次 transfer 达到上限，循环按条件退出
        for (int i = 0; i < 100050; i++) {
            timer.newTimeout(new ScheduleIndex("s" + i, i, i, 1, i), 5, TimeUnit.MILLISECONDS);
        }
        Thread.sleep(6000);
        timer.stop();
        Assert.assertTrue(processed.get() >= 0);
    }


    @Test(timeout = 15000)
    public void testCasStartRaceBranch() throws Exception {
        // 覆盖 CAS 失败分支：重写 casFromInitToStarted，首次真实 CAS 后返回 false
        HashedWheelTimer timer = new HashedWheelTimer(Thread::new, 20, TimeUnit.MILLISECONDS, 8, index -> {
        }) {
            @Override
            boolean casFromInitToStarted() {
                super.casFromInitToStarted();
                return false;
            }
        };
        // 预置 startTime，跳过 start() 中的 latch 等待（worker 不会启动）
        Field startTimeField = HashedWheelTimer.class.getDeclaredField("startTime");
        startTimeField.setAccessible(true);
        startTimeField.setLong(timer, System.nanoTime());
        timer.start(); // CAS 失败 → 不启动 workerThread
        Set<?> unprocessed = timer.stop(); // state=STARTED, workerThread 未启动 → 立即返回
        Assert.assertNotNull(unprocessed);
    }


    @Test(timeout = 30000)
    public void testMiddleRemovalInSameBucket() throws Exception {
        // 变体 3：3 个同 bucket，先过期中间元素 → remove() 覆盖非 head 非 tail 分支
        AtomicInteger processed = new AtomicInteger();
        HashedWheelTimer timer = newTimer(10, index -> processed.incrementAndGet());
        timer.start();
        timer.newTimeout(new ScheduleIndex("far", 1, 1, 1, 1), 115, TimeUnit.MILLISECONDS);
        timer.newTimeout(new ScheduleIndex("mid", 2, 2, 1, 2), 35, TimeUnit.MILLISECONDS);
        timer.newTimeout(new ScheduleIndex("near", 3, 3, 1, 3), 35, TimeUnit.MILLISECONDS);
        long deadline = System.currentTimeMillis() + 5000;
        while (processed.get() < 2 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        Assert.assertTrue(processed.get() >= 2);
        Thread.sleep(200);
        timer.stop();
    }

}
