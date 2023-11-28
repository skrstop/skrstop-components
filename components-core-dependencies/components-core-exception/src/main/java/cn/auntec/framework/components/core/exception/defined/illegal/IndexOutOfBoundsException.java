package cn.auntec.framework.components.core.exception.defined.illegal;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.core.exception.common.CommonExceptionCode;

/**
 * 指针越界异常
 *
 * @author 蒋时华
 * @date 2019/9/10
 */
public class IndexOutOfBoundsException extends AuntecRuntimeException {

    private static final long serialVersionUID = -8030762224392082863L;

    public IndexOutOfBoundsException() {
        super(CommonExceptionCode.INDEX_OUT_OF_BOUNDS);
    }

    public IndexOutOfBoundsException(Throwable throwable) {
        super(CommonExceptionCode.INDEX_OUT_OF_BOUNDS, throwable);
    }

    public IndexOutOfBoundsException(String message) {
        super(CommonExceptionCode.INDEX_OUT_OF_BOUNDS, message);
    }

    public IndexOutOfBoundsException(String message, Throwable throwable) {
        super(CommonExceptionCode.INDEX_OUT_OF_BOUNDS, message, throwable);
    }
}
