package cn.auntec.framework.components.util.executor.retry.listener;


import cn.auntec.framework.components.util.executor.retry.Status;

public interface RetryListener<T> {

    void onEvent(Status<T> status);

}
