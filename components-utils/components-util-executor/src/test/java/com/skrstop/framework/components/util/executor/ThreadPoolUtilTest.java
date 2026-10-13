package com.skrstop.framework.components.util.executor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class ThreadPoolUtilTest {

    private final List<ThreadPoolExecutor> pools = new ArrayList<>();

    private ThreadPoolExecutor create(ThreadPoolExecutor pool) {
        pools.add(pool);
        return pool;
    }

    @After
    public void tearDown() {
        for (ThreadPoolExecutor pool : pools) {
            pool.shutdownNow();
        }
        pools.clear();
    }

    @Test
    public void testFixedPools() throws Exception {
        Assert.assertNotNull(create(ThreadPoolUtil.newFixedThreadPool(2)));
        Assert.assertNotNull(create(ThreadPoolUtil.newFixedThreadPool(2, ThreadPoolUtil.buildQueue(5))));
        Assert.assertNotNull(create(ThreadPoolUtil.newFixedThreadPool(2, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory())));
        Assert.assertNotNull(create(ThreadPoolUtil.newFixedThreadPool(2, ThreadPoolUtil.buildQueue(5), ThreadPoolUtil.policyCallerRuns())));
        Assert.assertNotNull(create(ThreadPoolUtil.newFixedThreadPool(2, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory(), ThreadPoolUtil.policyAbort())));

        ThreadPoolExecutor pool = create(ThreadPoolUtil.newFixedThreadPool(1, ThreadPoolUtil.buildQueue(1)));
        AtomicInteger ran = new AtomicInteger();
        pool.submit(() -> ran.incrementAndGet()).get(2, TimeUnit.SECONDS);
        Assert.assertEquals(1, ran.get());
    }

    @Test
    public void testCachedPools() throws Exception {
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2)));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2, ThreadPoolUtil.buildQueue(5))));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2, ThreadPoolUtil.buildQueue(5), ThreadPoolUtil.policyDiscard())));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory())));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory(), ThreadPoolUtil.policyDiscardOldest())));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(2, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory(), ThreadPoolUtil.policyCallerRuns())));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2, 1000, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory(), ThreadPoolUtil.policyCallerRuns())));
        Assert.assertNotNull(create(ThreadPoolUtil.newCachedThreadPool(1, 2, 1000, TimeUnit.MILLISECONDS, ThreadPoolUtil.buildQueue(5), Executors.defaultThreadFactory(), ThreadPoolUtil.policyCallerRuns())));

        ThreadPoolExecutor pool = create(ThreadPoolUtil.newCachedThreadPool(1, 2, ThreadPoolUtil.buildQueue(5)));
        AtomicInteger ran = new AtomicInteger();
        pool.submit(() -> ran.incrementAndGet()).get(2, TimeUnit.SECONDS);
        Assert.assertEquals(1, ran.get());
    }

    @Test
    public void testBuildQueue() throws Exception {
        Assert.assertNotNull(ThreadPoolUtil.buildQueue(10));
        BlockingQueue<Runnable> queue = ThreadPoolUtil.buildQueue(10, false);
        Assert.assertNotNull(queue);
        BlockingQueue<Runnable> priority = ThreadPoolUtil.buildQueue(10, true);
        Assert.assertNotNull(priority);
        BlockingQueue<Runnable> zero = ThreadPoolUtil.buildQueue(0);
        Assert.assertTrue(zero instanceof SynchronousQueue);
        // 优先级队列可直接 offer 并按序取出（元素需 Comparable）
        java.util.Queue<Integer> pq = (java.util.Queue<Integer>) (java.util.Queue<?>) priority;
        pq.offer(2);
        pq.offer(1);
        Assert.assertEquals(Integer.valueOf(1), pq.poll());
        Assert.assertEquals(Integer.valueOf(2), pq.poll());
        // size<0 → 无界优先级队列
        BlockingQueue<Runnable> unboundedPriority = ThreadPoolUtil.buildQueue(-1, true);
        java.util.Queue<Integer> upq = (java.util.Queue<Integer>) (java.util.Queue<?>) unboundedPriority;
        Assert.assertTrue(upq.offer(1));
        // size<0 非优先级 → 无界 LinkedBlockingQueue
        BlockingQueue<Runnable> unbounded = ThreadPoolUtil.buildQueue(-1, false);
        Assert.assertTrue(unbounded.offer(() -> {
        }));
    }

    @Test
    public void testPolicies() {
        Assert.assertNotNull(ThreadPoolUtil.policyAbort());
        Assert.assertNotNull(ThreadPoolUtil.policyDiscard());
        Assert.assertNotNull(ThreadPoolUtil.policyDiscardOldest());
        Assert.assertNotNull(ThreadPoolUtil.policyCallerRuns());
    }


    @Test
    public void testInstantiate() {
        Assert.assertNotNull(new ThreadPoolUtil());
    }

}
