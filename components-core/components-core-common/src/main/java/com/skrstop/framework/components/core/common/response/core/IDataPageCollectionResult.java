package com.skrstop.framework.components.core.common.response.core;

import com.skrstop.framework.components.core.common.response.page.PageData;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:42:27
 */
public interface IDataPageCollectionResult<T, P extends PageData<Collection<T>>> extends IPageResult<Collection<T>, P> {


}
