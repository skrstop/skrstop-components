package cn.auntec.framework.components.starter.web.exception.global.interceptor.exception;

import cn.auntec.framework.components.core.common.response.Result;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:11:34
 */
@NoArgsConstructor
@Slf4j
public class AuntecRuntimeExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public IResult execute(Exception e) {
        if (e instanceof AuntecRuntimeException) {
            AuntecRuntimeException auntecRuntimeException = (AuntecRuntimeException) e;
            return Result.Builder.result(auntecRuntimeException.getIResult());
        }
        return null;
    }
}
