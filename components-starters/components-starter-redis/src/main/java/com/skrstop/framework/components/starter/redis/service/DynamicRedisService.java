package com.skrstop.framework.components.starter.redis.service;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * DynamicRedisService class
 *
 * @author 蒋时华
 * @date 2023-12-08
 */
public interface DynamicRedisService {

    /**
     * 自增
     *
     * @param dsKey
     * @param key
     * @return
     */
    Long increment(final String dsKey, final String key);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Long increment(final String dsKey, final String key, final long delta);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Double increment(final String dsKey, final String key, final double delta);

    /**
     * 自减
     *
     * @param key
     * @return
     */
    Long decrement(final String dsKey, final String key);

    /**
     * 自减
     *
     * @param key
     * @return
     */
    Long decrement(final String dsKey, final String key, long delta);

    /**
     * 追加
     *
     * @param key
     * @return
     */
    Integer append(final String dsKey, final String key, String value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String dsKey, final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String dsKey, final String key, Object value, final long expireTime, final TimeUnit timeUnit);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSet(final String dsKey, final Map<String, Object> map);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSet(final String dsKey, final Map<String, Object> map, final long expireTime, final TimeUnit timeUnit);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSetIfAbsent(final String dsKey, final Map<String, Object> map);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSetIfAbsent(final String dsKey, final Map<String, Object> map, final long expireTime, final TimeUnit timeUnit);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String dsKey, final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String dsKey, final String key, Object value, Duration duration);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String dsKey, final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String dsKey, final String key, Object value, Duration duration);

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String dsKey, final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String dsKey, final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String dsKey, final String key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String dsKey, final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String dsKey, final String key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 批量删除对应的value
     *
     * @param keys
     * @return
     */
    long remove(final String dsKey, final String... keys);

    /**
     * 批量删除对应的value
     *
     * @param keys
     * @return
     */
    long remove(final String dsKey, final Collection<String> keys);

    /**
     * 批量删除key
     *
     * @param pattern
     * @return
     */
    long removePattern(final String dsKey, final String pattern);

    /**
     * 删除对应的value
     *
     * @param key
     * @return
     */
    boolean remove(final String dsKey, final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    boolean hasKey(final String dsKey, final String key);

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    <T> T get(final String dsKey, final String key, final Class<T> cls);

    /**
     * 批量读取
     *
     * @param keys
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> multiGet(final String dsKey, Collection<String> keys, final Class<T> cls);

    /**
     * 读取缓存并且赋值
     *
     * @param key
     * @return
     */
    <T> T getAndSet(final String dsKey, final String key, final Class<T> cls, T value);

    /**
     * 读取缓存并且赋值，设置过期时间
     *
     * @param key
     * @return
     */
    <T> T getAndSet(final String dsKey, final String key, final Class<T> cls, T value, long expireTime);

    /**
     * 读取缓存并且赋值，设置过期时间
     *
     * @param key
     * @return
     */
    <T> T getAndSet(final String dsKey, String key, Class<T> cls, T value, long expireTime, TimeUnit timeUnit);

    /**
     * 获取hash中的所有key
     *
     * @param key
     * @return
     */
    Set<String> hashKeys(final String dsKey, String key);

    /**
     * hash中是否有key
     *
     * @param key
     * @param hashKey
     * @return
     */
    boolean hashHasKey(final String dsKey, String key, String hashKey);

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    void hashPut(final String dsKey, String key, String hashKey, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    boolean hashPutIfAbsent(final String dsKey, String key, String hashKey, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    void hashPutAll(final String dsKey, String key, Map<String, Object> values);

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    <T> T hashGet(final String dsKey, String key, String hashKey, final Class<T> cls);

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKeys
     * @return
     */
    <T> List<T> hashGet(final String dsKey, String key, List<String> hashKeys, final Class<T> cls);

    /**
     * 哈希删除数据
     *
     * @param key
     * @param hasKey
     * @return
     */
    long hashDelete(final String dsKey, String key, String... hasKey);

    /**
     * 哈希删除数据
     *
     * @param key
     * @param hasKey
     * @return
     */
    long hashDelete(final String dsKey, String key, Collection<String> hasKey);

    /**
     * 列表右添加
     *
     * @param key
     * @param value
     */
    long listRightPush(final String dsKey, String key, Object value);

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    long listRightPushAll(final String dsKey, String key, Object... values);

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    long listRightPushAll(final String dsKey, String key, Collection<String> values);

    /**
     * 列表右添加
     *
     * @param key
     * @param value
     */
    long listRightPushIfPresent(final String dsKey, String key, Object value);

    /**
     * 列表左添加
     *
     * @param key
     * @param value
     */
    long listLeftPush(final String dsKey, String key, Object value);

    /**
     * 列表左添加
     *
     * @param key
     * @param values
     */
    long listLeftPushAll(final String dsKey, String key, Object... values);

    /**
     * 列表左添加
     *
     * @param key
     * @param values
     */
    long listLeftPushAll(final String dsKey, String key, Collection<String> values);

    /**
     * 列表左添加
     *
     * @param key
     * @param value
     */
    long listLeftPushIfPresent(final String dsKey, String key, Object value);

    /**
     * 列表获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    <T> List<T> listRange(final String dsKey, String key, long start, long end, Class<T> cls);

    /**
     * 左弹出获取
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    <T> T listLeftPop(final String dsKey, final String key, final Class<T> cls);

    /**
     * 右弹出获取
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    <T> T listRightPop(final String dsKey, final String key, final Class<T> cls);


    /**
     * 集合添加
     *
     * @param key
     * @param value
     * @return
     */
    long setAdd(final String dsKey, String key, Object value);

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    <T> Set<T> setMembers(final String dsKey, String key, Class<T> cls);

    long setRemove(final String dsKey, String key, Object... value);

    long setRemove(final String dsKey, String key, Collection<String> values);

    boolean setIsMember(final String dsKey, String key, Object value);

    <T> T setPop(final String dsKey, String key, Class<T> cls);

    <T> List<T> setPop(final String dsKey, String key, Class<T> cls, int count);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    boolean zsetAdd(final String dsKey, String key, Object value, double scoure);

    long zsetRemove(final String dsKey, String key, Object... values);

    long zsetRemove(final String dsKey, String key, Collection<String> values);

    long zsetRemoveRangeByScore(final String dsKey, String key, double min, double max);

    long zsetRemoveRange(final String dsKey, String key, long start, long end);

    /**
     * 有序集合获取
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    <T> Set<T> zsetRangeByScore(final String dsKey, String key, double min, double max, Class<T> cls);

    <T> Set<T> zsetRange(final String dsKey, String key, long start, long end, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeWithScores(final String dsKey, String key, long start, long end, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(final String dsKey, String key, double min, double max, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(final String dsKey, String key, double min, double max, long offset, long count, Class<T> cls);

    <T> Set<T> zsetReverseRange(final String dsKey, String key, long start, long end, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeWithScores(final String dsKey, String key, long start, long end, Class<T> cls);

    <T> Set<T> zsetReverseRangeByScore(final String dsKey, String key, double min, double max, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(final String dsKey, String key, double min, double max, Class<T> cls);

    <T> Set<T> zsetReverseRangeByScore(final String dsKey, String key, double min, double max, long offset, long count, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(final String dsKey, String key, double min, double max, long offset, long count, Class<T> cls);

    Long zsetSize(final String dsKey, String key);

    Long zsetZCard(final String dsKey, String key);

    Double zsetScore(final String dsKey, String key, Object value);

    Long zsetUnionAndStore(final String dsKey, String key, String otherKey, String destKey);

    Long zsetUnionAndStore(final String dsKey, String key, Collection<String> otherKeys, String destKey);

    default Long zsetUnionAndStore(final String dsKey, String key, Collection<String> otherKeys, String destKey, Aggregate aggregate) {
        return zsetUnionAndStore(dsKey, key, otherKeys, destKey, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
    }

    Long zsetUnionAndStore(final String dsKey, String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights);

    Long zsetIntersectAndStore(final String dsKey, String key, String otherKey, String destKey);

    Long zsetIntersectAndStore(final String dsKey, String key, Collection<String> otherKeys, String destKey);

    default Long zsetIntersectAndStore(final String dsKey, String key, Collection<String> otherKeys, String destKey, Aggregate aggregate) {
        return zsetIntersectAndStore(dsKey, key, otherKeys, destKey, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
    }

    Long zsetIntersectAndStore(final String dsKey, String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights);

    <T> Set<T> zsetRangeByLex(final String dsKey, String key, Range<String> range, Class<T> cls);

    <T> Set<T> zsetRangeByLex(final String dsKey, String key, Range<String> range, Limit limit, Class<T> cls);

    long zsetCount(final String dsKey, String key, double min, double max);

    Double zsetIncrementScore(final String dsKey, String key, Object value, double delta);

    Long zsetRank(final String dsKey, String key, Object value);

    Long zsetReverseRank(final String dsKey, String key, Object value);

    /**
     * 选择当前使用库
     *
     * @param index
     */
    void setDB(final String dsKey, Integer index);

    /**
     * 判断redis连接是否存在
     *
     * @return
     */
    boolean isConnection(final String dsKey);

    /**
     * 通过key设置过期时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    boolean expire(final String dsKey, String key, long expireTime);

    /**
     * 通过key设置过期时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    boolean expire(final String dsKey, String key, long expireTime, TimeUnit timeUnit);

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    long getExpire(final String dsKey, String key);

    /**
     * pipline执行
     *
     * @param callback
     * @return
     */
    List<Object> executePipelined(final String dsKey, SessionCallback<?> callback);

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    long getExpire(final String dsKey, String key, TimeUnit timeUnit);

    Set<String> getPattern(final String dsKey, String pattern);
}
