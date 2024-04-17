package com.skrstop.framework.components.util.event.core;

/**
 * @author 蒋时华
 * @date 2021-07-23 11:31:06
 */
public interface CallbackResultReturn<T, R> {

    /**
     * 验证不合法回调
     *
     * @param obj
     */
    default R onUnValid(T obj) {
        return null;
    }

    /**
     * before event
     *
     * @param obj
     */
    default R onBefore(T obj) {
        return null;
    }

    /**
     * after event
     *
     * @param obj
     */
    default R onAfter(T obj) {
        return null;
    }

    /**
     * 成功回调
     *
     * @param obj
     */
    default R onSuccess(T obj) {
        return null;
    }

    /**
     * 失败回调
     *
     * @param e
     * @param obj
     */
    default R onFailure(Throwable e, T obj) {
        return null;
    }

    /**
     * 失败回调
     *
     * @param obj
     */
    default R onFailure(T obj) {
        return onFailure(null, obj);
    }

    /**
     * 处理中回调
     *
     * @param obj
     */
    default R onExecute(T obj) {
        return null;
    }

    /**
     * 超时回调
     *
     * @param obj
     */
    default R onTimeout(T obj) {
        return null;
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
