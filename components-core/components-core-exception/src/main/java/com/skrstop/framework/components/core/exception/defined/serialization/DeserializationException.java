package com.skrstop.framework.components.core.exception.defined.serialization;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;

import java.io.Serial;

/**
 * 反序列化异常
 *
 * @author 蒋时华
 */
public class DeserializationException extends SkrstopRuntimeException {

    @Serial
    private static final long serialVersionUID = -211821078080239182L;

    public DeserializationException() {
        super(CommonExceptionCode.DESERIALIZATION);
    }

    public DeserializationException(Throwable throwable) {
        super(CommonExceptionCode.DESERIALIZATION, throwable);
    }

    public DeserializationException(String message) {
        super(CommonExceptionCode.DESERIALIZATION, message);
    }

    public DeserializationException(String message, Throwable throwable) {
        super(CommonExceptionCode.DESERIALIZATION, message, throwable);
    }
}
