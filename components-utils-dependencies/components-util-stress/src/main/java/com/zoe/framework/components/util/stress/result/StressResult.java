package com.zoe.framework.components.util.stress.result;


import com.zoe.framework.components.util.stress.StressThreadWorker;

import java.util.List;

/**
 * 测试结果
 *
 * @author 蒋时华
 * @date 2018-02-15
 **/
public class StressResult {
    /**
     * 并发访问数
     */
    private int concurrencyLevel;
    /**
     * 总共请求数
     */
    private int totalRequests;
    /**
     *
     */
    private long testsTakenTime;
    /**
     * 失败的请求次数
     */
    private int failedRequests;
    /**
     *
     */
    private List<Long> allTimes;
    /**
     *
     */
    private List<StressThreadWorker> workers;


    public long getTestsTakenTime() {
        return this.testsTakenTime;
    }

    public int getConcurrencyLevel() {
        return this.concurrencyLevel;
    }

    public void setConcurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }

    public int getTotalRequests() {
        return this.totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public void setTestsTakenTime(long testsTakenTime) {
        this.testsTakenTime = testsTakenTime;
    }

    public int getFailedRequests() {
        return this.failedRequests;
    }

    public void setFailedRequests(int failedRequests) {
        this.failedRequests = failedRequests;
    }

    public List<Long> getAllTimes() {
        return this.allTimes;
    }

    public void setAllTimes(List<Long> allTimes) {
        this.allTimes = allTimes;
    }

    public List<StressThreadWorker> getWorkers() {
        return this.workers;
    }

    public void setWorkers(List<StressThreadWorker> workers) {
        this.workers = workers;
    }
}
