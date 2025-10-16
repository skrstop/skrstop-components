package com.skrstop.framework.components.starter.web.exception.core.interceptor;

import cn.hutool.core.lang.Pair;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.global.interceptor.error.DefaultErrorInterceptor;
import com.skrstop.framework.components.starter.web.exception.global.interceptor.error.SkrstopDataErrorInterceptor;
import com.skrstop.framework.components.starter.web.exception.global.interceptor.error.SkrstopErrorInterceptor;
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
public class ErrorHandleChainPattern {

    private final List<ErrorHandlerInterceptor> errorHandleChainPatterns;

    public ErrorHandleChainPattern(List<ErrorHandlerInterceptor> errorHandleChainPatterns) {
        this.errorHandleChainPatterns = errorHandleChainPatterns;
    }

    @PostConstruct
    private void initChainPattern() {
        errorHandleChainPatterns.add(new SkrstopDataErrorInterceptor());
        errorHandleChainPatterns.add(new SkrstopErrorInterceptor());
        errorHandleChainPatterns.add(new DefaultErrorInterceptor());
        errorHandleChainPatterns.sort(Comparator.comparingInt(ErrorHandlerInterceptor::order));
    }

    public Pair<IResult, Integer> execute(Error e) {
        for (ErrorHandlerInterceptor errorHandlerInterceptor : errorHandleChainPatterns) {
            if (!errorHandlerInterceptor.support(e)) {
                continue;
            }
            InterceptorResult execute = errorHandlerInterceptor.execute(e);
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
