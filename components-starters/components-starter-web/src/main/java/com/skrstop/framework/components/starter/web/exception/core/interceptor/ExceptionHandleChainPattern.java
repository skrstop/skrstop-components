package com.skrstop.framework.components.starter.web.exception.core.interceptor;

import cn.hutool.core.lang.Pair;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.global.interceptor.exception.*;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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

    public Pair<IResult, Integer> execute(Exception e) {
        for (ExceptionHandlerInterceptor exceptionHandlerInterceptor : exceptionHandlerInterceptors) {
            if (!exceptionHandlerInterceptor.support(e)) {
                continue;
            }
            InterceptorResult execute = exceptionHandlerInterceptor.execute(e);
            if (ObjectUtil.isNull(execute)) {
                return Pair.of(Result.Builder.result(CommonResultCode.FAIL), HttpStatusConst.HTTP_INTERNAL_ERROR);
            }
            if (ObjectUtil.isNull(execute.getResult()) && !execute.isNext()) {
                return Pair.of(Result.Builder.result(CommonResultCode.FAIL), HttpStatusConst.HTTP_INTERNAL_ERROR);
            }
            return Pair.of(execute.getResult(), ObjectUtil.defaultIfNull(execute.getResponseStatus(), HttpStatusConst.HTTP_INTERNAL_ERROR));
        }
        return Pair.of(Result.Builder.result(CommonResultCode.FAIL), HttpStatusConst.HTTP_INTERNAL_ERROR);
    }

}
