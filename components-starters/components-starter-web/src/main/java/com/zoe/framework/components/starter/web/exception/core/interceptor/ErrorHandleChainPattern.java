package com.zoe.framework.components.starter.web.exception.core.interceptor;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.exception.global.interceptor.error.DefaultErrorInterceptor;
import com.zoe.framework.components.starter.web.exception.global.interceptor.error.ZoeDataErrorInterceptor;
import com.zoe.framework.components.starter.web.exception.global.interceptor.error.ZoeErrorInterceptor;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
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
        errorHandleChainPatterns.add(new ZoeDataErrorInterceptor());
        errorHandleChainPatterns.add(new ZoeErrorInterceptor());
        errorHandleChainPatterns.add(new DefaultErrorInterceptor());
        errorHandleChainPatterns.sort(Comparator.comparingInt(ErrorHandlerInterceptor::order));
    }

    public IResult execute(Error e) {
        for (ErrorHandlerInterceptor errorHandlerInterceptor : errorHandleChainPatterns) {
            if (!errorHandlerInterceptor.support(e)) {
                continue;
            }
            InterceptorResult execute = errorHandlerInterceptor.execute(e);
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
