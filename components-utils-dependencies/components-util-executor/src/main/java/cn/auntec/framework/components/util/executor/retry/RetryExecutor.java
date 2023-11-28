package cn.auntec.framework.components.util.executor.retry;

import cn.auntec.framework.components.util.executor.retry.exception.RetriesExhaustedException;
import cn.auntec.framework.components.util.executor.retry.exception.UnexpectedException;

import java.util.concurrent.Callable;

public interface RetryExecutor<T, S> {

    S execute(Callable<T> callable) throws RetriesExhaustedException, UnexpectedException;

    S execute(Callable<T> callable, String callName)
            throws RetriesExhaustedException, UnexpectedException;
}
