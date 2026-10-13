package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class SpringThreadPoolUtilTest {

    private static final ThreadFactory FACTORY = new ThreadFactory() {
        private final AtomicInteger seq = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "factory-" + seq.incrementAndGet());
        }
    };

    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutorAb();

    static class ThreadPoolExecutorAb implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, java.util.concurrent.ThreadPoolExecutor executor) {
            // noop
        }
    }

    @Test
    public void testFixedPools() throws Exception {
        ThreadPoolTaskExecutor pool = SpringThreadPoolUtil.newFixedThreadSpringPool(1);
        Assert.assertEquals(1, pool.getCorePoolSize());
        Assert.assertEquals(1, pool.getMaxPoolSize());
        Assert.assertEquals(0, pool.getKeepAliveSeconds());
        Assert.assertEquals(-1, pool.getQueueCapacity());
        pool.afterPropertiesSet();
        pool.destroy();

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(2, "prefix-");
        Assert.assertEquals(2, pool.getCorePoolSize());
        Assert.assertEquals("prefix-", pool.getThreadNamePrefix());

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10);
        Assert.assertEquals(10, pool.getQueueCapacity());

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, "p-");
        Assert.assertEquals("p-", pool.getThreadNamePrefix());
        Assert.assertEquals(10, pool.getQueueCapacity());

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, FACTORY);
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, FACTORY, "q-");
        Assert.assertEquals("q-", pool.getThreadNamePrefix());
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, HANDLER);
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, HANDLER, "r-");
        Assert.assertEquals("r-", pool.getThreadNamePrefix());
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, FACTORY, HANDLER);
        Assert.assertNotNull(pool);
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newFixedThreadSpringPool(3, 10, FACTORY, HANDLER, "s-");
        Assert.assertEquals("s-", pool.getThreadNamePrefix());
        Assert.assertNotNull(pool);
        Assert.assertNotNull(pool);
    }

    @Test
    public void testCachedPools() {
        ThreadPoolTaskExecutor pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5);
        Assert.assertEquals(1, pool.getCorePoolSize());
        Assert.assertEquals(5, pool.getMaxPoolSize());
        Assert.assertEquals(60, pool.getKeepAliveSeconds());
        Assert.assertEquals(-1, pool.getQueueCapacity());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, "c-");
        Assert.assertEquals("c-", pool.getThreadNamePrefix());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8);
        Assert.assertEquals(8, pool.getQueueCapacity());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, "d-");
        Assert.assertEquals("d-", pool.getThreadNamePrefix());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, HANDLER);
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, FACTORY);
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, FACTORY, "e-");
        Assert.assertEquals("e-", pool.getThreadNamePrefix());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, FACTORY, HANDLER);
        Assert.assertNotNull(pool);
        Assert.assertNotNull(pool);

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, HANDLER, "f-");
        Assert.assertEquals("f-", pool.getThreadNamePrefix());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 8, FACTORY, HANDLER, "f-");
        Assert.assertEquals("f-", pool.getThreadNamePrefix());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 30, 8, FACTORY, HANDLER);
        Assert.assertEquals(30, pool.getKeepAliveSeconds());

        pool = SpringThreadPoolUtil.newCachedThreadSpringPool(1, 5, 45, 8, FACTORY, HANDLER, "g-");
        Assert.assertEquals(45, pool.getKeepAliveSeconds());
        Assert.assertEquals("g-", pool.getThreadNamePrefix());
    }

}