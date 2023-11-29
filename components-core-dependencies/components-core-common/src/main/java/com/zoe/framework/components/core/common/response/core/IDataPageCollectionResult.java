package com.zoe.framework.components.core.common.response.core;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:42:27
 */
public interface IDataPageCollectionResult<T> extends IDataPageResult<Collection<T>> {

    /**
     * get page list data
     *
     * @return
     */
    @Override
    Collection<T> getData();

    /**
     * set page list data
     *
     * @param list
     */
    @Override
    void setData(Collection<T> list);

}
