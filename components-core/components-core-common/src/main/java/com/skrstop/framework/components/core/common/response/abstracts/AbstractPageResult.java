package com.skrstop.framework.components.core.common.response.abstracts;

import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 14:56:58
 */
@Setter
@Getter
public abstract class AbstractPageResult<TS, P extends PageData<TS>> extends AbstractResult {
    private static final long serialVersionUID = 8527871385636688648L;

    protected P data;

    public AbstractPageResult() {

    }

    public AbstractPageResult(IResult iResult) {
        super(iResult);
    }

    public AbstractPageResult(IResult iResult, P pageData) {
        super(iResult);
        this.data = pageData;
    }

    public AbstractPageResult(IPageResult<TS, P> iPageResult) {
        this(iPageResult, iPageResult.getData());
    }

    public AbstractPageResult(IPageResult<TS, P> iPageResult, TS list) {
        super(iPageResult);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

}
