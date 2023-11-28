package com.jphoebe.framework.components.core.common.response.abstracts;

import com.jphoebe.framework.components.core.common.response.core.IPageResult;
import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.core.common.response.page.PageInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 14:56:58
 */
@Setter
@Getter
public abstract class AbstractPageResult extends AbstractResult {

    protected PageInfo pageInfo;

    public AbstractPageResult() {

    }

    public AbstractPageResult(IResult IResult) {
        super(IResult);
    }

    public AbstractPageResult(IResult IResult, PageInfo pageInfo) {
        super(IResult);
        this.pageInfo = pageInfo;
    }

    public AbstractPageResult(IPageResult IPageResult) {
        super(IPageResult);
    }

}
