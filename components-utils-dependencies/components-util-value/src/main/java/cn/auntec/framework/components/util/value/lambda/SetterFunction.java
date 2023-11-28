package cn.auntec.framework.components.util.value.lambda;

import java.io.Serializable;

/**
 * SFunction class
 *
 * @author 蒋时华
 * @date 2019/12/14
 */
@FunctionalInterface
public interface SetterFunction<T, U> extends Serializable {
    void set(T source, U parameter);
}
