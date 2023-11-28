package cn.auntec.framework.components.starter.redis.filter;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-10-06 18:20:19
 */
public class BinaryValueFilter implements ValueFilter {

    @Override
    public <T> T filter(Object obj, Class<T> cls) {
        return (T) obj;
    }

    @Override
    public <T> Set<T> filterSet(Set obj, Class<T> cls) {
        return (Set<T>) obj;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> filterSetScore(Set<ZSetOperations.TypedTuple<T>> obj, Class<T> cls) {
        return (Set<ZSetOperations.TypedTuple<T>>) obj;
    }

    @Override
    public <T> List<T> filterList(List obj, Class<T> cls) {
        return (List<T>) obj;
    }

}
