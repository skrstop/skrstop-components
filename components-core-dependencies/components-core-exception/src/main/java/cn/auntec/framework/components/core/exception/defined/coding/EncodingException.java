package cn.auntec.framework.components.core.exception.defined.coding;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 编码异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class EncodingException extends AuntecRuntimeException {

    private static final long serialVersionUID = 6011792890916712679L;

    public EncodingException() {
        super(CommonExceptionCode.EN_CODING);
    }

    public EncodingException(Throwable throwable) {
        super(CommonExceptionCode.EN_CODING, throwable);
    }

    public EncodingException(String message) {
        super(CommonExceptionCode.EN_CODING, message);
    }

    public EncodingException(String message, Throwable throwable) {
        super(CommonExceptionCode.EN_CODING, message, throwable);
    }
}
