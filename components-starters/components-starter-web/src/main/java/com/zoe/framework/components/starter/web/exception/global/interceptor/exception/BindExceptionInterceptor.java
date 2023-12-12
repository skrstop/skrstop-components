package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.util.EnumCodeUtil;
import com.zoe.framework.components.core.exception.common.CommonExceptionCode;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.validate.ErrorMessageUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数异常handle
 *
 * @author 蒋时华
 * @date 2020-05-08 13:03:01
 */
@Slf4j
@NoArgsConstructor
public class BindExceptionInterceptor implements ExceptionHandlerInterceptor {

    @Override
    public boolean support(Exception e) {
        return e instanceof BindException;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 6;
    }

    @Override
    public InterceptorResult execute(Exception e) {
        BindException ex = (BindException) e;
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String defaultMessage = null;
        IResult error = EnumCodeUtil.transferEnumCode(CommonExceptionCode.PARAMETER);
        if (CollectionUtil.isNotEmpty(errors)) {
            defaultMessage = ErrorMessageUtil.getFirstErrorMessage(errors.stream()
                    .filter(err -> err instanceof FieldError && !((FieldError) err).isBindingFailure())
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList())
            );
        }
        error.setMessage(defaultMessage);
        return InterceptorResult.builder()
                .next(false)
                .result(error)
                .build();
    }

}
