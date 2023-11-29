package com.zoe.framework.components.core.exception.defined.illegal;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 非法参数异常
 *
 * @author 蒋时华
 * @date 2019/9/9
 */
public class IllegalArgumentException extends ZoeRuntimeException {

    private static final long serialVersionUID = -8063950290864116310L;

    public IllegalArgumentException() {
        super(CommonExceptionCode.ILLEGAL_ARGUMENT);
    }

    public IllegalArgumentException(Throwable throwable) {
        super(CommonExceptionCode.ILLEGAL_ARGUMENT, throwable);
    }

    public IllegalArgumentException(String message) {
        super(CommonExceptionCode.ILLEGAL_ARGUMENT, message);
    }

    public IllegalArgumentException(String message, Throwable throwable) {
        super(CommonExceptionCode.ILLEGAL_ARGUMENT, message, throwable);
    }

}
