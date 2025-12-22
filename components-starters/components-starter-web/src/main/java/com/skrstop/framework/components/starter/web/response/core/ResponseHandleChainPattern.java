package com.skrstop.framework.components.starter.web.response.core;

import cn.hutool.core.lang.Pair;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.DynamicResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.response.interceptor.DefaultCommonPageResponseInterceptor;
import com.skrstop.framework.components.starter.web.response.interceptor.DefaultPageResultResponseInterceptor;
import com.skrstop.framework.components.starter.web.response.interceptor.DefaultResponseInterceptor;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:14:38
 */
@Configuration
@Getter
public class ResponseHandleChainPattern {

    private final List<ResponseHandlerInterceptor> responseHandlerInterceptors;
    private final int defaultCode = HttpStatus.OK.value();

    public ResponseHandleChainPattern(List<ResponseHandlerInterceptor> responseHandlerInterceptors) {
        this.responseHandlerInterceptors = responseHandlerInterceptors;
    }

    @PostConstruct
    private void initChainPattern() {
        responseHandlerInterceptors.add(new DefaultCommonPageResponseInterceptor());
        responseHandlerInterceptors.add(new DefaultPageResultResponseInterceptor());
        responseHandlerInterceptors.add(new DefaultResponseInterceptor());
        // 排序
        responseHandlerInterceptors.sort(Comparator.comparingInt(ResponseHandlerInterceptor::order));
    }

    public Pair<Object, Integer> execute(Object returnValue, boolean disableTransResultTypeResponse) {

        if (disableTransResultTypeResponse && returnValue instanceof IResult) {
            // 禁用了IResult类型自动转换
            return Pair.of(new Result<>(CommonResultCode.SUCCESS, returnValue), defaultCode);
        }
        for (ResponseHandlerInterceptor responseHandlerInterceptor : responseHandlerInterceptors) {
            if (!responseHandlerInterceptor.support(returnValue)) {
                continue;
            }
            InterceptorResult execute = responseHandlerInterceptor.execute(returnValue);
            if (ObjectUtil.isNull(execute)) {
                return Pair.of(DynamicResult.build(returnValue), ObjectUtil.defaultIfNull(execute.getResponseStatus(), defaultCode));
            }
            if (ObjectUtil.isNull(execute.getResult()) && !execute.isNext()) {
                return Pair.of(DynamicResult.build(returnValue), ObjectUtil.defaultIfNull(execute.getResponseStatus(), defaultCode));
            }
            return Pair.of(execute.getResult(), ObjectUtil.defaultIfNull(execute.getResponseStatus(), defaultCode));
        }
        return Pair.of(null, defaultCode);
    }

}
