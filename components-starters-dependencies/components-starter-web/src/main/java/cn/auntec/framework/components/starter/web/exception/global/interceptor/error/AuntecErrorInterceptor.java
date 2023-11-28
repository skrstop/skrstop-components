package cn.auntec.framework.components.starter.web.exception.global.interceptor.error;

import cn.auntec.framework.components.core.common.response.Result;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.exception.AuntecError;
import cn.auntec.framework.components.starter.web.exception.core.interceptor.ErrorHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:33:02
 */
@Slf4j
@NoArgsConstructor
public class AuntecErrorInterceptor implements ErrorHandlerInterceptor {
    @Override
    public IResult execute(Error e) {
        if (e instanceof AuntecError) {
            AuntecError auntecError = (AuntecError) e;
            return Result.Builder.result(auntecError.getIResult());
        }
        return null;
    }
}
