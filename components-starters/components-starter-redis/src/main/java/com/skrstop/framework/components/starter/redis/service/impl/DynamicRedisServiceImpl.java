package com.skrstop.framework.components.starter.redis.service.impl;

import com.skrstop.framework.components.starter.redis.service.DynamicRedisService;
import com.skrstop.framework.components.starter.redis.service.RedisService;
import org.springframework.data.redis.connection.RedisZSetCommands;
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
public class DynamicRedisServiceImpl implements DynamicRedisService {

    private RedisService redisService;

    public DynamicRedisServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public Long increment(String dsKey, String key) {
        return this.redisService.increment(key);
    }

    @Override
    public Long increment(String dsKey, String key, long delta) {
        return this.redisService.increment(key, delta);
    }

    @Override
    public Double increment(String dsKey, String key, double delta) {
        return this.redisService.increment(key, delta);
    }

    @Override
    public Long decrement(String dsKey, String key) {
        return this.redisService.decrement(key);
    }

    @Override
    public Long decrement(String dsKey, String key, long delta) {
        return this.redisService.decrement(key, delta);
    }

    @Override
    public Integer append(String dsKey, String key, String value) {
        return this.redisService.append(key, value);
    }

    @Override
    public boolean set(String dsKey, String key, Object value) {
        return this.redisService.set(key, value);
    }

    @Override
    public boolean set(String dsKey, String key, Object value, long expireTime, TimeUnit timeUnit) {
        return this.redisService.set(key, value, expireTime, timeUnit);
    }

    @Override
    public boolean multiSet(String dsKey, Map<String, Object> map) {
        return this.redisService.multiSet(map);
    }

    @Override
    public boolean multiSet(String dsKey, Map<String, Object> map, long expireTime, TimeUnit timeUnit) {
        return this.redisService.multiSet(map, expireTime, timeUnit);
    }

    @Override
    public boolean multiSetIfAbsent(String dsKey, Map<String, Object> map) {
        return this.redisService.multiSetIfAbsent(map);
    }

    @Override
    public boolean multiSetIfAbsent(String dsKey, Map<String, Object> map, long expireTime, TimeUnit timeUnit) {
        return this.redisService.multiSetIfAbsent(map, expireTime, timeUnit);
    }

    @Override
    public boolean setIfAbsent(String dsKey, String key, Object value) {
        return this.redisService.setIfAbsent(key, value);
    }

    @Override
    public boolean setIfAbsent(String dsKey, String key, Object value, Duration duration) {
        return this.redisService.setIfAbsent(key, value, duration);
    }

    @Override
    public boolean setIfPresent(String dsKey, String key, Object value) {
        return this.redisService.setIfPresent(key, value);
    }

    @Override
    public boolean setIfPresent(String dsKey, String key, Object value, Duration duration) {
        return this.redisService.setIfPresent(key, value, duration);
    }

    @Override
    public boolean set(String dsKey, String key, Object value, long expireTime) {
        return this.redisService.set(key, value, expireTime);
    }

    @Override
    public boolean setIfAbsent(String dsKey, String key, Object value, long expireTime) {
        return this.redisService.setIfAbsent(key, value, expireTime);
    }

    @Override
    public boolean setIfAbsent(String dsKey, String key, Object value, long expireTime, TimeUnit timeUnit) {
        return this.redisService.setIfAbsent(key, value, expireTime, timeUnit);
    }

    @Override
    public boolean setIfPresent(String dsKey, String key, Object value, long expireTime) {
        return this.redisService.setIfPresent(key, value, expireTime);
    }

    @Override
    public boolean setIfPresent(String dsKey, String key, Object value, long expireTime, TimeUnit timeUnit) {
        return this.redisService.setIfPresent(key, value, expireTime, timeUnit);
    }

    @Override
    public long remove(String dsKey, String... keys) {
        return this.redisService.remove(keys);
    }

    @Override
    public long remove(String dsKey, Collection<String> keys) {
        return this.redisService.remove(keys);
    }

    @Override
    public long removePattern(String dsKey, String pattern) {
        return this.redisService.removePattern(pattern);
    }

    @Override
    public boolean remove(String dsKey, String key) {
        return this.redisService.remove(key);
    }

    @Override
    public boolean hasKey(String dsKey, String key) {
        return this.redisService.hasKey(key);
    }

    @Override
    public <T> T get(String dsKey, String key, Class<T> cls) {
        return this.redisService.get(key, cls);
    }

    @Override
    public <T> List<T> multiGet(String dsKey, Collection<String> keys, Class<T> cls) {
        return this.redisService.multiGet(keys, cls);
    }

    @Override
    public <T> T getAndSet(String dsKey, String key, Class<T> cls, T value) {
        return this.redisService.getAndSet(key, cls, value);
    }

    @Override
    public <T> T getAndSet(String dsKey, String key, Class<T> cls, T value, long expireTime) {
        return this.redisService.getAndSet(key, cls, value, expireTime);
    }

