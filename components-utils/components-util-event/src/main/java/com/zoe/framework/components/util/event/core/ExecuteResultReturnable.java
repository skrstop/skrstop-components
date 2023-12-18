package com.zoe.framework.components.util.event.core;

/**
 * @author 蒋时华
 * @date 2023-12-15 13:58:10
 */
public interface ExecuteResultReturnable<T, R> {

    R execute(T obj);

}
