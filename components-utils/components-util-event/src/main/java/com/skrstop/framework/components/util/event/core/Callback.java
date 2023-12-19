package com.skrstop.framework.components.util.event.core;

/**
 * @author 蒋时华
 * @date 2021-07-23 11:31:06
 */
public interface Callback {

    /**
     * 验证不合法回调
     */
    default void onUnValid() {

    }

    /**
     * before event
     */
    default void onBefore() {
    }

    /**
     * after event
     */
    default void onAfter() {
    }

    /**
     * 成功回调
     */
    default void onSuccess() {

    }

    /**
     * 失败回调
     *
     * @param e
     */
    default void onFailure(Throwable e) {

    }

    /**
     * 失败回调
     */
    default void onFailure() {
        onFailure(null);
    }

    /**
     * 处理中回调
     */
    default void onExecute() {

    }

    /**
     * 超时回调
     */
    default void onTimeout() {

    }

    /**
     * 是否继续
     */
    default boolean onContinue() {
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
