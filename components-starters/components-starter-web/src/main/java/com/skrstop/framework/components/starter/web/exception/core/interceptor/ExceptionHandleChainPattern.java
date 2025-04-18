package com.skrstop.framework.components.starter.web.exception.core.interceptor;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.global.interceptor.exception.*;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.util.Comparator;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:14:38
 */
@Configuration
@Getter
@Setter
public class ExceptionHandleChainPattern {

    private final List<ExceptionHandlerInterceptor> exceptionHandlerInterceptors;

    public ExceptionHandleChainPattern(List<ExceptionHandlerInterceptor> exceptionHandlerInterceptors) {
        this.exceptionHandlerInterceptors = exceptionHandlerInterceptors;
    }

    @PostConstruct
    private void initChainPattern() {
        exceptionHandlerInterceptors.add(new ParameterExceptionInterceptor());
        exceptionHandlerInterceptors.add(new ServerWebInputExceptionInterceptor());
        exceptionHandlerInterceptors.add(new NotFoundExceptionInterceptor());
        exceptionHandlerInterceptors.add(new BindExceptionInterceptor());
        exceptionHandlerInterceptors.add(new ConstraintViolationExceptionInterceptor());
        exceptionHandlerInterceptors.add(new HttpClientErrorExceptionInterceptor());
        exceptionHandlerInterceptors.add(new SkrstopDataExceptionInterceptor());
        exceptionHandlerInterceptors.add(new SkrstopExceptionInterceptor());
        exceptionHandlerInterceptors.add(new DefaultExceptionInterceptor());
        exceptionHandlerInterceptors.sort(Comparator.comparingInt(ExceptionHandlerInterceptor::order));
    }

    public IResult execute(Exception e, HttpServletResponse httpServletResponse, ServerHttpResponse serverHttpResponse) {
        for (ExceptionHandlerInterceptor exceptionHandlerInterceptor : exceptionHandlerInterceptors) {
            if (!exceptionHandlerInterceptor.support(e)) {
                continue;
            }
            InterceptorResult execute = exceptionHandlerInterceptor.execute(e, httpServletResponse, serverHttpResponse);
            if (ObjectUtil.isNull(execute)) {
                return Result.Builder.result(CommonResultCode.FAIL);
            }
            if (ObjectUtil.isNull(execute.getResult()) && !execute.isNext()) {
                return Result.Builder.result(CommonResultCode.FAIL);
            }
            return execute.getResult();
        }
        return null;
    }

}
