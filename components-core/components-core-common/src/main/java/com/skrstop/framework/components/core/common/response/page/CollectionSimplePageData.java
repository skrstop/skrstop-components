package com.skrstop.framework.components.core.common.response.page;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2025-02-10 14:52:46
 * @since 1.0.0
 */
public class CollectionSimplePageData<T> extends SimplePageData<Collection<T>> {
    private static final long serialVersionUID = -2419450763177948411L;

    public CollectionSimplePageData() {
    }

    public CollectionSimplePageData(int page, int limit) {
        super(page, limit);
    }

    public CollectionSimplePageData(int page, int limit, int total) {
        super(page, limit, total);
    }

    public <R> CollectionSimplePageData<R> convert(Function<? super T, ? extends R> mapper) {
        CollectionSimplePageData<R> convertPage = new CollectionSimplePageData<>();
        Collection<R> collect = this.getRows().stream().map(mapper).collect(Collectors.toList());
        convertPage.setRows(collect);
        convertPage.setPageNumber(this.pageNumber);
        convertPage.setPageSize(this.pageSize);
        convertPage.setTotal(this.total);
        convertPage.setLastPageInfo(this.lastPageInfo);
        return convertPage;
    }

}
