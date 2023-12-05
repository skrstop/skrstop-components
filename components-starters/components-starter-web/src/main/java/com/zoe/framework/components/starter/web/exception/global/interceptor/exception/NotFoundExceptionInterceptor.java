package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.DefaultResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * 参数异常handle
 *
 * @author 蒋时华
 * @date 2020-05-08 13:03:01
 */
@Slf4j
@NoArgsConstructor
public class NotFoundExceptionInterceptor implements ExceptionHandlerInterceptor {

    @Override
    public IResult execute(Exception e) {
        if (e instanceof ResponseStatusException) {
            // 404
            HttpStatusCode status = ((ResponseStatusException) e).getStatusCode();
            if (HttpStatus.NOT_FOUND.value() == status.value()) {
                return DefaultResult.Builder.result(CommonResultCode.NOT_FOUND);
            }
        }
        return null;
    }

}
