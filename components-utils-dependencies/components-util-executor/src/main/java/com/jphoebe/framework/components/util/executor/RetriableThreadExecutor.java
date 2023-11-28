package com.jphoebe.framework.components.util.executor;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Retriable Thread Executor
 *
 * @author 蒋时华
 * @version 27/11/2018.
 */
@ThreadSafe
@Slf4j
public class RetriableThreadExecutor<T> {

    private String name;
    private RetriableAttribute attribute;
    private Callable<T> callable;
    private HashedWheelTimer hashedWheelTimer;
    private ExecutorCallback<T> callback;
    private AtomicInteger retryTime = new AtomicInteger(1);

    public RetriableThreadExecutor(
            String name,
            Callable<T> callable,
            RetriableAttribute attribute,
            ExecutorCallback<T> callback) {
        this.name = name;
        this.attribute = attribute;
        this.callable = callable;
        this.callback = callback;
        // not null
        assert this.callable != null && this.attribute != null;
        this.hashedWheelTimer = new HashedWheelTimer(1, TimeUnit.SECONDS);

        // log
        log.debug("===>[Task Begin]<===");
        log.debug("Task Name: {}", this.name);
        log.debug("Task Retry Period : {}", this.attribute.retryPeriod);
        log.debug("Task Retry Max Times : {}", this.attribute.maxRetryTimes);
        log.debug("Task Retry TimeUnit : {}", this.attribute.timeUnit);
    }

    void stopTimer() {
        if (hashedWheelTimer != null) {
            hashedWheelTimer.stop();
        }
    }

    public void execute() {

        log.debug("[RTE] executing callable task, times: {}", retryTime.get());
        try {
            // call task
            T t = callable.call();
            log.debug("[RTE] callable task executed succeed.");
            if (callback != null) {
                callback.onCompleted(t);
            }

            // stop timer
            stopTimer();
            log.debug("[RTE] stop execute timer.");

        } catch (Exception e) {
            log.debug("[RTE] callable task execute failed ,exception ", e);
            //
            if (retryTime.get() > attribute.maxRetryTimes) {
                log.debug(
                        "[RTE] callable task already retried times: "
                                + retryTime.get()
                                + " , bigger than setting max retry-times:"
                                + attribute.maxRetryTimes
                                + ", will exit;");
                if (callback != null) {
                    callback.onFailed(e.getMessage());
                }
            } else {

                log.debug(
                        "[RTE] callable task execute failed , will try after time: "
                                + attribute.retryPeriod
                                + " ,TimeUnit:"
                                + attribute.timeUnit.name());

                retryTime.incrementAndGet();
                // failed ,retry
                hashedWheelTimer.newTimeout(
                        new TimerTask() {
                            @Override
                            public void run(Timeout timeout) throws Exception {
                                log.debug("[RTE] Retry execute callable task now ~");
                                RetriableThreadExecutor.this.execute();
                            }
                        },
                        this.attribute.retryPeriod,
                        this.attribute.timeUnit);
            }
        }
    }

    void upgradeRetryTimes() {
        retryTime.incrementAndGet();
    }

    public interface ExecutorCallback<T> {

        /**
         * Invoked On Completed
         *
         * @param result result
         * @see T
         */
        void onCompleted(T result);

        /**
         * Invoked On Failed
         *
         * @param message error message
         */
        void onFailed(String message);
    }

    public static class RetriableAttribute {
        private int maxRetryTimes;
        private long retryPeriod;
        private TimeUnit timeUnit;

        public RetriableAttribute(int maxRetryTimes, long retryPeriod, TimeUnit timeUnit) {
            this.maxRetryTimes = maxRetryTimes;
            this.retryPeriod = retryPeriod;
            this.timeUnit = timeUnit;
        }
    }
}
