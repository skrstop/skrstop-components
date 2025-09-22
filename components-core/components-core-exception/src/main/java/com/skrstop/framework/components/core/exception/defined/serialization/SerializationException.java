package com.skrstop.framework.components.core.exception.defined.serialization;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;

import java.io.Serial;

/**
 * 序列化异常
 *
 * @author 蒋时华
 */
public class SerializationException extends SkrstopRuntimeException {

    @Serial
    private static final long serialVersionUID = 6082512356376518249L;

    public SerializationException() {
        super(CommonExceptionCode.SERIALIZATION);
    }

    public SerializationException(Throwable throwable) {
        super(CommonExceptionCode.SERIALIZATION, throwable);
    }

    public SerializationException(String message) {
        super(CommonExceptionCode.SERIALIZATION, message);
    }

    public SerializationException(String message, Throwable throwable) {
        super(CommonExceptionCode.SERIALIZATION, message, throwable);
    }
}
