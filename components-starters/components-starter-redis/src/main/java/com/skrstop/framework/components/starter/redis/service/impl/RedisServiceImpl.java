package com.skrstop.framework.components.starter.redis.service.impl;

import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.redis.filter.ValueFilter;
import com.skrstop.framework.components.starter.redis.service.RedisService;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.*;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 蒋时华 on 2017/9/21.
 */
@Slf4j
@SuppressWarnings("all")
public class RedisServiceImpl implements RedisService {

    @Getter
    private final RedisTemplate redisTemplate;
    @Getter
    private final ValueFilter valueFilter;

    public RedisServiceImpl(RedisTemplate redisTemplate, ValueFilter valueFilter) {
        this.redisTemplate = redisTemplate;
        this.valueFilter = valueFilter;
    }

    @Override
    public Long increment(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.increment(key);
    }

    @Override
    public Long increment(String key, long delta) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.increment(key, delta);
    }

    @Override
    public Double increment(String key, double delta) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.increment(key, delta);
    }

    @Override
    public Long decrement(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.decrement(key);
    }

    @Override
    public Long decrement(String key, long delta) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.decrement(key, delta);
    }

    @Override
    public Integer append(String key, String value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.append(key, value);
    }

    @Override
    public Boolean set(final String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean set(final String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, timeUnit);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean multiSet(final Map<String, Object> map) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.multiSet(map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean multiSet(final Map<String, Object> map, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.multiSet(map);
            map.keySet().forEach(key -> {
                this.expire(key, expireTime, timeUnit);
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean multiSetIfAbsent(final Map<String, Object> map) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.multiSetIfAbsent(map);
    }

    @Override
    public Boolean multiSetIfAbsent(final Map<String, Object> map, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Boolean result = operations.multiSetIfAbsent(map);
        map.keySet().forEach(key -> {
            this.expire(key, expireTime, timeUnit);
        });
        return result;
    }

    @Override
    public Boolean setIfAbsent(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, value);
    }

    @Override
    public Boolean setIfAbsent(String key, Object value, Duration duration) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, value, duration);
    }

    @Override
    public Boolean setIfPresent(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfPresent(key, value);
    }

    @Override
    public Boolean setIfPresent(String key, Object value, Duration duration) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfPresent(key, value, duration);
    }

    @Override
    public Boolean set(final String key, Object value, long expireTime) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean setIfAbsent(String key, Object value, long expireTime) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Boolean setIfAbsent(String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, value, expireTime, timeUnit);
    }

    @Override
    public Boolean setIfPresent(String key, Object value, long expireTime) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfPresent(key, value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Boolean setIfPresent(String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfPresent(key, value, expireTime, timeUnit);
    }

    @Override
    public Long remove(final String... keys) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.delete(CollectionUtil.newArrayList(keys));
    }

    @Override
    public Long remove(Collection<String> keys) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.delete(keys);
    }

    @Override
    public Long removePattern(final String pattern) {
        if (this.isConnectionClose()) {
            return null;
        }
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            return redisTemplate.delete(keys);
        }
        return null;
    }

    @Override
    public Boolean remove(final String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.delete(key);
    }

    @Override
    public Boolean hasKey(final String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T get(final String key, final Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
        T result = operations.get(key);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public <T> List<T> getForList(String key, Class<T> cls) {
        String val = this.get(key, String.class);
        if (valueFilter == null) {
            throw new NotSupportedException("不支持的操作");
        }
        return valueFilter.filterStr2List(val, cls);
    }

    @Override
    public <T> List<T> multiGet(Collection<String> keys, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        List<T> result = operations.multiGet(keys);
        if (valueFilter != null) {
            return valueFilter.filterList(result, cls);
        }
        return result;
    }

    @Override
    public <T> T getAndSet(String key, Class<T> cls, T value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
        T result = operations.getAndSet(key, value);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public <T> T getAndSet(String key, Class<T> cls, T value, long expireTime) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
        T result = operations.getAndSet(key, value);
        this.expire(key, expireTime, TimeUnit.SECONDS);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public <T> T getAndSet(String key, Class<T> cls, T value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
        T result = operations.getAndSet(key, value);
        this.expire(key, expireTime, timeUnit);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public Set<String> hashKeys(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.keys(key);
    }

    @Override
    public Boolean hashHasKey(String key, String hashKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.hasKey(key, hashKey);
    }

    @Override
    public Boolean hashPut(String key, String hashKey, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
            hash.put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean hashPutIfAbsent(String key, String hashKey, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.putIfAbsent(key, hashKey, value);
    }

    @Override
    public Boolean hashPutAll(String key, Map<String, Object> values) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
            hash.putAll(key, values);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public <T> T hashGet(String key, String hashKey, final Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        HashOperations<String, String, T> hash = redisTemplate.opsForHash();
        T result = hash.get(key, hashKey);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public <T> List<T> hashGet(String key, List<String> hashKeys, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        HashOperations<String, String, T> hash = redisTemplate.opsForHash();
        List<T> ts = hash.multiGet(key, hashKeys);
        if (valueFilter != null) {
            return valueFilter.filterList(ts, cls);
        }
        return ts;
    }

    @Override
    public Long hashDelete(String key, String... hasKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.opsForHash().delete(key, hasKey);
    }

    @Override
    public Long hashDelete(String key, Collection<String> hasKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.opsForHash().delete(key, hasKey.toArray(new String[hasKey.size()]));
    }

    @Override
    public Long listRightPush(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPush(key, value);
    }

    @Override
    public Long listRightPushAll(String key, Object... values) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPushAll(key, values);
    }

    @Override
    public Long listRightPushAll(String key, Collection<String> values) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPushAll(key, values.toArray(new String[values.size()]));
    }

    @Override
    public Long listRightPushIfPresent(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPushIfPresent(key, value);
    }

    @Override
    public Long listLeftPush(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPush(key, value);
    }

    @Override
    public Long listLeftPushAll(String key, Object... values) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPushAll(key, values);
    }

    @Override
    public Long listLeftPushAll(String key, Collection<String> values) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPushAll(key, values.toArray(new String[values.size()]));
    }

    @Override
    public Long listLeftPushIfPresent(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPushIfPresent(key, value);
    }

    @Override
    public Long listSize(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.size(key);
    }

    @Override
    public <T> List<T> listRange(String key, long start, long end, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, T> list = redisTemplate.opsForList();
        List<T> result = list.range(key, start, end);
        if (valueFilter != null) {
            return valueFilter.filterList(result, cls);
        }
        return result;
    }

    @Override
    public <T> T listLeftPop(final String key, final Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, T> list = redisTemplate.opsForList();
        T result = list.leftPop(key);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public <T> T listRightPop(final String key, final Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ListOperations<String, T> list = redisTemplate.opsForList();
        T result = list.rightPop(key);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public Long setAdd(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.add(key, value);
    }

    @Override
    public <T> Set<T> setMembers(String key, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        SetOperations<String, T> set = redisTemplate.opsForSet();
        Set<T> result = set.members(key);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public Long setRemove(String key, Object... values) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public Long setRemove(String key, Collection<String> values) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.opsForSet().remove(key, values.toArray(new String[values.size()]));
    }

    @Override
    public Boolean setIsMember(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public <T> T setPop(String key, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        SetOperations<String, T> set = redisTemplate.opsForSet();
        T result = set.pop(key);
        if (valueFilter != null) {
            return valueFilter.filter(result, cls);
        }
        return result;
    }

    @Override
    public <T> List<T> setPop(String key, Class<T> cls, int count) {
        if (this.isConnectionClose()) {
            return null;
        }
        SetOperations<String, T> set = redisTemplate.opsForSet();
        List<T> result = set.pop(key, count);
        if (valueFilter != null) {
            return valueFilter.filterList(result, cls);
        }
        return result;
    }


    @Override
    public Boolean zsetAdd(String key, Object value, double scoure) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.add(key, value, scoure);
    }

    @Override
    public Long zsetRemove(String key, Object... values) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, values);
    }

    @Override
    public Long zsetRemove(String key, Collection<String> values) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, values.toArray(new String[values.size()]));
    }

    @Override
    public Long zsetRemoveRangeByScore(String key, double min, double max) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, min, max);
    }

    @Override
    public Long zsetRemoveRange(String key, long start, long end) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, start, end);
    }

    @Override
    public <T> Set<T> zsetRangeByScore(String key, double min, double max, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.rangeByScore(key, min, max);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<T> zsetRange(String key, long start, long end, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.range(key, start, end);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeWithScores(String key, long start, long end, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<T>> result = zset.rangeWithScores(key, start, end);
        if (valueFilter != null) {
            return valueFilter.filterSetScore(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(String key, double min, double max, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<T>> result = zset.rangeByScoreWithScores(key, min, max);
        if (valueFilter != null) {
            return valueFilter.filterSetScore(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<T>> result = zset.rangeByScoreWithScores(key, min, max, offset, count);
        if (valueFilter != null) {
            return valueFilter.filterSetScore(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<T> zsetReverseRange(String key, long start, long end, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.reverseRange(key, start, end);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeWithScores(String key, long start, long end, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<T>> result = zset.reverseRangeWithScores(key, start, end);
        if (valueFilter != null) {
            return valueFilter.filterSetScore(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<T> zsetReverseRangeByScore(String key, double min, double max, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.reverseRangeByScore(key, min, max);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(String key, double min, double max, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<T>> result = zset.reverseRangeByScoreWithScores(key, min, max);
        if (valueFilter != null) {
            return valueFilter.filterSetScore(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<T> zsetReverseRangeByScore(String key, double min, double max, long offset, long count, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.reverseRangeByScore(key, min, max, offset, count);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<T>> result = zset.reverseRangeByScoreWithScores(key, min, max, offset, count);
        if (valueFilter != null) {
            return valueFilter.filterSetScore(result, cls);
        }
        return result;
    }

    @Override
    public Long zsetSize(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.size(key);
    }

    @Override
    public Long zsetZCard(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.zCard(key);
    }

    @Override
    public Double zsetScore(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, value);
    }

    @Override
    public Long zsetUnionAndStore(String key, String otherKey, String destKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.unionAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public Long zsetIntersectAndStore(String key, String otherKey, String destKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public <T> Set<T> zsetRangeByLex(String key, Range<String> range, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.rangeByLex(key, range);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public <T> Set<T> zsetRangeByLex(String key, Range<String> range, Limit limit, Class<T> cls) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, T> zset = redisTemplate.opsForZSet();
        Set<T> result = zset.rangeByLex(key, range, limit);
        if (valueFilter != null) {
            return valueFilter.filterSet(result, cls);
        }
        return result;
    }

    @Override
    public Long zsetCount(String key, double min, double max) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.count(key, min, max);
    }

    @Override
    public Double zsetIncrementScore(String key, Object value, double delta) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.incrementScore(key, value, delta);
    }

    @Override
    public Long zsetRank(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rank(key, value);
    }

    @Override
    public Long zsetReverseRank(String key, Object value) {
        if (this.isConnectionClose()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRank(key, value);
    }

    @Override
    public Boolean setDB(Integer index) {
        if (this.isConnectionClose()) {
            return null;
        }
        try {
            LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
            connectionFactory.setDatabase(index);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isConnectionClose() {
        if (redisTemplate == null) {
            return false;
        }
        try {
            return redisTemplate.getRequiredConnectionFactory().getConnection().isClosed();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean expire(String key, long expireTime) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Boolean expire(String key, long expireTime, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.expire(key, expireTime, timeUnit);
    }

    @Override
    public Long getExpire(String key) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.getExpire(key);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        if (this.isConnectionClose()) {
            return null;
        }
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> callback) {
        if (this.isConnectionClose()) {
            return null;
        }
        return this.redisTemplate.executePipelined(callback);
    }

    @Override
    public Set getPattern(String pattern) {
        Set values = this.redisTemplate.keys(pattern);
        if (values == null) {
            values = new HashSet();
        }
        return values;
    }

}
