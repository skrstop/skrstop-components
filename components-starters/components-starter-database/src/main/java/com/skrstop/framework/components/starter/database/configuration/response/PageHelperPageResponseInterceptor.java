package com.skrstop.framework.components.starter.database.configuration.response;

import com.github.pagehelper.PageInfo;
import com.skrstop.framework.components.core.common.response.PageListResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Slf4j
public class PageHelperPageResponseInterceptor implements ResponseHandlerInterceptor {
    @Override
    public boolean support(Object returnValue) {
        return returnValue instanceof PageInfo;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public InterceptorResult execute(Object returnValue) {
        PageInfo<?> pageInfo = (PageInfo<?>) returnValue;
        return InterceptorResult.builder()
                .next(false)
                .result(PageListResult.Builder.success(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), pageInfo.getList()))
                .build();
    }
}
