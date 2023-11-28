package com.jphoebe.framework.components.util.stress;

import com.jphoebe.framework.components.util.stress.task.StressTask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 * @date 2018-02-15
 **/
public class StressContext {
    private int everyThreadCount;
    private CyclicBarrier threadStartBarrier;
    private CountDownLatch threadEndLatch;
    private AtomicInteger failedCounter;
    private StressTask stressTask;


    public int getEveryThreadCount() {
        return this.everyThreadCount;
    }

    public void setEveryThreadCount(int everyThreadCount) {
        this.everyThreadCount = everyThreadCount;
    }

    public CyclicBarrier getThreadStartBarrier() {
        return this.threadStartBarrier;
    }

    public void setThreadStartBarrier(CyclicBarrier threadStartBarrier) {
        this.threadStartBarrier = threadStartBarrier;
    }

    public CountDownLatch getThreadEndLatch() {
        return this.threadEndLatch;
    }

    public void setThreadEndLatch(CountDownLatch threadEndLatch) {
        this.threadEndLatch = threadEndLatch;
    }

    public AtomicInteger getFailedCounter() {
        return this.failedCounter;
    }

    public void setFailedCounter(AtomicInteger failedCounter) {
        this.failedCounter = failedCounter;
    }

    public StressTask getTestService() {
        return this.stressTask;
    }

    public void setTestService(StressTask stressTask) {
        this.stressTask = stressTask;
    }
}
