package com.zoe.framework.components.starter.web.exception.core.interceptor;

import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.exception.global.interceptor.exception.*;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Collections;
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
        Collections.sort(exceptionHandlerInterceptors, AnnotationAwareOrderComparator.INSTANCE);
        exceptionHandlerInterceptors.add(new ServerWebInputExceptionInterceptor());
        exceptionHandlerInterceptors.add(new NotFoundExceptionInterceptor());
        exceptionHandlerInterceptors.add(new BindExceptionInterceptor());
        exceptionHandlerInterceptors.add(new ConstraintViolationExceptionInterceptor());
        exceptionHandlerInterceptors.add(new HttpClientErrorExceptionInterceptor());
        exceptionHandlerInterceptors.add(new ServiceDataExceptionInterceptor());
        exceptionHandlerInterceptors.add(new ZoeRuntimeExceptionInterceptor());
        exceptionHandlerInterceptors.add(new ZoeExceptionInterceptor());
        exceptionHandlerInterceptors.add(new DefaultExceptionInterceptor());
    }

    public IResult execute(Exception e) {
        for (ExceptionHandlerInterceptor exceptionHandlerInterceptor : exceptionHandlerInterceptors) {
            IResult IResult = exceptionHandlerInterceptor.execute(e);
            if (ObjectUtil.isNotNull(IResult)) {
                return IResult;
            }
        }
        return null;
    }

}
