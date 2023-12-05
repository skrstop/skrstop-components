package com.zoe.framework.components.starter.web.exception.core.interceptor;

import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.exception.global.interceptor.error.DefaultErrorInterceptor;
import com.zoe.framework.components.starter.web.exception.global.interceptor.error.ZoeErrorInterceptor;
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
public class ErrorHandleChainPattern {

    private final List<ErrorHandlerInterceptor> errorHandleChainPatterns;

    public ErrorHandleChainPattern(List<ErrorHandlerInterceptor> errorHandleChainPatterns) {
        this.errorHandleChainPatterns = errorHandleChainPatterns;
    }

    @PostConstruct
    private void initChainPattern() {
        Collections.sort(errorHandleChainPatterns, AnnotationAwareOrderComparator.INSTANCE);
        errorHandleChainPatterns.add(new ZoeErrorInterceptor());
        errorHandleChainPatterns.add(new DefaultErrorInterceptor());
    }

    public IResult execute(Error e) {
        for (ErrorHandlerInterceptor errorHandlerInterceptor : errorHandleChainPatterns) {
            IResult IResult = errorHandlerInterceptor.execute(e);
            if (ObjectUtil.isNotNull(IResult)) {
                return IResult;
            }
        }
        return null;
    }

}
