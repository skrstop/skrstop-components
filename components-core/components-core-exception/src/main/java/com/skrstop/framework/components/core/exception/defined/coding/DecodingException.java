package com.skrstop.framework.components.core.exception.defined.coding;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;

import java.io.Serial;

/**
 * 解码异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class DecodingException extends SkrstopRuntimeException {
    @Serial
    private static final long serialVersionUID = -7076726091856995091L;

    public DecodingException() {
        super(CommonExceptionCode.DECODING);
    }

    public DecodingException(Throwable throwable) {
        super(CommonExceptionCode.DECODING, throwable);
    }

    public DecodingException(String message) {
        super(CommonExceptionCode.DECODING, message);
    }

    public DecodingException(String message, Throwable throwable) {
        super(CommonExceptionCode.DECODING, message, throwable);
    }
}
