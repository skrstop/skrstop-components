package com.skrstop.framework.components.core.common.response.core;

import com.skrstop.framework.components.core.common.response.page.PageData;

import java.util.HashSet;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageHashSetResult<T, P extends PageData<HashSet<T>>> extends IPageResult<HashSet<T>, P> {


}
