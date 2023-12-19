package com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts;

/**
 * @author 蒋时华
 * @date 2022-03-01 15:46:37
 */
public interface ProcessorAssert<T> {

    /**
     * 是否支持
     *
     * @param currentValue 当前值
     * @return
     */
    boolean isSupported(T currentValue);

}
