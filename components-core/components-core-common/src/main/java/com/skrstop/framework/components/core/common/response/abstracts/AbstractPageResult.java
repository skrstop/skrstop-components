package com.skrstop.framework.components.core.common.response.abstracts;

import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 14:56:58
 */
@Setter
@Getter
public abstract class AbstractPageResult<T> extends AbstractResult {
    private static final long serialVersionUID = 8527871385636688648L;

    protected PageData<T> data;

    public AbstractPageResult() {

    }

    public AbstractPageResult(IResult IResult) {
        super(IResult);
    }

    public AbstractPageResult(IResult IResult, PageData<T> pageData) {
        super(IResult);
        this.data = pageData;
    }

    public AbstractPageResult(IPageResult<T> IPageResult) {
        this(IPageResult, IPageResult.getData());
    }

    public AbstractPageResult(IPageResult<T> IPageResult, T list) {
        super(IPageResult);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public AbstractPageResult(IResult IResult, PageData<T> pageData, T list) {
        this(IResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public AbstractPageResult(IResult IResult, long pageNum, long pageSize) {
        this(IResult, new SimplePageData<T>(pageNum, pageSize));
    }

    public AbstractPageResult(IResult IResult, long pageNum, long pageSize, T list) {
        this(IResult, new SimplePageData<T>(pageNum, pageSize));
        this.data.setPageNumber(pageNum);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public AbstractPageResult(IResult IResult, long pageNum, long pageSize, long total) {
        this(IResult, new SimplePageData<T>(pageNum, pageSize, total));
        this.data.setPageNumber(pageNum);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public AbstractPageResult(IResult IResult, long pageNum, long pageSize, long total, T list) {
        this(IResult, new SimplePageData<T>(pageNum, pageSize, total));
        this.data.setPageNumber(pageNum);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
        this.data.setRows(list);
    }

}
