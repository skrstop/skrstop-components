package com.jphoebe.framework.components.util.stress;

import com.jphoebe.framework.components.util.stress.result.SimpleResultFormater;
import com.jphoebe.framework.components.util.stress.task.StressTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 * @date 2018-02-15
 **/
public class StressThreadWorker implements Runnable {

    protected static Logger log = LoggerFactory.getLogger(SimpleResultFormater.class);

    /**
     * 压力测试任务接口
     */
    private StressTask service;
    /**
     * 线程集合点，等所有现存启动完毕在一起请求任务
     * <p>
     * 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。
     * 在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时 CyclicBarrier 很有用。
     * 因为该 barrier 在释放等待线程后可以重用，所以称它为循环 的 barrier。CyclicBarrier 支持一个可选的 Runnable 命令，
     * 在一组线程中的最后一个线程到达之后（但在释放所有线程之前），该命令只在每个屏障点运行一次。
     * 若在继续所有参与线程之前更新共享状态，此屏障操作 很有用。
     */
    private CyclicBarrier threadStartBarrier;
    /**
     * 控制所有线程做完任务后状态，数量为0的时间，所有任务执行完毕
     */
    private CountDownLatch threadEndLatch;

    private AtomicInteger failedCounter = null;

    private int count;

    private List<Long> everyTimes;

    @Override
    public void run() {
        try {
            this.threadStartBarrier.await();
            this.doRun();
        } catch (Exception e) {
            log.error("Test Exception", e);
            e.printStackTrace();
        }

    }

    protected void doRun() throws Exception {
        for (int i = 0; i < this.count; ++i) {
            long start = System.nanoTime();

            try {
                this.service.doTask();
            } catch (Throwable throwable) {
                this.failedCounter.incrementAndGet();
            } finally {
                long end = System.nanoTime();
                long limit = end - start;
                this.everyTimes.add(limit);
            }
        }
        this.threadEndLatch.countDown();
    }


    public StressThreadWorker(StressContext stressContext, int count) {
        this.threadStartBarrier = stressContext.getThreadStartBarrier();
        this.threadEndLatch = stressContext.getThreadEndLatch();
        this.failedCounter = stressContext.getFailedCounter();
        this.count = count;
        this.everyTimes = new ArrayList(count);
        this.service = stressContext.getTestService();
    }


    public List<Long> getEveryTimes() {
        return this.everyTimes;
    }
}
