package cn.auntec.framework.components.core.common.response.core;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface ICollectionResult<T> extends IDataResult<Collection<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    Collection<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(Collection<T> data);

}
