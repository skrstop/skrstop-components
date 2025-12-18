package com.skrstop.framework.components.starter.web.exception.core;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义HTTP状态码异常
 *
 * @author 蒋时华
 * @date 2020-05-16 15:53:06
 */
public class SkrstopCustomHttpStatusBusinessException extends SkrstopBusinessException implements CustomHttpStatusException {

    @Getter
    private HttpStatus httpStatus = HttpStatus.OK;

    public SkrstopCustomHttpStatusBusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusBusinessException(Throwable throwable, HttpStatus httpStatus) {
        super(throwable);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusBusinessException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, throwable);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusBusinessException(IResult iResult, HttpStatus httpStatus) {
        super(iResult);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusBusinessException(IResult iResult, Throwable throwable) {
        super(iResult, throwable);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusBusinessException(IResult iResult, String message, HttpStatus httpStatus) {
        super(iResult, message);
        this.httpStatus = httpStatus;
    }

    public SkrstopCustomHttpStatusBusinessException(IResult iResult, String message, HttpStatus httpStatus, Throwable throwable) {
        super(iResult, message, throwable);
        this.httpStatus = httpStatus;
    }
}
