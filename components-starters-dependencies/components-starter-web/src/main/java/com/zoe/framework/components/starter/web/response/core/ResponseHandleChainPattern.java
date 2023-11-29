package com.zoe.framework.components.starter.web.response.core;

import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.response.interceptor.*;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:14:38
 */
@Configuration
@Getter
@Setter
public class ResponseHandleChainPattern {

    private final List<ResponseHandlerInterceptor> responseHandlerInterceptors;

    public ResponseHandleChainPattern(List<ResponseHandlerInterceptor> responseHandlerInterceptors) {
        this.responseHandlerInterceptors = responseHandlerInterceptors;
    }

    @PostConstruct
    private void initChainPattern() {
        Collections.sort(responseHandlerInterceptors, AnnotationAwareOrderComparator.INSTANCE);
        responseHandlerInterceptors.add(new PageHelperPageResponseInterceptor());
        responseHandlerInterceptors.add(new MybatisPlusPageResponseInterceptor());
        responseHandlerInterceptors.add(new ICollectionResultResponseInterceptor());
        responseHandlerInterceptors.add(new IPageResultResponseInterceptor());
        responseHandlerInterceptors.add(new IResultResponseInterceptor());
        responseHandlerInterceptors.add(new DefaultResponseInterceptor());
    }

    public IResult execute(Object returnValue, boolean transResultResponse) {
        for (ResponseHandlerInterceptor responseHandlerInterceptor : responseHandlerInterceptors) {
            IResult IResult = responseHandlerInterceptor.execute(returnValue, transResultResponse);
            if (ObjectUtil.isNotNull(IResult)) {
                return IResult;
            }
        }
        return null;
    }

}
