package com.skrstop.framework.components.core.exception.defined.coding;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 编码异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class EncodingException extends SkrstopRuntimeException {

    private static final long serialVersionUID = 6011792890916712679L;

    public EncodingException() {
        super(CommonExceptionCode.ENCODING);
    }

    public EncodingException(Throwable throwable) {
        super(CommonExceptionCode.ENCODING, throwable);
    }

    public EncodingException(String message) {
        super(CommonExceptionCode.ENCODING, message);
    }

    public EncodingException(String message, Throwable throwable) {
        super(CommonExceptionCode.ENCODING, message, throwable);
    }
}
