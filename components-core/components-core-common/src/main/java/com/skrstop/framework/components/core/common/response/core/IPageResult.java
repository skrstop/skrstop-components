package com.skrstop.framework.components.core.common.response.core;

import com.skrstop.framework.components.core.common.response.page.PageData;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IPageResult<TS, P extends PageData<TS>> extends IResult {

    /*** get page data */
    P getData();

    void setData(P data);

}
