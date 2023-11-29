package com.zoe.framework.components.util.executor.retry.listener;


import com.zoe.framework.components.util.executor.retry.Status;

public interface RetryListener<T> {

    void onEvent(Status<T> status);

}
