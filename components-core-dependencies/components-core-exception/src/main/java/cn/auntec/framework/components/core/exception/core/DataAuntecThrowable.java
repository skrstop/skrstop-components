package cn.auntec.framework.components.core.exception.core;

/**
 * 携带异常数据
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public interface DataAuntecThrowable extends AuntecThrowable {

    /**
     * 获取自定义异常数据
     */
    ThrowableData getThrowableData();

    /**
     * 设置自定义异常数据
     *
     * @param throwableData
     */
    void setThrowableData(ThrowableData throwableData);


}
