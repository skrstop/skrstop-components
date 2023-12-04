package com.zoe.framework.components.starter.annotation.exception.aspect;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;

/**
 * IntranetLimitException class
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class IntranetLimitException extends ZoeRuntimeException {
    private static final long serialVersionUID = -5339821299605718859L;

    public IntranetLimitException() {
        super(WebAnnotationExceptionCode.INTRANET_LIMIT);
    }
}
