package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.validate.ErrorMessageUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数异常handle
 *
 * @author 蒋时华
 * @date 2020-05-08 13:03:01
 */
@Slf4j
@NoArgsConstructor
public class ConstraintViolationExceptionInterceptor implements ExceptionHandlerInterceptor {

    @Override
    public boolean support(Exception e) {
        return e instanceof ConstraintViolationException;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 5;
    }

    @Override
    public InterceptorResult execute(Exception e, HttpServletResponse httpServletResponse, ServerHttpResponse serverHttpResponse) {
        if (ObjectUtil.isNotNull(httpServletResponse)) {
            httpServletResponse.setStatus(HttpStatusConst.HTTP_BAD_REQUEST);
        }
        if (ObjectUtil.isNotNull(serverHttpResponse)) {
            serverHttpResponse.setRawStatusCode(HttpStatusConst.HTTP_BAD_REQUEST);
        }
        IResult paramError = EnumCodeUtil.transferEnumCode(CommonExceptionCode.PARAMETER);
        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
        if (CollectionUtil.isEmpty(constraintViolations)) {
            return InterceptorResult.builder()
                    .next(false)
                    .result(paramError)
                    .build();
        }
        List<String> errors = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(errors)) {
            return InterceptorResult.builder()
                    .next(false)
                    .result(paramError)
                    .build();
        }
        paramError.setMessage(ErrorMessageUtil.getFirstErrorMessage(errors));
        return InterceptorResult.builder()
                .next(false)
                .result(paramError)
                .build();
    }

}