    @Override
    public <T> T getAndSet(String dsKey, String key, Class<T> cls, T value, long expireTime, TimeUnit timeUnit) {
        return this.redisService.getAndSet(key, cls, value, expireTime, timeUnit);
    }

    @Override
    public Set<String> hashKeys(String dsKey, String key) {
        return this.redisService.hashKeys(key);
    }

    @Override
    public boolean hashHasKey(String dsKey, String key, String hashKey) {
        return this.redisService.hashHasKey(key, hashKey);
    }

    @Override
    public void hashPut(String dsKey, String key, String hashKey, Object value) {
        this.redisService.hashPut(key, hashKey, value);
    }

    @Override
    public boolean hashPutIfAbsent(String dsKey, String key, String hashKey, Object value) {
        return this.redisService.hashPutIfAbsent(key, hashKey, value);
    }

    @Override
    public void hashPutAll(String dsKey, String key, Map<String, Object> values) {
        this.redisService.hashPutAll(key, values);
    }

    @Override
    public <T> T hashGet(String dsKey, String key, String hashKey, Class<T> cls) {
        return this.redisService.hashGet(key, hashKey, cls);
    }

    @Override
    public <T> List<T> hashGet(String dsKey, String key, List<String> hashKeys, Class<T> cls) {
        return this.redisService.hashGet(key, hashKeys, cls);
    }

    @Override
    public long hashDelete(String dsKey, String key, String... hasKey) {
        return this.redisService.hashDelete(key, hasKey);
    }

    @Override
    public long hashDelete(String dsKey, String key, Collection<String> hasKey) {
        return this.redisService.hashDelete(key, hasKey);
    }

    @Override
    public long listRightPush(String dsKey, String key, Object value) {
        return this.redisService.listRightPush(key, value);
    }

    @Override
    public long listRightPushAll(String dsKey, String key, Object... values) {
        return this.redisService.listRightPushAll(key, values);
    }

    @Override
    public long listRightPushAll(String dsKey, String key, Collection<String> values) {
        return this.redisService.listRightPushAll(key, values);
    }

    @Override
    public long listRightPushIfPresent(String dsKey, String key, Object value) {
        return this.redisService.listRightPushIfPresent(key, value);
    }

    @Override
    public long listLeftPush(String dsKey, String key, Object value) {
        return this.redisService.listLeftPush(key, value);
    }

    @Override
    public long listLeftPushAll(String dsKey, String key, Object... values) {
        return this.redisService.listLeftPushAll(key, values);
    }

    @Override
    public long listLeftPushAll(String dsKey, String key, Collection<String> values) {
        return this.redisService.listLeftPushAll(key, values);
    }

    @Override
    public long listLeftPushIfPresent(String dsKey, String key, Object value) {
        return this.redisService.listLeftPushIfPresent(key, value);
    }

    @Override
    public <T> List<T> listRange(String dsKey, String key, long start, long end, Class<T> cls) {
        return this.redisService.listRange(key, start, end, cls);
    }

    @Override
    public <T> T listLeftPop(String dsKey, String key, Class<T> cls) {
        return this.redisService.listLeftPop(key, cls);
    }

    @Override
    public <T> T listRightPop(String dsKey, String key, Class<T> cls) {
        return this.redisService.listRightPop(key, cls);
    }

    @Override
    public long setAdd(String dsKey, String key, Object value) {
        return this.redisService.setAdd(key, value);
    }

    @Override
    public <T> Set<T> setMembers(String dsKey, String key, Class<T> cls) {
        return this.redisService.setMembers(key, cls);
    }

    @Override
    public long setRemove(String dsKey, String key, Object... value) {
        return this.redisService.setRemove(key, value);
    }

    @Override
    public long setRemove(String dsKey, String key, Collection<String> values) {
        return this.redisService.setRemove(key, values);
    }

    @Override
    public boolean setIsMember(String dsKey, String key, Object value) {
        return this.redisService.setIsMember(key, value);
    }

    @Override
    public <T> T setPop(String dsKey, String key, Class<T> cls) {
        return this.redisService.setPop(key, cls);
    }

    @Override
    public <T> List<T> setPop(String dsKey, String key, Class<T> cls, int count) {
        return this.redisService.setPop(key, cls, count);
    }

    @Override
    public boolean zsetAdd(String dsKey, String key, Object value, double scoure) {
        return this.redisService.zsetAdd(key, value, scoure);
    }

    @Override
    public long zsetRemove(String dsKey, String key, Object... values) {
        return this.redisService.zsetRemove(key, values);
    }

    @Override
    public long zsetRemove(String dsKey, String key, Collection<String> values) {
        return this.redisService.zsetRemove(key, values);
    }

    @Override
    public long zsetRemoveRangeByScore(String dsKey, String key, double min, double max) {
        return this.redisService.zsetRemoveRangeByScore(key, min, max);
    }

    @Override
    public long zsetRemoveRange(String dsKey, String key, long start, long end) {
        return this.redisService.zsetRemoveRange(key, start, end);
    }

