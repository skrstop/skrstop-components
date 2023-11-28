package com.jphoebe.framework.components.starter.redis.service;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisService class
 *
 * @author 蒋时华
 * @date 2017/12/18
 */
public interface RedisService {

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Long increment(final String key);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Long increment(final String key, final long delta);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Double increment(final String key, final double delta);

    /**
     * 自减
     *
     * @param key
     * @return
     */
    Long decrement(final String key);

    /**
     * 自减
     *
     * @param key
     * @return
     */
    Long decrement(final String key, long delta);

    /**
     * 追加
     *
     * @param key
     * @return
     */
    Integer append(final String key, String value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Object value, final long expireTime, final TimeUnit timeUnit);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSet(final Map<String, Object> map);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSet(final Map<String, Object> map, final long expireTime, final TimeUnit timeUnit);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSetIfAbsent(final Map<String, Object> map);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    boolean multiSetIfAbsent(final Map<String, Object> map, final long expireTime, final TimeUnit timeUnit);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String key, Object value, Duration duration);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String key, Object value, Duration duration);

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfAbsent(final String key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean setIfPresent(final String key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 批量删除对应的value
     *
     * @param keys
     * @return
     */
    long remove(final String... keys);

    /**
     * 批量删除对应的value
     *
     * @param keys
     * @return
     */
    long remove(final Collection<String> keys);

    /**
     * 批量删除key
     *
     * @param pattern
     * @return
     */
    long removePattern(final String pattern);

    /**
     * 删除对应的value
     *
     * @param key
     * @return
     */
    boolean remove(final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    boolean hasKey(final String key);

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    <T> T get(final String key, final Class<T> cls);

    /**
     * 批量读取
     *
     * @param keys
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> multiGet(Collection<String> keys, final Class<T> cls);

    /**
     * 读取缓存并且赋值
     *
     * @param key
     * @return
     */
    <T> T getAndSet(final String key, final Class<T> cls, T value);

    /**
     * 读取缓存并且赋值，设置过期时间
     *
     * @param key
     * @return
     */
    <T> T getAndSet(final String key, final Class<T> cls, T value, long expireTime);

    /**
     * 读取缓存并且赋值，设置过期时间
     *
     * @param key
     * @return
     */
    <T> T getAndSet(String key, Class<T> cls, T value, long expireTime, TimeUnit timeUnit);

    /**
     * 获取hash中的所有key
     *
     * @param key
     * @return
     */
    Set<String> hashKeys(String key);

    /**
     * hash中是否有key
     *
     * @param key
     * @param hashKey
     * @return
     */
    boolean hashHasKey(String key, String hashKey);

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    void hashPut(String key, String hashKey, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    boolean hashPutIfAbsent(String key, String hashKey, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    void hashPutAll(String key, Map<String, Object> values);

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    <T> T hashGet(String key, String hashKey, final Class<T> cls);

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKeys
     * @return
     */
    <T> List<T> hashGet(String key, List<String> hashKeys, final Class<T> cls);

    /**
     * 哈希删除数据
     *
     * @param key
     * @param hasKey
     * @return
     */
    long hashDelete(String key, String... hasKey);

    /**
     * 哈希删除数据
     *
     * @param key
     * @param hasKey
     * @return
     */
    long hashDelete(String key, Collection<String> hasKey);

    /**
     * 列表右添加
     *
     * @param key
     * @param value
     */
    long listRightPush(String key, Object value);

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    long listRightPushAll(String key, Object... values);

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    long listRightPushAll(String key, Collection<String> values);

    /**
     * 列表右添加
     *
     * @param key
     * @param value
     */
    long listRightPushIfPresend(String key, Object value);

    /**
     * 列表左添加
     *
     * @param key
     * @param value
     */
    long listLeftPush(String key, Object value);

    /**
     * 列表左添加
     *
     * @param key
     * @param values
     */
    long listLeftPushAll(String key, Object... values);

    /**
     * 列表左添加
     *
     * @param key
     * @param values
     */
    long listLeftPushAll(String key, Collection<String> values);

    /**
     * 列表左添加
     *
     * @param key
     * @param value
     */
    long listLeftPushIfPresend(String key, Object value);

    /**
     * 列表获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    <T> List<T> listRange(String key, long start, long end, Class<T> cls);

    /**
     * 左弹出获取
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    <T> T listLeftPop(final String key, final Class<T> cls);

    /**
     * 右弹出获取
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    <T> T listRightPop(final String key, final Class<T> cls);


    /**
     * 集合添加
     *
     * @param key
     * @param value
     * @return
     */
    long setAdd(String key, Object value);

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    <T> Set<T> setMembers(String key, Class<T> cls);

    long setRemove(String key, Object... value);

    long setRemove(String key, Collection<String> values);

    boolean setIsMember(String key, Object value);

    <T> T setPop(String key, Class<T> cls);

    <T> List<T> setPop(String key, Class<T> cls, int count);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    boolean zsetAdd(String key, Object value, double scoure);

    long zsetRemove(String key, Object... values);

    long zsetRemove(String key, Collection<String> values);

    long zsetRemoveRangeByScore(String key, double min, double max);

    long zsetRemoveRange(String key, long start, long end);

    /**
     * 有序集合获取
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    <T> Set<T> zsetRangeByScore(String key, double min, double max, Class<T> cls);

    <T> Set<T> zsetRange(String key, long start, long end, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeWithScores(String key, long start, long end, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(String key, double min, double max, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> cls);

    <T> Set<T> zsetReverseRange(String key, long start, long end, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeWithScores(String key, long start, long end, Class<T> cls);

    <T> Set<T> zsetReverseRangeByScore(String key, double min, double max, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(String key, double min, double max, Class<T> cls);

    <T> Set<T> zsetReverseRangeByScore(String key, double min, double max, long offset, long count, Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> cls);

    Long zsetSize(String key);

    Long zsetZCard(String key);

    Double zsetScore(String key, Object value);

    Long zsetUnionAndStore(String key, String otherKey, String destKey);

    Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey);

    default Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate) {
        return zsetUnionAndStore(key, otherKeys, destKey, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights);

    Long zsetIntersectAndStore(String key, String otherKey, String destKey);

    Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey);

    default Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate) {
        return zsetIntersectAndStore(key, otherKeys, destKey, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights);

    <T> Set<T> zsetRangeByLex(String key, RedisZSetCommands.Range range, Class<T> cls);

    <T> Set<T> zsetRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit, Class<T> cls);

    long zsetCount(String key, double min, double max);

    Double zsetIncrementScore(String key, Object value, double delta);

    Long zsetRank(String key, Object value);

    Long zsetReverseRank(String key, Object value);

    /**
     * 选择当前使用库
     *
     * @param index
     */
    void setDB(Integer index);

    /**
     * 判断redis连接是否存在
     *
     * @return
     */
    boolean isConnection();

    /**
     * 通过key设置过期时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    boolean expire(String key, long expireTime);

    /**
     * 通过key设置过期时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    boolean expire(String key, long expireTime, TimeUnit timeUnit);

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    long getExpire(String key);

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    long getExpire(String key, TimeUnit timeUnit);

    Set getPattern(String pattern);

    RedisTemplate getRedisTemplate();

}
