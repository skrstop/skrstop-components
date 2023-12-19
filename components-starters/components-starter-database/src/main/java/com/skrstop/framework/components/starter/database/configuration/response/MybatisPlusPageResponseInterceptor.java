package com.skrstop.framework.components.starter.database.configuration.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.skrstop.framework.components.core.common.response.PageListResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Slf4j
public class MybatisPlusPageResponseInterceptor implements ResponseHandlerInterceptor {

    @Override
    public boolean support(Object returnValue) {
        return returnValue instanceof IPage;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public InterceptorResult execute(Object returnValue) {
        IPage<?> iPage = (IPage<?>) returnValue;
        return InterceptorResult.builder()
                .next(false)
                .result(PageListResult.Builder.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords()))
                .build();
    }
}
