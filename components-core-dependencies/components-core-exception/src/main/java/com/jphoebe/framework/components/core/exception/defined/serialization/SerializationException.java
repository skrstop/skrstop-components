package com.jphoebe.framework.components.core.exception.defined.serialization;

import com.jphoebe.framework.components.core.exception.AuntecRuntimeException;
import com.jphoebe.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 序列化异常
 *
 * @author 蒋时华
 */
public class SerializationException extends AuntecRuntimeException {

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
