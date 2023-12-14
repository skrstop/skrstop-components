package com.zoe.framework.components.starter.web.response.core;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.util.DynamicResult;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.response.interceptor.DefaultCommonPageResponseInterceptor;
import com.zoe.framework.components.starter.web.response.interceptor.DefaultPageResultResponseInterceptor;
import com.zoe.framework.components.starter.web.response.interceptor.DefaultResponseInterceptor;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

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

    public Object execute(Object returnValue, boolean disableTransResultTypeResponse) {
        if (disableTransResultTypeResponse && returnValue instanceof IResult) {
            // 禁用了IResult类型自动转换
            return new Result<>(CommonResultCode.SUCCESS, returnValue);
        }
        for (ResponseHandlerInterceptor responseHandlerInterceptor : responseHandlerInterceptors) {
            if (!responseHandlerInterceptor.support(returnValue)) {
                continue;
            }
            InterceptorResult execute = responseHandlerInterceptor.execute(returnValue);
            if (ObjectUtil.isNull(execute)) {
                return DynamicResult.build(returnValue);
            }
            if (ObjectUtil.isNull(execute.getResult()) && !execute.isNext()) {
                return DynamicResult.build(returnValue);
            }
            return execute.getResult();
        }
        return null;
    }

}
