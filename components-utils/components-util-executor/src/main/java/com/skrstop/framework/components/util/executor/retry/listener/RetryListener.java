package com.skrstop.framework.components.util.executor.retry.listener;


import com.skrstop.framework.components.util.executor.retry.Status;

public interface RetryListener<T> {

    void onEvent(Status<T> status);

}
