package com.skrstop.framework.components.core.exception.defined.illegal;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 空指针异常
 *
 * @author 蒋时华
 * @date 2019/9/9
 */
public class NullPointerException extends SkrstopRuntimeException {

    public NullPointerException() {
        super(CommonExceptionCode.NULL_POINTER);
    }

    public NullPointerException(Throwable throwable) {
        super(CommonExceptionCode.NULL_POINTER, throwable);
    }

    public NullPointerException(String message) {
        super(CommonExceptionCode.NULL_POINTER, message);
    }

    public NullPointerException(String message, Throwable throwable) {
        super(CommonExceptionCode.NULL_POINTER, message, throwable);
    }
}
