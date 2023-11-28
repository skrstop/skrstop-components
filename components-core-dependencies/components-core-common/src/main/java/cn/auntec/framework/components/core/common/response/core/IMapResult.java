package cn.auntec.framework.components.core.common.response.core;

import java.util.Map;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface IMapResult<T, R> extends IDataResult<Map<T, R>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    Map<T, R> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(Map<T, R> data);

}
