package com.skrstop.framework.components.core.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;

/**
 * @author 蒋时华
 * @date 2023-12-12 19:05:43
 */
public class SkrstopBusinessException extends SkrstopRuntimeException implements BusinessThrowable {

    public SkrstopBusinessException() {
    }

    public SkrstopBusinessException(String message) {
        super(message);
    }

    public SkrstopBusinessException(Throwable throwable) {
        super(throwable);
    }

    public SkrstopBusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SkrstopBusinessException(IResult iResult) {
        super(iResult);
    }

    public SkrstopBusinessException(IResult iResult, Throwable throwable) {
        super(iResult, throwable);
    }

    public SkrstopBusinessException(IResult iResult, String message) {
        super(iResult, message);
    }

    public SkrstopBusinessException(IResult iResult, String message, Throwable throwable) {
        super(iResult, message, throwable);
    }
}
