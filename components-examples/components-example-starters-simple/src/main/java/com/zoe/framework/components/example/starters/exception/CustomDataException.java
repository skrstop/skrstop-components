package com.zoe.framework.components.example.starters.exception;

import com.zoe.framework.components.core.exception.ZoeRuntimeDataException;

/**
 * @author 蒋时华
 * @date 2023-12-01 17:27:01
 */
public class CustomDataException extends ZoeRuntimeDataException {

    public CustomDataException() {
    }

    public CustomDataException(String message) {
        super(message);
    }

    public CustomDataException(Throwable throwable) {
        super(throwable);
    }

    public CustomDataException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CustomDataException(com.zoe.framework.components.core.common.response.core.IResult IResult) {
        super(IResult);
    }

    public CustomDataException(com.zoe.framework.components.core.common.response.core.IResult IResult, Throwable throwable) {
        super(IResult, throwable);
    }

    public CustomDataException(com.zoe.framework.components.core.common.response.core.IResult IResult, String message) {
        super(IResult, message);
    }

    public CustomDataException(com.zoe.framework.components.core.common.response.core.IResult IResult, String message, Throwable throwable) {
        super(IResult, message, throwable);
    }
}
