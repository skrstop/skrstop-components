package com.zoe.framework.components.starter.web.exception.global.interceptor.error;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.exception.ZoeError;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ErrorHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:33:02
 */
@Slf4j
@NoArgsConstructor
public class ZoeErrorInterceptor implements ErrorHandlerInterceptor {
    @Override
    public IResult execute(Error e) {
        if (e instanceof ZoeError) {
            ZoeError zoeError = (ZoeError) e;
            return Result.Builder.result(zoeError.getIResult());
        }
        return null;
    }
}
