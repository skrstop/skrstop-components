package com.skrstop.framework.components.starter.redis.service;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
    Boolean set(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean set(final String key, Object value, final long expireTime, final TimeUnit timeUnit);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    Boolean multiSet(final Map<String, Object> map);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    Boolean multiSet(final Map<String, Object> map, final long expireTime, final TimeUnit timeUnit);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    Boolean multiSetIfAbsent(final Map<String, Object> map);

    /**
     * 批量写入
     *
     * @param map
     * @return
     */
    Boolean multiSetIfAbsent(final Map<String, Object> map, final long expireTime, final TimeUnit timeUnit);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfAbsent(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfAbsent(final String key, Object value, Duration duration);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfPresent(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfPresent(final String key, Object value, Duration duration);

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    Boolean set(final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfAbsent(final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfAbsent(final String key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfPresent(final String key, Object value, long expireTime);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setIfPresent(final String key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 批量删除对应的value
     *
     * @param keys
     * @return
     */
    Long remove(final String... keys);

    /**
     * 批量删除对应的value
     *
     * @param keys
     * @return
     */
    Long remove(final Collection<String> keys);

    /**
     * 批量删除key
     *
     * @param pattern
     * @return
     */
    Long removePattern(final String pattern);

    /**
     * 删除对应的value
     *
     * @param key
     * @return
     */
    Boolean remove(final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    Boolean hasKey(final String key);

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
    Boolean hashHasKey(String key, String hashKey);

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    Boolean hashPut(String key, String hashKey, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    Boolean hashPutIfAbsent(String key, String hashKey, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    Boolean hashPutAll(String key, Map<String, Object> values);

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
    Long hashDelete(String key, String... hasKey);

    /**
     * 哈希删除数据
     *
     * @param key
     * @param hasKey
     * @return
     */
    Long hashDelete(String key, Collection<String> hasKey);

    /**
     * 列表右添加
     *
     * @param key
     * @param value
     */
    Long listRightPush(String key, Object value);

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    Long listRightPushAll(String key, Object... values);

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    Long listRightPushAll(String key, Collection<String> values);

    /**
     * 列表右添加
     *
     * @param key
     * @param value
     */
    Long listRightPushIfPresent(String key, Object value);

    /**
     * 列表左添加
     *
     * @param key
     * @param value
     */
    Long listLeftPush(String key, Object value);

    /**
     * 列表左添加
     *
     * @param key
     * @param values
     */
    Long listLeftPushAll(String key, Object... values);

    /**
     * 列表左添加
     *
     * @param key
     * @param values
     */
    Long listLeftPushAll(String key, Collection<String> values);

    /**
     * 列表左添加
     *
     * @param key
     * @param value
     */
    Long listLeftPushIfPresent(String key, Object value);

    /**
     * 列表大小
     *
     * @param key
     */
    Long listSize(String key);

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
    Long setAdd(String key, Object value);

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    <T> Set<T> setMembers(String key, Class<T> cls);

    Long setRemove(String key, Object... value);

    Long setRemove(String key, Collection<String> values);

    Boolean setIsMember(String key, Object value);

    <T> T setPop(String key, Class<T> cls);

    <T> List<T> setPop(String key, Class<T> cls, int count);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    Boolean zsetAdd(String key, Object value, double scoure);

    Long zsetRemove(String key, Object... values);

    Long zsetRemove(String key, Collection<String> values);

    Long zsetRemoveRangeByScore(String key, double min, double max);

    Long zsetRemoveRange(String key, long start, long end);

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

    default Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate) {
        return zsetUnionAndStore(key, otherKeys, destKey, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
    }

    Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights);

    Long zsetIntersectAndStore(String key, String otherKey, String destKey);

    Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey);

    default Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate) {
        return zsetIntersectAndStore(key, otherKeys, destKey, aggregate, Weights.fromSetCount(1 + otherKeys.size()));
    }

    Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, Aggregate aggregate, Weights weights);

    <T> Set<T> zsetRangeByLex(String key, Range<String> range, Class<T> cls);

    <T> Set<T> zsetRangeByLex(String key, Range<String> range, Limit limit, Class<T> cls);

    Long zsetCount(String key, double min, double max);

    Double zsetIncrementScore(String key, Object value, double delta);

    Long zsetRank(String key, Object value);

    Long zsetReverseRank(String key, Object value);

    /**
     * 选择当前使用库
     *
     * @param index
     */
    @Deprecated
    Boolean setDB(Integer index);

    /**
     * 判断redis连接是否存在
     *
     * @return
     */
    boolean isConnectionClose();

    /**
     * 通过key设置过期时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    Boolean expire(String key, long expireTime);

    /**
     * 通过key设置过期时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    Boolean expire(String key, long expireTime, TimeUnit timeUnit);

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    Long getExpire(String key);

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    Long getExpire(String key, TimeUnit timeUnit);

    /**
     * pipline执行
     *
     * @param callback
     * @return
     */
    List<Object> executePipelined(SessionCallback<?> callback);

    Set getPattern(String pattern);

    RedisTemplate getRedisTemplate();

}
