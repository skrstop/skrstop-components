package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.exception.defined.illegal.ParameterException;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * 参数异常handle
 *
 * @author 蒋时华
 * @date 2020-05-08 13:03:01
 */
@Slf4j
@NoArgsConstructor
public class ParameterExceptionInterceptor implements ExceptionHandlerInterceptor {

    @Override
    public boolean support(Exception e) {
        return e instanceof ParameterException;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 8;
    }

    @Override
    public InterceptorResult execute(Exception e, HttpServletResponse httpServletResponse, ServerHttpResponse serverHttpResponse) {
        if (ObjectUtil.isNotNull(httpServletResponse)) {
            httpServletResponse.setStatus(HttpStatusConst.HTTP_BAD_REQUEST);
        }
        if (ObjectUtil.isNotNull(serverHttpResponse)) {
            serverHttpResponse.setRawStatusCode(HttpStatusConst.HTTP_BAD_REQUEST);
        }
        return InterceptorResult.builder()
                .next(false)
                .result(Result.Builder.result(((ParameterException) e).getIResult()))
                .build();
    }

}
