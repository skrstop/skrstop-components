package com.zoe.framework.components.core.exception.defined.coding;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 解码异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class DecodingException extends ZoeRuntimeException {
    private static final long serialVersionUID = -7076726091856995091L;

    public DecodingException() {
        super(CommonExceptionCode.DE_CODING);
    }

    public DecodingException(Throwable throwable) {
        super(CommonExceptionCode.DE_CODING, throwable);
    }

    public DecodingException(String message) {
        super(CommonExceptionCode.DE_CODING, message);
    }

    public DecodingException(String message, Throwable throwable) {
        super(CommonExceptionCode.DE_CODING, message, throwable);
    }
}
