package com.skrstop.framework.components.core.common.response.core;

import com.skrstop.framework.components.core.common.response.page.PageData;

import java.util.LinkedHashSet;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageLinkedSetResult<T, P extends PageData<LinkedHashSet<T>>> extends IPageResult<LinkedHashSet<T>, P> {


}
