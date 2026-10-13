package com.skrstop.framework.components.util.stress;

import com.skrstop.framework.components.util.stress.task.StressTask;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class StressThreadWorkerTest {

    @Test
    public void testDoRunSuccess() throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(1);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger failed = new AtomicInteger();
        AtomicInteger executed = new AtomicInteger();
        StressContext context = new StressContext();
        context.setThreadStartBarrier(barrier);
        context.setThreadEndLatch(latch);
        context.setFailedCounter(failed);
        context.setTestService(() -> executed.incrementAndGet());

        StressThreadWorker worker = new StressThreadWorker(context, 3);
        worker.run();
        Assert.assertTrue(latch.await(1, TimeUnit.SECONDS));
        Assert.assertEquals(3, executed.get());
        Assert.assertEquals(0, failed.get());
        List<Long> times = worker.getEveryTimes();
        Assert.assertEquals(3, times.size());
    }

    @Test
    public void testDoRunTaskThrows() throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(1);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger failed = new AtomicInteger();
        StressContext context = new StressContext();
        context.setThreadStartBarrier(barrier);
        context.setThreadEndLatch(latch);
        context.setFailedCounter(failed);
        context.setTestService(new StressTask() {
            @Override
            public void doTask() throws Exception {
                throw new IllegalStateException("task fail");
            }
        });

        StressThreadWorker worker = new StressThreadWorker(context, 2);
        worker.run();
        Assert.assertTrue(latch.await(1, TimeUnit.SECONDS));
        Assert.assertEquals(2, failed.get());
        Assert.assertEquals(2, worker.getEveryTimes().size());
    }

    @Test
    public void testRunBrokenBarrier() {
        // barrier await 抛 BrokenBarrierException → run() 捕获，任务未执行
        CyclicBarrier barrier = new CyclicBarrier(1) {
            @Override
            public int await() throws java.util.concurrent.BrokenBarrierException {
                throw new java.util.concurrent.BrokenBarrierException("mock broken");
            }
        };
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger failed = new AtomicInteger();
        StressContext context = new StressContext();
        context.setThreadStartBarrier(barrier);
        context.setThreadEndLatch(latch);
        context.setFailedCounter(failed);
        context.setTestService(() -> {
        });

        StressThreadWorker worker = new StressThreadWorker(context, 1);
        Thread t = new Thread(worker);
        t.start();
        try {
            t.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // run() 捕获异常，任务未执行，latch 未递减
        Assert.assertFalse(t.isAlive());
        Assert.assertEquals(1, latch.getCount());
    }

}