    @Override
    public <T> Set<T> zsetRangeByScore(String dsKey, String key, double min, double max, Class<T> cls) {
        return this.redisService.zsetRangeByScore(key, min, max, cls);
    }

    @Override
    public <T> Set<T> zsetRange(String dsKey, String key, long start, long end, Class<T> cls) {
        return this.redisService.zsetRange(key, start, end, cls);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeWithScores(String dsKey, String key, long start, long end, Class<T> cls) {
        return this.redisService.zsetRangeWithScores(key, start, end, cls);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(String dsKey, String key, double min, double max, Class<T> cls) {
        return this.redisService.zsetRangeByScoreWithScores(key, min, max, cls);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetRangeByScoreWithScores(String dsKey, String key, double min, double max, long offset, long count, Class<T> cls) {
        return this.redisService.zsetRangeByScoreWithScores(key, min, max, offset, count, cls);
    }

    @Override
    public <T> Set<T> zsetReverseRange(String dsKey, String key, long start, long end, Class<T> cls) {
        return this.redisService.zsetReverseRange(key, start, end, cls);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeWithScores(String dsKey, String key, long start, long end, Class<T> cls) {
        return this.redisService.zsetReverseRangeWithScores(key, start, end, cls);
    }

    @Override
    public <T> Set<T> zsetReverseRangeByScore(String dsKey, String key, double min, double max, Class<T> cls) {
        return this.redisService.zsetReverseRangeByScore(key, min, max, cls);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(String dsKey, String key, double min, double max, Class<T> cls) {
        return this.redisService.zsetReverseRangeByScoreWithScores(key, min, max, cls);
    }

    @Override
    public <T> Set<T> zsetReverseRangeByScore(String dsKey, String key, double min, double max, long offset, long count, Class<T> cls) {
        return this.redisService.zsetReverseRangeByScore(key, min, max, offset, count, cls);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> zsetReverseRangeByScoreWithScores(String dsKey, String key, double min, double max, long offset, long count, Class<T> cls) {
        return this.redisService.zsetReverseRangeByScoreWithScores(key, min, max, offset, count, cls);
    }

    @Override
    public Long zsetSize(String dsKey, String key) {
        return this.redisService.zsetSize(key);
    }

    @Override
    public Long zsetZCard(String dsKey, String key) {
        return this.redisService.zsetZCard(key);
    }

    @Override
    public Double zsetScore(String dsKey, String key, Object value) {
        return this.redisService.zsetScore(key, value);
    }

    @Override
    public Long zsetUnionAndStore(String dsKey, String key, String otherKey, String destKey) {
        return this.redisService.zsetUnionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String dsKey, String key, Collection<String> otherKeys, String destKey) {
        return this.redisService.zsetUnionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String dsKey, String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return this.redisService.zsetUnionAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public Long zsetIntersectAndStore(String dsKey, String key, String otherKey, String destKey) {
        return this.redisService.zsetIntersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String dsKey, String key, Collection<String> otherKeys, String destKey) {
        return this.redisService.zsetIntersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String dsKey, String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return this.redisService.zsetIntersectAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public <T> Set<T> zsetRangeByLex(String dsKey, String key, RedisZSetCommands.Range range, Class<T> cls) {
        return this.redisService.zsetRangeByLex(key, range, cls);
    }

    @Override
    public <T> Set<T> zsetRangeByLex(String dsKey, String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit, Class<T> cls) {
        return this.redisService.zsetRangeByLex(key, range, limit, cls);
    }

    @Override
    public long zsetCount(String dsKey, String key, double min, double max) {
        return this.redisService.zsetCount(key, min, max);
    }

    @Override
    public Double zsetIncrementScore(String dsKey, String key, Object value, double delta) {
        return this.redisService.zsetIncrementScore(key, value, delta);
    }

    @Override
    public Long zsetRank(String dsKey, String key, Object value) {
        return this.redisService.zsetRank(key, value);
    }

    @Override
    public Long zsetReverseRank(String dsKey, String key, Object value) {
        return this.redisService.zsetReverseRank(key, value);
    }

    @Override
    public void setDB(String dsKey, Integer index) {
        this.redisService.setDB(index);
    }

    @Override
    public boolean isConnection(String dsKey) {
        return this.redisService.isConnection();
    }

    @Override
    public boolean expire(String dsKey, String key, long expireTime) {
        return this.redisService.expire(key, expireTime);
    }

    @Override
    public boolean expire(String dsKey, String key, long expireTime, TimeUnit timeUnit) {
        return this.redisService.expire(key, expireTime, timeUnit);
    }

    @Override
    public long getExpire(String dsKey, String key) {
        return this.redisService.getExpire(key);
    }

    @Override
    public List<Object> executePipelined(String dsKey, SessionCallback<?> callback) {
        return this.redisService.executePipelined(callback);
    }

    @Override
    public long getExpire(String dsKey, String key, TimeUnit timeUnit) {
        return this.redisService.getExpire(key, timeUnit);
    }

    @Override
    public Set<String> getPattern(String dsKey, String pattern) {
        return this.redisService.getPattern(pattern);
    }
}
