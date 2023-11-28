package com.jphoebe.framework.components.core.exception;

import com.jphoebe.framework.components.core.common.response.core.IDataPageResult;
import com.jphoebe.framework.components.core.common.response.core.IDataResult;
import com.jphoebe.framework.components.core.exception.core.BusinessServiceThrowable;
import com.jphoebe.framework.components.core.exception.core.ServiceDataAuntecThrowable;

/**
 * @author 蒋时华
 * @date 2020-06-03 18:29:40
 */
public class AuntecBusinessServiceException extends AuntecRuntimeException implements BusinessServiceThrowable, ServiceDataAuntecThrowable {

    public AuntecBusinessServiceException() {
    }

    public AuntecBusinessServiceException(String message) {
        super(message);
    }

    public AuntecBusinessServiceException(Throwable throwable) {
        super(throwable);
    }

    public AuntecBusinessServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AuntecBusinessServiceException(com.jphoebe.framework.components.core.common.response.core.IResult IResult) {
        super(IResult);
    }

    public AuntecBusinessServiceException(com.jphoebe.framework.components.core.common.response.core.IResult IResult, Throwable throwable) {
        super(IResult, throwable);
    }

    public AuntecBusinessServiceException(com.jphoebe.framework.components.core.common.response.core.IResult IResult, String message) {
        super(IResult, message);
    }

    public AuntecBusinessServiceException(com.jphoebe.framework.components.core.common.response.core.IResult IResult, String message, Throwable throwable) {
        super(IResult, message, throwable);
    }

    @Override
    public Object getData() {
        com.jphoebe.framework.components.core.common.response.core.IResult iResult = super.getIResult();
        if (iResult instanceof IDataResult) {
            return ((IDataResult) iResult).getData();
        } else if (iResult instanceof IDataPageResult) {
            return ((IDataPageResult) iResult).getData();
        }
        return null;
    }
}
