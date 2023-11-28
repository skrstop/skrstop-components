package cn.auntec.framework.components.starter.annotation.exception;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;

/**
 * ControllerDeprecatedException class
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class ControllerDeprecatedException extends AuntecRuntimeException {
    private static final long serialVersionUID = 8431106099116011149L;

    public ControllerDeprecatedException() {
        super(WebAnnotationExceptionCode.CONTROLLER_DEPRECATED);
    }

    public ControllerDeprecatedException(String message) {
        super(WebAnnotationExceptionCode.CONTROLLER_DEPRECATED, message);
    }
}
