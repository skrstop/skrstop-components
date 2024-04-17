package com.skrstop.framework.components.core.common.util;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.core.*;
import com.skrstop.framework.components.core.common.response.page.CommonPageData;
import com.skrstop.framework.components.core.common.response.page.PageInfo;
import com.skrstop.framework.components.core.common.response.page.SimplePageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * response code with data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
@SuppressWarnings("all")
public class DynamicResult {

    private static PageInfo defaultIfNull(PageInfo pageInfo, PageInfo defaultPageInfo) {
        if (Objects.isNull(pageInfo)) {
            return defaultPageInfo;
        }
        return pageInfo;
    }

    public static IPageResult buildPage(PageInfo pageInfo, Collection<?> val) {
        if (Objects.isNull(val)) {
            return DefaultPageResult.Builder.success();
        }
        pageInfo = defaultIfNull(pageInfo, new SimplePageInfo());
        // object
        if (val instanceof List) {
            return PageListResult.Builder.success(pageInfo, (List) val);
        } else if (val instanceof HashSet) {
            return PageHashSetResult.Builder.success(pageInfo, (HashSet) val);
        } else if (val instanceof LinkedHashSet) {
            return PageLinkedSetResult.Builder.success(pageInfo, (LinkedHashSet) val);
        } else if (val instanceof Set) {
            return PageSetResult.Builder.success(pageInfo, (Set) val);
        } else if (val instanceof Collection) {
            return PageCollectionResult.Builder.success(pageInfo, (Collection) val);
        }
        return PageCollectionResult.Builder.success(pageInfo, val);
    }

    public static IPageResult buildPage(CommonPageData<?> commonPageData) {
        if (Objects.isNull(commonPageData)) {
            return DefaultPageResult.Builder.success();
        }
        PageInfo pageInfo = defaultIfNull(commonPageData.getPageInfo(), new SimplePageInfo());
        return buildPage(pageInfo, commonPageData.getData());
    }

    public static IPageResult buildPage(IPageResult result) {
        if (Objects.isNull(result)) {
            return DefaultPageResult.Builder.success();
        }
        PageInfo pageInfo = defaultIfNull(result.getPageInfo(), new SimplePageInfo());
        // result
        if (result instanceof IDataPageListResult) {
            return PageListResult.Builder.success(pageInfo, ((IDataPageListResult<?>) result).getData());
        } else if (result instanceof IDataPageHashSetResult) {
            return PageHashSetResult.Builder.success(pageInfo, ((IDataPageHashSetResult<?>) result).getData());
        } else if (result instanceof IDataPageLinkedSetResult) {
            return PageLinkedSetResult.Builder.success(pageInfo, ((IDataPageLinkedSetResult<?>) result).getData());
        } else if (result instanceof IDataPageSetResult) {
            return PageSetResult.Builder.success(pageInfo, ((IDataPageSetResult<?>) result).getData());
        } else if (result instanceof IDataPageCollectionResult) {
            return PageCollectionResult.Builder.success(pageInfo, ((IDataPageCollectionResult<?>) result).getData());
        } else if (result instanceof IPageResult) {
            return DefaultPageResult.Builder.success(pageInfo);
        }
        return PageCollectionResult.Builder.result(result);
    }

    public static IResult build(Object val) {
        if (Objects.isNull(val)) {
            return Result.Builder.success();
        }
        // object
        if (val instanceof List) {
            return ListResult.Builder.success((List) val);
        } else if (val instanceof LinkedHashMap) {
            return LinkedMapResult.Builder.success((LinkedHashMap) val);
        } else if (val instanceof Map) {
            return MapResult.Builder.success((Map) val);
        } else if (val instanceof LinkedHashSet) {
            return LinkedSetResult.Builder.success((LinkedHashSet) val);
        } else if (val instanceof HashSet) {
            return HashSetResult.Builder.success((HashSet) val);
        } else if (val instanceof Set) {
            return SetResult.Builder.success((Set) val);
        } else if (val instanceof Collection) {
            return CollectionResult.Builder.success((Collection) val);
        }
        // result
        else if (val instanceof IListResult) {
            return ListResult.Builder.result((IListResult) val);
        } else if (val instanceof ILinkedMapResult) {
            return LinkedMapResult.Builder.result((ILinkedMapResult) val);
        } else if (val instanceof IMapResult) {
            return MapResult.Builder.result((IMapResult) val);
        } else if (val instanceof ILinkedSetResult) {
            return LinkedSetResult.Builder.result((ILinkedSetResult) val);
        } else if (val instanceof IHashSetResult) {
            return HashSetResult.Builder.result((IHashSetResult) val);
        } else if (val instanceof ISetResult) {
            return SetResult.Builder.result((ISetResult) val);
        } else if (val instanceof ICollectionResult) {
            return CollectionResult.Builder.result((ICollectionResult) val);
        } else if (val instanceof IResult) {
            return Result.Builder.result((IResult) val);
        }
        // default
        return Result.Builder.success(val);
    }

}
