package com.zoe.framework.components.starter.redis.service.impl;

import com.zoe.framework.components.starter.redis.filter.ValueFilter;
import com.zoe.framework.components.starter.redis.service.RedisService;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
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
        if (this.isConnection()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long increment(String key, long delta) {
        if (this.isConnection()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key, delta);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Double increment(String key, double delta) {
        if (this.isConnection()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key, delta);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long decrement(String key) {
        if (this.isConnection()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.decrement(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long decrement(String key, long delta) {
        if (this.isConnection()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.decrement(key, delta);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Integer append(String key, String value) {
        if (this.isConnection()) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.append(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean set(final String key, Object value) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean set(final String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean multiSet(final Map<String, Object> map) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.multiSet(map);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean multiSet(final Map<String, Object> map, long expireTime, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.multiSet(map);
            map.keySet().forEach(key -> {
                this.expire(key, expireTime, timeUnit);
            });
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean multiSetIfAbsent(final Map<String, Object> map) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.multiSetIfAbsent(map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean multiSetIfAbsent(final Map<String, Object> map, long expireTime, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.multiSetIfAbsent(map);
            map.keySet().forEach(key -> {
                this.expire(key, expireTime, timeUnit);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfAbsent(String key, Object value) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfAbsent(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfAbsent(String key, Object value, Duration duration) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfAbsent(key, value, duration);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfPresent(String key, Object value) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfPresent(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfPresent(String key, Object value, Duration duration) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfPresent(key, value, duration);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean set(final String key, Object value, long expireTime) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfAbsent(String key, Object value, long expireTime) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfAbsent(String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfAbsent(key, value, expireTime, timeUnit);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfPresent(String key, Object value, long expireTime) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfPresent(key, value, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean setIfPresent(String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return false;
        }
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            result = operations.setIfPresent(key, value, expireTime, timeUnit);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public long remove(final String... keys) {
        if (this.isConnection()) {
            return 0L;
        }
        return redisTemplate.delete(CollectionUtil.newArrayList(keys));
    }

    @Override
    public long remove(Collection<String> keys) {
        if (this.isConnection()) {
            return 0L;
        }
        return redisTemplate.delete(keys);
    }

    @Override
    public long removePattern(final String pattern) {
        if (this.isConnection()) {
            return 0L;
        }
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            return redisTemplate.delete(keys);
        }
        return 0L;
    }

    @Override
    public boolean remove(final String key) {
        if (this.isConnection()) {
            return false;
        }
        return redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(final String key) {
        if (this.isConnection()) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T get(final String key, final Class<T> cls) {
        if (this.isConnection()) {
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
    public <T> List<T> multiGet(Collection<String> keys, Class<T> cls) {
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
            return null;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.keys(key);
    }

    @Override
    public boolean hashHasKey(String key, String hashKey) {
        if (this.isConnection()) {
            return false;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.hasKey(key, hashKey);
    }

    @Override
    public void hashPut(String key, String hashKey, Object value) {
        if (this.isConnection()) {
            return;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    @Override
    public boolean hashPutIfAbsent(String key, String hashKey, Object value) {
        if (this.isConnection()) {
            return false;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.putIfAbsent(key, hashKey, value);
    }

    @Override
    public void hashPutAll(String key, Map<String, Object> values) {
        if (this.isConnection()) {
            return;
        }
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key, values);
    }

    @Override
    public <T> T hashGet(String key, String hashKey, final Class<T> cls) {
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
    public long hashDelete(String key, String... hasKey) {
        if (this.isConnection()) {
            return 0L;
        }
        return redisTemplate.opsForHash().delete(key, hasKey);
    }

    @Override
    public long hashDelete(String key, Collection<String> hasKey) {
        if (this.isConnection()) {
            return 0L;
        }
        return redisTemplate.opsForHash().delete(key, hasKey.toArray(new String[hasKey.size()]));
    }

    @Override
    public long listRightPush(String key, Object value) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPush(key, value);
    }

    @Override
    public long listRightPushAll(String key, Object... values) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPushAll(key, values);
    }

    @Override
    public long listRightPushAll(String key, Collection<String> values) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPushAll(key, values.toArray(new String[values.size()]));
    }

    @Override
    public long listRightPushIfPresent(String key, Object value) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPushIfPresent(key, value);
    }

    @Override
    public long listLeftPush(String key, Object value) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPush(key, value);
    }

    @Override
    public long listLeftPushAll(String key, Object... values) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPushAll(key, values);
    }

    @Override
    public long listLeftPushAll(String key, Collection<String> values) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPushAll(key, values.toArray(new String[values.size()]));
    }

    @Override
    public long listLeftPushIfPresent(String key, Object value) {
        if (this.isConnection()) {
            return 0L;
        }
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPushIfPresent(key, value);
    }

    @Override
    public <T> List<T> listRange(String key, long start, long end, Class<T> cls) {
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
    public long setAdd(String key, Object value) {
        if (this.isConnection()) {
            return 0L;
        }
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.add(key, value);
    }

    @Override
    public <T> Set<T> setMembers(String key, Class<T> cls) {
        if (this.isConnection()) {
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
    public long setRemove(String key, Object... values) {
        if (this.isConnection()) {
            return 0;
        }
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public long setRemove(String key, Collection<String> values) {
        if (this.isConnection()) {
            return 0;
        }
        return redisTemplate.opsForSet().remove(key, values.toArray(new String[values.size()]));
    }

    @Override
    public boolean setIsMember(String key, Object value) {
        if (this.isConnection()) {
            return false;
        }
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public <T> T setPop(String key, Class<T> cls) {
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
    public boolean zsetAdd(String key, Object value, double scoure) {
        if (this.isConnection()) {
            return false;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.add(key, value, scoure);
    }

    @Override
    public long zsetRemove(String key, Object... values) {
        if (this.isConnection()) {
            return 0;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, values);
    }

    @Override
    public long zsetRemove(String key, Collection<String> values) {
        if (this.isConnection()) {
            return 0;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, values.toArray(new String[values.size()]));
    }

    @Override
    public long zsetRemoveRangeByScore(String key, double min, double max) {
        if (this.isConnection()) {
            return 0;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, min, max);
    }

    @Override
    public long zsetRemoveRange(String key, long start, long end) {
        if (this.isConnection()) {
            return 0;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, start, end);
    }

    @Override
    public <T> Set<T> zsetRangeByScore(String key, double min, double max, Class<T> cls) {
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
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
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.size(key);
    }

    @Override
    public Long zsetZCard(String key) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.zCard(key);
    }

    @Override
    public Double zsetScore(String key, Object value) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, value);
    }

    @Override
    public Long zsetUnionAndStore(String key, String otherKey, String destKey) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.unionAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public Long zsetIntersectAndStore(String key, String otherKey, String destKey) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.intersectAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public <T> Set<T> zsetRangeByLex(String key, RedisZSetCommands.Range range, Class<T> cls) {
        if (this.isConnection()) {
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
    public <T> Set<T> zsetRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit, Class<T> cls) {
        if (this.isConnection()) {
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
    public long zsetCount(String key, double min, double max) {
        if (this.isConnection()) {
            return 0;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.count(key, min, max);
    }

    @Override
    public Double zsetIncrementScore(String key, Object value, double delta) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.incrementScore(key, value, delta);
    }

    @Override
    public Long zsetRank(String key, Object value) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rank(key, value);
    }

    @Override
    public Long zsetReverseRank(String key, Object value) {
        if (this.isConnection()) {
            return null;
        }
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRank(key, value);
    }

    @Override
    public void setDB(Integer index) {
        if (this.isConnection()) {
            return;
        }
        LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        connectionFactory.setDatabase(index);
    }

    @Override
    public boolean isConnection() {
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
    public boolean expire(String key, long expireTime) {
        if (this.isConnection()) {
            return false;
        }
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean expire(String key, long expireTime, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return false;
        }
        return redisTemplate.expire(key, expireTime, timeUnit);
    }

    @Override
    public long getExpire(String key) {
        if (this.isConnection()) {
            return 0;
        }
        return redisTemplate.getExpire(key);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> callback) {
        if (this.isConnection()) {
            return null;
        }
        return this.redisTemplate.executePipelined(callback);
    }


    @Override
    public long getExpire(String key, TimeUnit timeUnit) {
        if (this.isConnection()) {
            return 0;
        }
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public Set<String> getPattern(String pattern) {
        Set<String> values = this.redisTemplate.keys(pattern);
        if (values == null) {
            values = new HashSet();
        }
        return values;
    }

}
