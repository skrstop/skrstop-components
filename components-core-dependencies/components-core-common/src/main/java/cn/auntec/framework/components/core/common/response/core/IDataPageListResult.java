package cn.auntec.framework.components.core.common.response.core;

import java.util.List;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageListResult<T> extends IDataPageResult<List<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    List<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(List<T> data);

}
