package com.skrstop.framework.components.core.common.response.core;

import com.skrstop.framework.components.core.common.response.page.PageData;

import java.util.ArrayList;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageArrayListResult<T, P extends PageData<ArrayList<T>>> extends IPageResult<ArrayList<T>, P> {


}
