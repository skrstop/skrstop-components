package com.zoe.framework.components.core.exception.defined.illegal;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 无法支持的异常
 *
 * @author 蒋时华
 * @date 2020-05-02 23:11:49
 */
public class NotSupportedException extends ZoeRuntimeException {

    public NotSupportedException() {
        super(CommonExceptionCode.NOT_SUPPORTED);
    }

    public NotSupportedException(Throwable throwable) {
        super(CommonExceptionCode.NOT_SUPPORTED, throwable);
    }

    public NotSupportedException(String message) {
        super(CommonExceptionCode.NOT_SUPPORTED, message);
    }

    public NotSupportedException(String message, Throwable throwable) {
        super(CommonExceptionCode.NOT_SUPPORTED, message, throwable);
    }
}
