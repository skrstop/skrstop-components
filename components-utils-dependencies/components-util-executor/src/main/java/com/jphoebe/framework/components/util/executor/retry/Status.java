package com.jphoebe.framework.components.util.executor.retry;

import java.time.Duration;

public class Status<T> extends AttemptStatus<T> {

    private String id;
    private long startTime;
    private long endTime;
    private String callName;
    private int totalTries;
    private Duration totalElapsedDuration;
    private Exception lastExceptionThatCausedRetry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public int getTotalTries() {
        return totalTries;
    }

    public void setTotalTries(int totalTries) {
        this.totalTries = totalTries;
    }

    public Duration getTotalElapsedDuration() {
        return totalElapsedDuration;
    }

    public void setTotalElapsedDuration(Duration totalElapsedDuration) {
        this.totalElapsedDuration = totalElapsedDuration;
    }

    public Exception getLastExceptionThatCausedRetry() {
        return lastExceptionThatCausedRetry;
    }

    public void setLastExceptionThatCausedRetry(Exception lastExceptionThatCausedRetry) {
        this.lastExceptionThatCausedRetry = lastExceptionThatCausedRetry;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Status{");
        sb.append("id=").append(id);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", callName='").append(callName).append('\'');
        sb.append(", wasSuccessful=").append(wasSuccessful());
        sb.append(", totalTries=").append(totalTries);
        sb.append(", totalElapsedDuration=").append(totalElapsedDuration);
        sb.append(", result=").append(getResult());
        sb.append(", lastExceptionThatCausedRetry=").append(lastExceptionThatCausedRetry);
        sb.append('}');
        return sb.toString();
    }
}
