package com.skrstop.framework.components.starter.annotation.exception.aspect;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;

/**
 * ControllerDeprecatedException class
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class ControllerDeprecatedException extends SkrstopRuntimeException {
    private static final long serialVersionUID = 8431106099116011149L;

    public ControllerDeprecatedException() {
        super(WebAnnotationExceptionCode.CONTROLLER_DEPRECATED);
    }

    public ControllerDeprecatedException(String message) {
        super(WebAnnotationExceptionCode.CONTROLLER_DEPRECATED, message);
    }
}
