package com.zoe.framework.components.core.exception;

import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.exception.core.BusinessThrowable;
import com.zoe.framework.components.core.exception.core.data.ThrowableData;

/**
 * @author 蒋时华
 * @date 2023-12-12 19:05:43
 */
public class ZoeBusinessDataException extends ZoeDataRuntimeException implements BusinessThrowable {

    public ZoeBusinessDataException(ThrowableData throwableData) {
        super(throwableData);
    }

    public ZoeBusinessDataException(String message, ThrowableData throwableData) {
        super(message, throwableData);
    }

    public ZoeBusinessDataException(Throwable throwable, ThrowableData throwableData) {
        super(throwable, throwableData);
    }

    public ZoeBusinessDataException(String message, Throwable throwable, ThrowableData throwableData) {
        super(message, throwable, throwableData);
    }

    public ZoeBusinessDataException(IResult iResult, ThrowableData throwableData) {
        super(iResult, throwableData);
    }

    public ZoeBusinessDataException(IResult iResult, Throwable throwable, ThrowableData throwableData) {
        super(iResult, throwable, throwableData);
    }

    public ZoeBusinessDataException(IResult iResult, String message, ThrowableData throwableData) {
        super(iResult, message, throwableData);
    }

    public ZoeBusinessDataException(IResult iResult, String message, Throwable throwable, ThrowableData throwableData) {
        super(iResult, message, throwable, throwableData);
    }
}
