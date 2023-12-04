package com.zoe.framework.components.starter.annotation.exception.aspect;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;

/**
 * PrivacyInfoException class
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class PrivacyInfoException extends ZoeRuntimeException {
    private static final long serialVersionUID = 8795029052309556328L;

    public PrivacyInfoException() {
        super(WebAnnotationExceptionCode.PRIVACY_INFO);
    }
}
