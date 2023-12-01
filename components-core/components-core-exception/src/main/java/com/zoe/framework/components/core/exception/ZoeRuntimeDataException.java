package com.zoe.framework.components.core.exception;

import com.zoe.framework.components.core.common.response.core.IDataPageResult;
import com.zoe.framework.components.core.common.response.core.IDataResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.exception.core.BusinessServiceThrowable;
import com.zoe.framework.components.core.exception.core.ServiceDataZoeThrowable;

/**
 * @author 蒋时华
 * @date 2020-06-03 18:29:40
 */
public class ZoeRuntimeDataException extends ZoeRuntimeException implements BusinessServiceThrowable, ServiceDataZoeThrowable {

    public ZoeRuntimeDataException() {
    }

    public ZoeRuntimeDataException(String message) {
        super(message);
    }

    public ZoeRuntimeDataException(Throwable throwable) {
        super(throwable);
    }

    public ZoeRuntimeDataException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ZoeRuntimeDataException(IResult IResult) {
        super(IResult);
    }

    public ZoeRuntimeDataException(IResult IResult, Throwable throwable) {
        super(IResult, throwable);
    }

    public ZoeRuntimeDataException(IResult IResult, String message) {
        super(IResult, message);
    }

    public ZoeRuntimeDataException(IResult IResult, String message, Throwable throwable) {
        super(IResult, message, throwable);
    }

    @Override
    public Object getData() {
        IResult iResult = super.getIResult();
        if (iResult instanceof IDataResult) {
            return ((IDataResult) iResult).getData();
        } else if (iResult instanceof IDataPageResult) {
            return ((IDataPageResult) iResult).getData();
        }
        return null;
    }
}
