package com.zoe.framework.components.starter.redis.filter;

import com.zoe.framework.components.util.serialization.json.FastJsonUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2020-10-06 18:20:19
 */
@SuppressWarnings("all")
public class FastjsonValueFilter implements ValueFilter {

    private final Boolean fastjsonFilterEach;
    private final Boolean autoType;

    public FastjsonValueFilter(Boolean fastjsonFilterEach, Boolean autoType) {
        this.fastjsonFilterEach = fastjsonFilterEach;
        this.autoType = autoType;
    }

    @Override
    public <T> T filter(Object obj, Class<T> cls) {
        if (obj == null) {
            return null;
        }
        if (!cls.equals(obj.getClass())) {
            return FastJsonUtil.toBean(FastJsonUtil.toJson(obj), cls);
        }
        return (T) obj;
    }

    @Override
    public <T> Set<T> filterSet(Set obj, Class<T> cls) {
        if (obj == null) {
            return null;
        }
        final Set collect = (Set) obj.stream().filter(ObjectUtil::isNotNull).collect(Collectors.toSet());
        if (fastjsonFilterEach) {
            LinkedHashSet<T> result = new LinkedHashSet<>();
            collect.forEach(o -> {
                if (!cls.equals(o.getClass())) {
                    result.add(FastJsonUtil.toBean(FastJsonUtil.toJson(collect), cls));
                } else {
                    result.add((T) o);
                }
            });
            return result;
        } else if (!autoType) {
            return new LinkedHashSet<>(FastJsonUtil.toBeanForList(FastJsonUtil.toJson(collect), cls));
        }
        return collect;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> filterSetScore(Set<ZSetOperations.TypedTuple<T>> obj, Class<T> cls) {
        if (obj == null) {
            return null;
        }
        final Set<ZSetOperations.TypedTuple<T>> collect = obj.stream().filter(ObjectUtil::isNotNull).collect(Collectors.toSet());
        LinkedHashSet<ZSetOperations.TypedTuple<T>> result = new LinkedHashSet<>();
        collect.forEach(o -> {
            if (!cls.equals(o.getClass())) {
                result.add(new DefaultTypedTuple<>(FastJsonUtil.toBean(FastJsonUtil.toJson(o.getValue()), cls), o.getScore()));
            } else {
                result.add(o);
            }
        });
        return result;
    }

    @Override
    public <T> List<T> filterList(List obj, Class<T> cls) {
        if (obj == null) {
            return null;
        }
        final List collect = (List) obj.stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList());
        if (fastjsonFilterEach) {
            ArrayList<T> result = new ArrayList<>();
            collect.forEach(o -> {
                if (!cls.equals(o.getClass())) {
                    result.add(FastJsonUtil.toBean(FastJsonUtil.toJson(collect), cls));
                } else {
                    result.add((T) o);
                }
            });
            return result;
        } else if (!autoType) {
            return FastJsonUtil.toBeanForList(FastJsonUtil.toJson(collect), cls);
        }
        return collect;
    }

}
