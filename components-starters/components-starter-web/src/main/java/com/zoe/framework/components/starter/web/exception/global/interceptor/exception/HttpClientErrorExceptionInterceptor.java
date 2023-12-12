package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.util.EnumCodeUtil;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.zoe.framework.components.util.serialization.json.FastJsonUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:08:48
 */
@Slf4j
@NoArgsConstructor
public class HttpClientErrorExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public boolean support(Exception e) {
        return e instanceof HttpClientErrorException;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 4;
    }

    @Override
    public InterceptorResult execute(Exception e) {
        HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
        String httpResultStr = null;
        try {
            httpResultStr = new String(httpClientErrorException.getResponseBodyAsByteArray(), "UTF-8");
            HashMap<String, String> map = FastJsonUtil.toBeanForHashMap(httpResultStr);
            String message = null;
            if (map != null) {
                message = map.get("message");
            }
            IResult error = EnumCodeUtil.transferEnumCode(CommonResultCode.FAIL);
            error.setMessage(message);
            return InterceptorResult.builder()
                    .next(false)
                    .result(Result.Builder.result(error))
                    .build();
        } catch (UnsupportedEncodingException ex) {
            return InterceptorResult.builder()
                    .next(false)
                    .result(Result.Builder.result(CommonResultCode.FAIL, ex))
                    .build();
        }
    }
}
