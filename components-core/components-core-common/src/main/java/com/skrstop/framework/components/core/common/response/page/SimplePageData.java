package com.skrstop.framework.components.core.common.response.page;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:13:29
 */
@Getter
@Setter
public class SimplePageData<T> extends AbstractPageInfo<T> implements PageData<T> {

    @Serial
    private static final long serialVersionUID = -7653109079434897802L;

    public SimplePageData() {
    }

    public SimplePageData(long pageNum, long pageSize) {
        super(pageNum, pageSize);
    }

    public SimplePageData(long pageNum, long pageSize, long total) {
        super(pageNum, pageSize, total);
    }

}
