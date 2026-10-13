package com.skrstop.framework.components.util.executor;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class AsyncRuntimeExecutorTest {

    @SuppressWarnings("unchecked")
    private static void resetPool() throws Exception {
        Field f = AsyncRuntimeExecutor.class.getDeclaredField("asyncThreadPool");
        f.setAccessible(true);
        f.set(null, null);
    }

    @Test(timeout = 30000)
    public void testGetAsyncThreadPool() throws Exception {
        ThreadPoolExecutor pool = AsyncRuntimeExecutor.getAsyncThreadPool();
        Assert.assertNotNull(pool);
        Assert.assertSame(pool, AsyncRuntimeExecutor.getAsyncThreadPool());
        Assert.assertNotNull(AsyncRuntimeExecutor.getAsyncThreadPool(false));

        AtomicInteger ran = new AtomicInteger();
        pool.submit(() -> ran.incrementAndGet()).get(2, TimeUnit.SECONDS);
        Assert.assertEquals(1, ran.get());
        pool.shutdownNow();

        // build=false 且池为空 → 返回 null；随后重新构建
        resetPool();
        Assert.assertNull(AsyncRuntimeExecutor.getAsyncThreadPool(false));
        ThreadPoolExecutor rebuilt = AsyncRuntimeExecutor.getAsyncThreadPool(true);
        Assert.assertNotNull(rebuilt);
        rebuilt.shutdownNow();
    }

    @Test(timeout = 60000)
    public void testRejectionHandler() throws Exception {
        resetPool();
        ThreadPoolExecutor pool = AsyncRuntimeExecutor.getAsyncThreadPool(true);
        int rejected = 0;
        for (int i = 0; i < 600; i++) {
            try {
                pool.submit(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                });
            } catch (RejectedExecutionException e) {
                rejected++;
            }
        }
        System.out.println(">>> rejected count = " + rejected);
        // 队列 256 + 最大 200 线程，600 个任务必然溢出
        Assert.assertTrue("expected rejections, got " + rejected, rejected >= 1);
        pool.shutdownNow();
        // 恢复池给其它测试使用
        resetPool();
        Assert.assertNotNull(AsyncRuntimeExecutor.getAsyncThreadPool());
    }

    @Test
    public void testNamedThreadFactoryViaReflection() throws Exception {
        Class<?> factoryClass = Class.forName(AsyncRuntimeExecutor.class.getName() + "$NamedThreadFactory");
        Constructor<?> twoArg = factoryClass.getDeclaredConstructor(String.class, boolean.class);
        twoArg.setAccessible(true);
        Object factory = twoArg.newInstance("TEST", true);

        Method newThread = factoryClass.getDeclaredMethod("newThread", Runnable.class);
        newThread.setAccessible(true);

        // 提升当前线程优先级 → newThread 内部命中 setPriority(NORM) 分支
        int oldPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        try {
            Thread t1 = (Thread) newThread.invoke(factory, (Runnable) () -> {
            });
            Assert.assertTrue(t1.isDaemon());
            Assert.assertEquals(Thread.NORM_PRIORITY, t1.getPriority());
        } finally {
            Thread.currentThread().setPriority(oldPriority);
        }

        Thread t2 = (Thread) newThread.invoke(factory, (Runnable) () -> {
        });
        Assert.assertEquals(Thread.NORM_PRIORITY, t2.getPriority());

        // 1-arg 构造函数 → 非守护线程
        Constructor<?> oneArg = factoryClass.getDeclaredConstructor(String.class);
        oneArg.setAccessible(true);
        Object factory2 = oneArg.newInstance("SINGLE");
        Thread t3 = (Thread) newThread.invoke(factory2, (Runnable) () -> {
        });
        Assert.assertFalse(t3.isDaemon());
    }


    @Test
    public void testInstantiateAndBuildCombinations() throws Exception {
        new AsyncRuntimeExecutor();
        ThreadPoolExecutor pool = AsyncRuntimeExecutor.getAsyncThreadPool();
        // 池已存在：(F,T) 与 (F,F) 组合
        Assert.assertNotNull(AsyncRuntimeExecutor.getAsyncThreadPool(true));
        Assert.assertNotNull(AsyncRuntimeExecutor.getAsyncThreadPool(false));
        pool.shutdownNow();
    }


    @Test(timeout = 60000)
    public void testInitAsyncThreadPoolDoubleChecked() throws Exception {
        // 反射直接调用 initAsyncThreadPool：第一次构建，第二次命中已存在分支
        resetPool();
        java.lang.reflect.Method init = AsyncRuntimeExecutor.class.getDeclaredMethod("initAsyncThreadPool");
        init.setAccessible(true);
        init.invoke(null);
        ThreadPoolExecutor first = AsyncRuntimeExecutor.getAsyncThreadPool();
        init.invoke(null);
        Assert.assertSame(first, AsyncRuntimeExecutor.getAsyncThreadPool());
        first.shutdownNow();
        resetPool();
        // 恢复
        Assert.assertNotNull(AsyncRuntimeExecutor.getAsyncThreadPool());
    }

}
