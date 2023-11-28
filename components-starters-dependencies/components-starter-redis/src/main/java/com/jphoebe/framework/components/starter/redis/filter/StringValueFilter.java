package com.jphoebe.framework.components.starter.redis.filter;

import com.jphoebe.framework.components.core.exception.defined.illegal.NotSupportedException;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-10-06 18:20:19
 */
public class StringValueFilter implements ValueFilter {
    @Override
    public <T> T filter(Object obj, Class<T> cls) {
        this.valid(cls);
        return (T) obj;
    }

    @Override
    public <T> Set<T> filterSet(Set obj, Class<T> cls) {
        this.valid(cls);
        return obj;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> filterSetScore(Set<ZSetOperations.TypedTuple<T>> obj, Class<T> cls) {
        this.valid(cls);
        return obj;
    }

    @Override
    public <T> List<T> filterList(List obj, Class<T> cls) {
        this.valid(cls);
        return obj;
    }

    private <T> void valid(Class<T> cls) {
        if (!String.class.equals(cls)) {
            throw new NotSupportedException("StringRedisTemplate仅支持String类型转换");
        }
    }

}
