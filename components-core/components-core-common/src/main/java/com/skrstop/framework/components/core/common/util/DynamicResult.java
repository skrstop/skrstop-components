package com.skrstop.framework.components.core.common.util;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.core.*;
import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
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

    private static PageData defaultIfNull(PageData pageData, PageData defaultPageData) {
        if (Objects.isNull(pageData)) {
            return defaultPageData;
        }
        return pageData;
    }

    public static IPageResult buildPage(PageData pageData, Collection<?> val) {
        if (Objects.isNull(val)) {
            return DefaultPageResult.Builder.success();
        }
        pageData = defaultIfNull(pageData, new SimplePageData());
        // object
        if (val instanceof List) {
            return PageListResult.Builder.success(pageData, (List) val);
        } else if (val instanceof HashSet) {
            return PageHashSetResult.Builder.success(pageData, (HashSet) val);
        } else if (val instanceof LinkedHashSet) {
            return PageLinkedSetResult.Builder.success(pageData, (LinkedHashSet) val);
        } else if (val instanceof Set) {
            return PageSetResult.Builder.success(pageData, (Set) val);
        } else if (val instanceof Collection) {
            return PageCollectionResult.Builder.success(pageData, (Collection) val);
        }
        return PageCollectionResult.Builder.success(pageData, val);
    }

    public static IPageResult buildPage(PageData<?> pageData) {
        if (Objects.isNull(pageData)) {
            return DefaultPageResult.Builder.success();
        }
        pageData = defaultIfNull(pageData, new SimplePageData());
        Object rows = pageData.getRows();
        if (rows instanceof List) {
            return PageListResult.Builder.success(pageData);
        } else if (rows instanceof LinkedHashSet) {
            return PageLinkedSetResult.Builder.success(pageData);
        } else if (rows instanceof HashSet) {
            return PageHashSetResult.Builder.success(pageData);
        } else if (rows instanceof Set) {
            return PageSetResult.Builder.success(pageData);
        } else if (rows instanceof Collection) {
            return PageCollectionResult.Builder.success(pageData);
        }
        return PageListResult.Builder.success(pageData, new ArrayList<>());
    }

    public static IPageResult buildPage(IPageResult result) {
        if (Objects.isNull(result)) {
            return DefaultPageResult.Builder.success();
        }
        PageData pageData = defaultIfNull(result.getData(), new SimplePageData());
        result.setData(pageData);
        return result;
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
        } else if (val instanceof IDataResult) {
            return Result.Builder.result((IDataResult) val);
        } else if (val instanceof IResult) {
            return Result.Builder.result((IResult) val);
        }
        // default
        return Result.Builder.success(val);
    }

}
