package com.jphoebe.framework.components.core.exception.defined.serialization;

import com.jphoebe.framework.components.core.exception.AuntecRuntimeException;
import com.jphoebe.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 反序列化异常
 *
 * @author 蒋时华
 */
public class DeserializationException extends AuntecRuntimeException {

    private static final long serialVersionUID = -211821078080239182L;

    public DeserializationException() {
        super(CommonExceptionCode.DE_SERIALIZATION);
    }

    public DeserializationException(Throwable throwable) {
        super(CommonExceptionCode.DE_SERIALIZATION, throwable);
    }

    public DeserializationException(String message) {
        super(CommonExceptionCode.DE_SERIALIZATION, message);
    }

    public DeserializationException(String message, Throwable throwable) {
        super(CommonExceptionCode.DE_SERIALIZATION, message, throwable);
    }
}
