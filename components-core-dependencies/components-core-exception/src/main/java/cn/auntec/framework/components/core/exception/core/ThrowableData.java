package cn.auntec.framework.components.core.exception.core;

import cn.auntec.framework.components.core.common.serializable.SerializableBean;

/**
 * @author 蒋时华
 * @date 2020-05-02 23:02:24
 */
public interface ThrowableData {

    /**
     * get detail string message
     *
     * @return string
     */
    String getDetailMessage();

    /**
     * get detail object message
     *
     * @param <T>
     * @return
     */
    <T extends SerializableBean> T getDataMessage();

}
