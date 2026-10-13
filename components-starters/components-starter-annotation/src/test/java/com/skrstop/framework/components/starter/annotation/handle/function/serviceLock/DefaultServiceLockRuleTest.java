package com.skrstop.framework.components.starter.annotation.handle.function.serviceLock;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.annotation.anno.function.ServiceLock;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultServiceLockRuleTest {

    static class Target {
        @ServiceLock(lockId = "k", fastFail = true, timeoutMs = 10)
        public void fastFail() {
        }

        @ServiceLock(lockId = "k", fastFail = false)
        public void slow() {
        }

        @ServiceLock(fastFail = true, timeoutMs = 100)
        public void noLockId() {
        }
    }

    private static ServiceLock anno(String method) throws Exception {
        return Target.class.getMethod(method).getAnnotation(ServiceLock.class);
    }

    private static MethodInvocation invocation() {
        try {
            Method m = Target.class.getMethod("slow");
            return new ReflectiveMethodInvocation(
                    new Target(), new Target(), m, new Object[0], null, Collections.emptyList()) {
                @Override
                public Object proceed() {
                    return "ok";
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final DefaultServiceLockRule rule = new DefaultServiceLockRule();

    @SuppressWarnings("unchecked")
    private java.util.concurrent.ConcurrentHashMap<String, Lock> lockMap() throws Exception {
        Field f = DefaultServiceLockRule.class.getDeclaredField("lockMap");
        f.setAccessible(true);
        return (java.util.concurrent.ConcurrentHashMap<String, Lock>) f.get(rule);
    }

    @After
    public void tearDown() throws Exception {
        for (Lock lock : lockMap().values()) {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException ignored) {
            }
        }
        lockMap().clear();
    }

    @Test
    public void testTryLockSuccess() throws Throwable {
        // 无锁竞争 → tryLock 成功执行
        Object result = rule.handle(invocation(), anno("noLockId"));
        Assert.assertEquals("ok", result);
    }

    @Test
    public void testFastFailAndBlockUntilRelease() throws Throwable {
        ReentrantLock lock = new ReentrantLock(true);
        lockMap().put("k", lock);
        // 锁必须由其他线程持有（ReentrantLock 可重入，同线程 tryLock 会成功）
        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            } finally {
                lock.unlock();
            }
        });
        holder.start();
        while (!lock.isLocked()) {
            Thread.yield();
        }
        try {
            // fastFail=true → tryLock 超时失败 → BUSY
            try {
                rule.handle(invocation(), anno("fastFail"));
                Assert.fail();
            } catch (SkrstopRuntimeException e) {
                Assert.assertEquals(CommonResultCode.BUSY.getCode(), e.getCode());
            }
            // fastFail=false → 阻塞等锁，持有线程释放后继续执行
            Object result = rule.handle(invocation(), anno("slow"));
            Assert.assertEquals("ok", result);
        } finally {
            holder.join();
        }
    }
}
