package com.skrstop.framework.components.starter.annotation.exception.aspect;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;

/**
 * IntranetLimitException class
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class IntranetLimitException extends SkrstopRuntimeException {
    private static final long serialVersionUID = -5339821299605718859L;

    public IntranetLimitException() {
        super(WebAnnotationExceptionCode.INTRANET_LIMIT);
    }
}
