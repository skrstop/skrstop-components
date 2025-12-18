package com.skrstop.framework.components.starter.web.exception.core;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.SkrstopDataRuntimeException;
import com.skrstop.framework.components.core.exception.core.data.ThrowableData;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义HTTP状态码异常
 *
 * @author 蒋时华
 * @date 2020-05-16 15:53:06
 */
public class SkrstopCustomHttpStatusDataException extends SkrstopDataRuntimeException implements CustomHttpStatusException {

    @Getter
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public SkrstopCustomHttpStatusDataException(ThrowableData throwableData, HttpStatus httpStatus) {
        super(throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(String message, HttpStatus httpStatus, ThrowableData throwableData) {
        super(message, throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(Throwable throwable, ThrowableData throwableData) {
        super(throwable, throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(String message, Throwable throwable, ThrowableData throwableData, HttpStatus httpStatus) {
        super(message, throwable, throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(IResult iResult, ThrowableData throwableData, HttpStatus httpStatus) {
        super(iResult, throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(IResult iResult, Throwable throwable, ThrowableData throwableData, HttpStatus httpStatus) {
        super(iResult, throwable, throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(IResult iResult, String message, ThrowableData throwableData, HttpStatus httpStatus) {
        super(iResult, message, throwableData);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusDataException(IResult iResult, String message, Throwable throwable, ThrowableData throwableData, HttpStatus httpStatus) {
        super(iResult, message, throwable, throwableData);
        this.httpStatus = httpStatus;
    }
}
