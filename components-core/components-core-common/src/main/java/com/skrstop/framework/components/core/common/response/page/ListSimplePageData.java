package com.skrstop.framework.components.core.common.response.page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2025-02-10 14:52:46
 * @since 1.0.0
 */
public class ListSimplePageData<T> extends SimplePageData<List<T>> {
    private static final long serialVersionUID = -2419450763177948411L;

    public ListSimplePageData() {
    }

    public ListSimplePageData(int page, int limit) {
        super(page, limit);
    }

    public ListSimplePageData(int page, int limit, int total) {
        super(page, limit, total);
    }

    public <R> ListSimplePageData<R> convert(Function<? super T, ? extends R> mapper) {
        ListSimplePageData<R> convertPage = new ListSimplePageData<>();
        List<R> collect = this.getRows().stream().map(mapper).collect(Collectors.toList());
        convertPage.setRows(collect);
        convertPage.setPageNumber(this.pageNumber);
        convertPage.setPageSize(this.pageSize);
        convertPage.setTotal(this.total);
        convertPage.setLastPageInfo(this.lastPageInfo);
        return convertPage;
    }

}
