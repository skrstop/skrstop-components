package cn.auntec.framework.components.core.common.response.core;

import java.util.LinkedHashSet;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageLinkedSetResult<T> extends IDataPageResult<LinkedHashSet<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    LinkedHashSet<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(LinkedHashSet<T> data);

}
