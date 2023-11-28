package com.jphoebe.framework.components.core.exception.defined.rpc;


import com.jphoebe.framework.components.core.exception.AuntecRuntimeException;
import com.jphoebe.framework.components.core.exception.common.CommonExceptionCode;

/**
 * http处理异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class HttpHandleException extends AuntecRuntimeException {

    private static final long serialVersionUID = -8981982891496434191L;

    public HttpHandleException() {
        super(CommonExceptionCode.HTTP_HANDLE);
    }

    public HttpHandleException(Throwable throwable) {
        super(CommonExceptionCode.HTTP_HANDLE, throwable);
    }

    public HttpHandleException(String message) {
        super(CommonExceptionCode.HTTP_HANDLE, message);
    }

    public HttpHandleException(String message, Throwable throwable) {
        super(CommonExceptionCode.HTTP_HANDLE, message, throwable);
    }
}
