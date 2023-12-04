package com.zoe.framework.components.example.starters.simple.exception;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;

/**
 * @author 蒋时华
 * @date 2023-12-01 17:27:01
 */
public class CustomException extends ZoeRuntimeException {

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Throwable throwable) {
        super(throwable);
    }

    public CustomException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CustomException(com.zoe.framework.components.core.common.response.core.IResult IResult) {
        super(IResult);
    }

    public CustomException(com.zoe.framework.components.core.common.response.core.IResult IResult, Throwable throwable) {
        super(IResult, throwable);
    }

    public CustomException(com.zoe.framework.components.core.common.response.core.IResult IResult, String message) {
        super(IResult, message);
    }

    public CustomException(com.zoe.framework.components.core.common.response.core.IResult IResult, String message, Throwable throwable) {
        super(IResult, message, throwable);
    }
}
