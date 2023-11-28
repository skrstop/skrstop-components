package com.jphoebe.framework.components.util.executor.retry.listener;


import com.jphoebe.framework.components.util.executor.retry.Status;

public interface RetryListener<T> {

    void onEvent(Status<T> status);

}
