package com.skrstop.framework.components.core.common.response.page;

import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2025-02-10 14:52:46
 * @since 1.0.0
 */
public class SetSimplePageData<T> extends SimplePageData<LinkedHashSet<T>> {
    private static final long serialVersionUID = -2419450763177948411L;

    public <R> SetSimplePageData<R> convert(Function<? super T, ? extends R> mapper) {
        SetSimplePageData<R> convertPage = new SetSimplePageData<>();
        LinkedHashSet<R> collect = this.getRows().stream()
                .map(mapper)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        convertPage.setRows(collect);
        convertPage.setPageNumber(this.pageNumber);
        convertPage.setPageSize(this.pageSize);
        convertPage.setTotal(this.total);
        convertPage.setLastPageInfo(this.lastPageInfo);
        return convertPage;
    }

}
