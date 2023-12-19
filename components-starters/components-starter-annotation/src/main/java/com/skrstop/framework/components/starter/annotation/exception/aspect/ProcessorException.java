package com.skrstop.framework.components.starter.annotation.exception.aspect;


import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;

/**
 * AccessLimitException class
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class ProcessorException extends SkrstopRuntimeException {
    private static final long serialVersionUID = 1205620779302704041L;

    public ProcessorException() {
        super(WebAnnotationExceptionCode.NO_PROCESSOR);
    }
}
