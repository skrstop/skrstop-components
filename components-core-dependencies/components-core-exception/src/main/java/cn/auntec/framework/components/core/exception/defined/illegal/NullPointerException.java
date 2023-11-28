package cn.auntec.framework.components.core.exception.defined.illegal;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 空指针异常
 *
 * @author 蒋时华
 * @date 2019/9/9
 */
public class NullPointerException extends AuntecRuntimeException {

    public NullPointerException() {
        super(CommonExceptionCode.PARAMETER);
    }

    public NullPointerException(Throwable throwable) {
        super(CommonExceptionCode.PARAMETER, throwable);
    }

    public NullPointerException(String message) {
        super(CommonExceptionCode.PARAMETER, message);
    }

    public NullPointerException(String message, Throwable throwable) {
        super(CommonExceptionCode.PARAMETER, message, throwable);
    }
}
