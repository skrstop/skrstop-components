package cn.auntec.framework.components.starter.web.response.interceptor;

import cn.auntec.framework.components.core.common.response.*;
import cn.auntec.framework.components.core.common.response.core.*;
import cn.auntec.framework.components.core.common.response.page.CommonPageData;
import cn.auntec.framework.components.core.common.response.page.PageInfo;
import cn.auntec.framework.components.core.common.response.page.SimplePageInfo;
import cn.auntec.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Configuration
@Slf4j
public class IPageResultResponseInterceptor implements ResponseHandlerInterceptor {

    @Override
    public IResult execute(Object returnValue, boolean transResultResponse) {
        if (returnValue instanceof CommonPageData) {
            CommonPageData pageData = (CommonPageData) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return PageListResult.Builder.success(pageInfo, pageData.getData());
        } else if (returnValue instanceof IDataPageListResult) {
            PageListResult pageData = (PageListResult) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return PageListResult.Builder.success(pageInfo, pageData.getData());
        } else if (returnValue instanceof IDataPageHashSetResult) {
            IDataPageHashSetResult pageData = (IDataPageHashSetResult) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return PageHashSetResult.Builder.success(pageInfo, pageData.getData());
        } else if (returnValue instanceof IDataPageLinkedSetResult) {
            IDataPageLinkedSetResult pageData = (IDataPageLinkedSetResult) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return PageLinkedSetResult.Builder.success(pageInfo, pageData.getData());
        } else if (returnValue instanceof IDataPageSetResult) {
            IDataPageSetResult pageData = (IDataPageSetResult) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return PageSetResult.Builder.success(pageInfo, pageData.getData());
        } else if (returnValue instanceof IDataPageCollectionResult) {
            IDataPageCollectionResult pageData = (IDataPageCollectionResult) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return PageCollectionResult.Builder.success(pageInfo, pageData.getData());
        } else if (returnValue instanceof IPageResult) {
            IPageResult pageData = (IPageResult) returnValue;
            PageInfo pageInfo = ObjectUtil.defaultIfNull(pageData.getPageInfo(), new SimplePageInfo());
            // 使用统一分页数据格式PageData分页的
            return DefaultPageResult.Builder.success(pageInfo);
        }
        return null;
    }
}
