package com.skrstop.framework.components.core.common.response.page;

import java.io.Serial;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2025-02-10 14:52:46
 * @since 1.0.0
 */
public class ListSimplePageData<T> extends SimplePageData<List<T>> {
    @Serial
    private static final long serialVersionUID = -2419450763177948411L;

    public <R> ListSimplePageData<R> convert(Function<? super T, ? extends R> mapper) {
        ListSimplePageData<R> convertPage = new ListSimplePageData<>();
        List<R> collect = this.getRows().stream().map(mapper).collect(Collectors.toList());
        convertPage.setRows(collect);
        return convertPage;
    }

}
