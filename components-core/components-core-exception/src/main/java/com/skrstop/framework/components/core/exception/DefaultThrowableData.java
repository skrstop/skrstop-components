package com.skrstop.framework.components.core.exception;

import com.skrstop.framework.components.core.exception.core.data.ThrowableData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 蒋时华
 * @date 2020-05-02 23:05:07
 */
@Getter
@Setter
public class DefaultThrowableData implements Serializable, ThrowableData<Object> {

    @Serial
    private static final long serialVersionUID = 1372837245824421920L;

    private Object data;
}
