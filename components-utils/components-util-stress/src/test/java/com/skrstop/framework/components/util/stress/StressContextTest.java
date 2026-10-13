package com.skrstop.framework.components.util.stress;

import com.skrstop.framework.components.util.stress.task.StressTask;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class StressContextTest {

    @Test
    public void testGettersSetters() {
        StressContext context = new StressContext();
        context.setEveryThreadCount(5);
        Assert.assertEquals(5, context.getEveryThreadCount());

        CyclicBarrier barrier = new CyclicBarrier(1);
        context.setThreadStartBarrier(barrier);
        Assert.assertSame(barrier, context.getThreadStartBarrier());

        CountDownLatch latch = new CountDownLatch(1);
        context.setThreadEndLatch(latch);
        Assert.assertSame(latch, context.getThreadEndLatch());

        AtomicInteger counter = new AtomicInteger();
        context.setFailedCounter(counter);
        Assert.assertSame(counter, context.getFailedCounter());

        StressTask task = () -> {
        };
        context.setTestService(task);
        Assert.assertSame(task, context.getTestService());
    }

}
