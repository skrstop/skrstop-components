package com.zoe.framework.components.core.exception;

import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.exception.core.BusinessThrowable;

/**
 * @author 蒋时华
 * @date 2023-12-12 19:05:43
 */
public class ZoeBusinessException extends ZoeRuntimeException implements BusinessThrowable {

    public ZoeBusinessException() {
    }

    public ZoeBusinessException(String message) {
        super(message);
    }

    public ZoeBusinessException(Throwable throwable) {
        super(throwable);
    }

    public ZoeBusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ZoeBusinessException(IResult iResult) {
        super(iResult);
    }

    public ZoeBusinessException(IResult iResult, Throwable throwable) {
        super(iResult, throwable);
    }

    public ZoeBusinessException(IResult iResult, String message) {
        super(iResult, message);
    }

    public ZoeBusinessException(IResult iResult, String message, Throwable throwable) {
        super(iResult, message, throwable);
    }
}
