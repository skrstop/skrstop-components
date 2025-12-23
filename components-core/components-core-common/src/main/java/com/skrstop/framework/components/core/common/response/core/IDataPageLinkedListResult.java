package com.skrstop.framework.components.core.common.response.core;

import com.skrstop.framework.components.core.common.response.page.PageData;

import java.util.LinkedList;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageLinkedListResult<T, P extends PageData<LinkedList<T>>> extends IPageResult<LinkedList<T>, P> {


}
