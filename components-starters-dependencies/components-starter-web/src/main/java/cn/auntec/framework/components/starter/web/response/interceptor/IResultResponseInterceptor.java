package cn.auntec.framework.components.starter.web.response.interceptor;

import cn.auntec.framework.components.core.common.response.Result;
import cn.auntec.framework.components.core.common.response.core.IDataResult;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Configuration
@Slf4j
public class IResultResponseInterceptor implements ResponseHandlerInterceptor {
    @Override
    public IResult execute(Object returnValue, boolean transResultResponse) {
        if (returnValue instanceof IDataResult) {
            IDataResult responsecode = (IDataResult) returnValue;
            return Result.Builder.result(responsecode);
        } else if (returnValue instanceof IResult) {
            IResult responsecode = (IResult) returnValue;
            return Result.Builder.result(responsecode);
        }
        return null;
    }
}
