package com.jphoebe.framework.components.util.event.core;

/**
 * @author 蒋时华
 * @date 2021-07-23 11:31:06
 */
public interface CallbackResult<T> {

    /**
     * 验证不合法回调
     *
     * @param obj
     */
    default void onUnValid(T obj) {

    }

    /**
     * before event
     *
     * @param obj
     */
    default void onBefore(T obj) {
    }

    /**
     * after event
     *
     * @param obj
     */
    default void onAfter(T obj) {
    }

    /**
     * 成功回调
     *
     * @param obj
     */
    default void onSuccess(T obj) {

    }

    /**
     * 失败回调
     *
     * @param e
     * @param obj
     */
    default void onFailure(Throwable e, T obj) {

    }

    /**
     * 失败回调
     *
     * @param obj
     */
    default void onFailure(T obj) {
        onFailure(null, obj);
    }

    /**
     * 处理中回调
     *
     * @param obj
     */
    default void onExecute(T obj) {

    }

    /**
     * 超时回调
     *
     * @param obj
     */
    default void onTimeout(T obj) {

    }

    /**
     * 是否继续
     */
    default boolean onContinue(T obj) {
        return true;
    }

    /**
     * 日志输出
     *
     * @param log   日志信息
     * @param error 是否错误日志
     */
    default void onLogOutput(String log, Boolean error) {

    }

}
