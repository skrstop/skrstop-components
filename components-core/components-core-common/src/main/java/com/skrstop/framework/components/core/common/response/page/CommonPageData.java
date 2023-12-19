package com.skrstop.framework.components.core.common.response.page;

import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2021-03-22 20:27:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CommonPageData<T> extends SerializableBean {
    private static final long serialVersionUID = -7429541694577150907L;

    private PageInfo pageInfo;

    private List<T> data;

    public <R> CommonPageData<R> convert(Function<? super T, ? extends R> mapper) {
        CommonPageData<R> convertPage = new CommonPageData<>();
        List<R> collect = this.getData().stream().map(mapper).collect(Collectors.toList());
        convertPage.setData(collect);
        convertPage.setPageInfo(this.pageInfo);
        return convertPage;
    }
}
