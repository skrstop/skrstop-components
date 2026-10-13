package com.skrstop.framework.components.util.stress.task;

import com.skrstop.framework.components.util.stress.result.StressResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class StressTesterTest {

    @Test
    public void testThreeArgOverload() {
        // 3 参数重载 → 默认预热 1600 次
        StressTester tester = new StressTester();
        StressResult result = tester.test(2, 4, () -> {
        });
        Assert.assertEquals(4, result.getTotalRequests());
        Assert.assertEquals(2, result.getConcurrencyLevel());
        // 每个线程 2 次任务 → 共 4 条耗时记录
        Assert.assertEquals(4, result.getAllTimes().size());
    }

    @Test
    public void testNullTask() {
        // null 任务 → 使用空任务
        StressTester tester = new StressTester();
        StressResult result = tester.test(2, 2, null, 0);
        Assert.assertEquals(2, result.getTotalRequests());
        Assert.assertEquals(0, result.getFailedRequests());
    }

    @Test
    public void testTaskThrows() {
        // 任务抛异常 → failedRequests 统计
        StressTester tester = new StressTester();
        StressResult result = tester.test(2, 4, new StressTask() {
            @Override
            public void doTask() throws Exception {
                throw new IllegalStateException("fail");
            }
        }, 0);
        Assert.assertEquals(4, result.getFailedRequests());
    }

    @Test
    public void testWarmUpTaskThrows() {
        // 预热阶段任务抛异常 → warmUp catch(Exception)
        StressTester tester = new StressTester();
        StressResult result = tester.test(1, 2, new StressTask() {
            private int count = 0;

            @Override
            public void doTask() throws Exception {
                count++;
                throw new IllegalStateException("warm up fail");
            }
        }, 3);
        Assert.assertEquals(2, result.getFailedRequests());
    }

    @Test
    public void testInterruptedException() {
        // await 期间中断主线程 → InterruptedException 被记录
        Thread main = Thread.currentThread();
        Thread interrupter = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            main.interrupt();
        });
        interrupter.start();
        try {
            StressTester tester = new StressTester();
            StressResult result = tester.test(2, 4, () -> Thread.sleep(300), 0);
            Assert.assertNotNull(result);
        } finally {
            Thread.interrupted();
            try {
                interrupter.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
