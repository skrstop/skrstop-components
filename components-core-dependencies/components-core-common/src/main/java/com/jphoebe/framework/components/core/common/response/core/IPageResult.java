package com.jphoebe.framework.components.core.common.response.core;

import com.jphoebe.framework.components.core.common.response.page.PageInfo;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IPageResult extends IResult {

    /*** get page info */
    PageInfo getPageInfo();

}
