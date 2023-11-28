package cn.auntec.framework.components.core.common.response.core;

import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface ISetResult<T> extends IDataResult<Set<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    Set<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(Set<T> data);

}
