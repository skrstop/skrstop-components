package com.zoe.framework.components.starter.web.response.interceptor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zoe.framework.components.core.common.response.PageListResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Configuration
@Slf4j
public class MybatisPlusPageResponseInterceptor implements ResponseHandlerInterceptor {

    private int isMybatisPlus = -1;

    @Override
    public IResult execute(Object returnValue, boolean transResultResponse) {
        if (this.isMybatisPlus == -1) {
            try {
                Class.forName("com.baomidou.mybatisplus.core.metadata.IPage");
                this.isMybatisPlus = 1;
            } catch (ClassNotFoundException e) {
                this.isMybatisPlus = 0;
            }
        }
        if (this.isMybatisPlus == 1 && returnValue instanceof IPage) {
            IPage iPage = (IPage) returnValue;
            // 使用统一分页数据格式PageData分页的
            return PageListResult.Builder.success(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
        }
        return null;
    }
}
