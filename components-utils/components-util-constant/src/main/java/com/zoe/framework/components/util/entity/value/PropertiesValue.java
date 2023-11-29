package com.zoe.framework.components.util.entity.value;

import java.io.Serializable;

/**
 * @author 蒋时华
 * @date 2021-04-21 10:19:53
 */
public interface PropertiesValue extends Serializable {

    /**
     * 获取可选值
     *
     * @return
     */
    Object getValue();

}
