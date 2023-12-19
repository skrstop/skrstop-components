package com.skrstop.framework.components.core.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.core.exception.core.data.ThrowableData;

/**
 * @author 蒋时华
 * @date 2023-12-12 19:05:43
 */
public class SkrstopBusinessDataException extends SkrstopDataRuntimeException implements BusinessThrowable {

    public SkrstopBusinessDataException(ThrowableData throwableData) {
        super(throwableData);
    }

    public SkrstopBusinessDataException(String message, ThrowableData throwableData) {
        super(message, throwableData);
    }

    public SkrstopBusinessDataException(Throwable throwable, ThrowableData throwableData) {
        super(throwable, throwableData);
    }

    public SkrstopBusinessDataException(String message, Throwable throwable, ThrowableData throwableData) {
        super(message, throwable, throwableData);
    }

    public SkrstopBusinessDataException(IResult iResult, ThrowableData throwableData) {
        super(iResult, throwableData);
    }

    public SkrstopBusinessDataException(IResult iResult, Throwable throwable, ThrowableData throwableData) {
        super(iResult, throwable, throwableData);
    }

    public SkrstopBusinessDataException(IResult iResult, String message, ThrowableData throwableData) {
        super(iResult, message, throwableData);
    }

    public SkrstopBusinessDataException(IResult iResult, String message, Throwable throwable, ThrowableData throwableData) {
        super(iResult, message, throwable, throwableData);
    }
}
