package com.skrstop.framework.components.core.exception.core;

import com.skrstop.framework.components.core.exception.core.data.ThrowableData;

/**
 * 可以携带数据的异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public interface SkrstopDataThrowable extends SkrstopThrowable {

    ThrowableData getThrowableData();

}
