package com.zoe.framework.components.starter.redis.service.impl;

import com.zoe.framework.components.starter.redis.configuration.redissonDynamic.MultiRedisson;
import org.redisson.api.*;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonCodec;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2023-12-15 09:59:17
 */
public class DynamicAopRedissonClient {

    private final MultiRedisson multiRedisson;

    public DynamicAopRedissonClient(MultiRedisson multiRedisson) {
        this.multiRedisson = multiRedisson;
    }

    /**
     * Returns time-series instance by <code>name</code>
     *
     * @param <V>  value type
     * @param <L>  label type
     * @param name - name of instance
     * @return RTimeSeries object
     */
    public <V, L> RTimeSeries<V, L> getTimeSeries(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getTimeSeries(name);
    }

    /**
     * Returns time-series instance by <code>name</code>
     * using provided <code>codec</code> for values.
     *
     * @param <V>   value type
     * @param <L>   label type
     * @param name  - name of instance
     * @param codec - codec for values
     * @return RTimeSeries object
     */
    public <V, L> RTimeSeries<V, L> getTimeSeries(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getTimeSeries(name, codec);
    }

    /**
     * Returns stream instance by <code>name</code>
     * <p>
     * Requires <b>Redis 5.0.0 and higher.</b>
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name of stream
     * @return RStream object
     */
    public <K, V> RStream<K, V> getStream(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getStream(name);
    }

    /**
     * Returns stream instance by <code>name</code>
     * using provided <code>codec</code> for entries.
     * <p>
     * Requires <b>Redis 5.0.0 and higher.</b>
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - name of stream
     * @param codec - codec for entry
     * @return RStream object
     */
    public <K, V> RStream<K, V> getStream(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getStream(name, codec);
    }

    /**
     * Returns API for RediSearch module
     *
     * @return RSearch object
     */
    public RSearch getSearch() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSearch();
    }

    /**
     * Returns API for RediSearch module using defined codec for attribute values.
     *
     * @return RSearch object
     */
    public RSearch getSearch(Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSearch(codec);
    }

    /**
     * Returns rate limiter instance by <code>name</code>
     *
     * @param name of rate limiter
     * @return RateLimiter object
     */
    public RRateLimiter getRateLimiter(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRateLimiter(name);
    }

    /**
     * Returns binary stream holder instance by <code>name</code>
     *
     * @param name of binary stream
     * @return BinaryStream object
     */
    public RBinaryStream getBinaryStream(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBinaryStream(name);
    }

    /**
     * Returns geospatial items holder instance by <code>name</code>.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return Geo object
     */
    public <V> RGeo<V> getGeo(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getGeo(name);
    }

    /**
     * Returns geospatial items holder instance by <code>name</code>
     * using provided codec for geospatial members.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for value
     * @return Geo object
     */
    public <V> RGeo<V> getGeo(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getGeo(name, codec);
    }

    /**
     * Returns set-based cache instance by <code>name</code>.
     * Supports value eviction with a given TTL value.
     *
     * <p>If eviction is not required then it's better to use regular map {@link #getSet(String, Codec)}.</p>
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return SetCache object
     */
    public <V> RSetCache<V> getSetCache(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSetCache(name);
    }

    /**
     * Returns set-based cache instance by <code>name</code>.
     * Supports value eviction with a given TTL value.
     *
     * <p>If eviction is not required then it's better to use regular map {@link #getSet(String, Codec)}.</p>
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return SetCache object
     */
    public <V> RSetCache<V> getSetCache(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSetCache(name, codec);
    }

    /**
     * Returns map-based cache instance by <code>name</code>
     * using provided <code>codec</code> for both cache keys and values.
     * Supports entry eviction with a given MaxIdleTime and TTL settings.
     * <p>
     * If eviction is not required then it's better to use regular map {@link #getMap(String, Codec)}.
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - object name
     * @param codec - codec for keys and values
     * @return MapCache object
     */
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMapCache(name, codec);
    }

    /**
     * Returns map-based cache instance by <code>name</code>
     * using provided <code>codec</code> for both cache keys and values.
     * Supports entry eviction with a given MaxIdleTime and TTL settings.
     * <p>
     * If eviction is not required then it's better to use regular map {@link #getMap(String, Codec)}.
     *
     * @param <K>     type of key
     * @param <V>     type of value
     * @param name    - object name
     * @param codec   - codec for keys and values
     * @param options - map options
     * @return MapCache object
     */
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec, MapCacheOptions<K, V> options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMapCache(name, codec, options);
    }

    /**
     * Returns map-based cache instance by name.
     * Supports entry eviction with a given MaxIdleTime and TTL settings.
     * <p>
     * If eviction is not required then it's better to use regular map {@link #getMap(String)}.</p>
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name - name of object
     * @return MapCache object
     */
    public <K, V> RMapCache<K, V> getMapCache(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMapCache(name);
    }

    /**
     * Returns map-based cache instance by name.
     * Supports entry eviction with a given MaxIdleTime and TTL settings.
     * <p>
     * If eviction is not required then it's better to use regular map {@link #getMap(String)}.</p>
     *
     * @param <K>     type of key
     * @param <V>     type of value
     * @param name    - name of object
     * @param options - map options
     * @return MapCache object
     */
    public <K, V> RMapCache<K, V> getMapCache(String name, MapCacheOptions<K, V> options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMapCache(name, options);
    }

    /**
     * Returns object holder instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return Bucket object
     */
    public <V> RBucket<V> getBucket(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBucket(name);
    }

    /**
     * Returns object holder instance by name
     * using provided codec for object.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return Bucket object
     */
    public <V> RBucket<V> getBucket(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBucket(name, codec);
    }

    /**
     * Returns interface for mass operations with Bucket objects.
     *
     * @return Buckets
     */
    public RBuckets getBuckets() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBuckets();
    }

    /**
     * Returns interface for mass operations with Bucket objects
     * using provided codec for object.
     *
     * @param codec - codec for bucket objects
     * @return Buckets
     */
    public RBuckets getBuckets(Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBuckets(codec);
    }

    /**
     * Returns JSON data holder instance by name using provided codec.
     *
     * @param <V>   type of value
     * @param name  name of object
     * @param codec codec for values
     * @return JsonBucket object
     */
    public <V> RJsonBucket<V> getJsonBucket(String name, JsonCodec<V> codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getJsonBucket(name, codec);
    }

    /**
     * Returns HyperLogLog instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return HyperLogLog object
     */
    public <V> RHyperLogLog<V> getHyperLogLog(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getHyperLogLog(name);
    }

    /**
     * Returns HyperLogLog instance by name
     * using provided codec for hll objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return HyperLogLog object
     */
    public <V> RHyperLogLog<V> getHyperLogLog(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getHyperLogLog(name, codec);
    }

    /**
     * Returns list instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return List object
     */
    public <V> RList<V> getList(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getList(name);
    }

    /**
     * Returns list instance by name
     * using provided codec for list objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return List object
     */
    public <V> RList<V> getList(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getList(name, codec);
    }

    /**
     * Returns List based Multimap instance by name.
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name - name of object
     * @return ListMultimap object
     */
    public <K, V> RListMultimap<K, V> getListMultimap(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getListMultimap(name);
    }

    /**
     * Returns List based Multimap instance by name
     * using provided codec for both map keys and values.
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for keys and values
     * @return ListMultimap object
     */
    public <K, V> RListMultimap<K, V> getListMultimap(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getListMultimap(name, codec);
    }

    /**
     * Returns List based Multimap instance by name.
     * Supports key-entry eviction with a given TTL value.
     *
     * <p>If eviction is not required then it's better to use regular map {@link #getSetMultimap(String)}.</p>
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name - name of object
     * @return ListMultimapCache object
     */
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getListMultimapCache(name);
    }

    /**
     * Returns List based Multimap instance by name
     * using provided codec for both map keys and values.
     * Supports key-entry eviction with a given TTL value.
     *
     * <p>If eviction is not required then it's better to use regular map {@link #getSetMultimap(String, Codec)}.</p>
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for keys and values
     * @return ListMultimapCache object
     */
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getListMultimapCache(name, codec);
    }

    /**
     * Returns local cached map instance by name.
     * Configured by parameters of options-object.
     *
     * @param <K>     type of key
     * @param <V>     type of value
     * @param name    - name of object
     * @param options - local map options
     * @return LocalCachedMap object
     */
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, LocalCachedMapOptions<K, V> options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getLocalCachedMap(name, options);
    }

    /**
     * Returns local cached map instance by name
     * using provided codec. Configured by parameters of options-object.
     *
     * @param <K>     type of key
     * @param <V>     type of value
     * @param name    - name of object
     * @param codec   - codec for keys and values
     * @param options - local map options
     * @return LocalCachedMap object
     */
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, Codec codec, LocalCachedMapOptions<K, V> options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getLocalCachedMap(name, codec, options);
    }

    /**
     * Returns map instance by name.
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name - name of object
     * @return Map object
     */
    public <K, V> RMap<K, V> getMap(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMap(name);
    }

    /**
     * Returns map instance by name.
     *
     * @param <K>     type of key
     * @param <V>     type of value
     * @param name    - name of object
     * @param options - map options
     * @return Map object
     */
    public <K, V> RMap<K, V> getMap(String name, MapOptions<K, V> options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMap(name, options);
    }

    /**
     * Returns map instance by name
     * using provided codec for both map keys and values.
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for keys and values
     * @return Map object
     */
    public <K, V> RMap<K, V> getMap(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMap(name, codec);
    }

    /**
     * Returns map instance by name
     * using provided codec for both map keys and values.
     *
     * @param <K>     type of key
     * @param <V>     type of value
     * @param name    - name of object
     * @param codec   - codec for keys and values
     * @param options - map options
     * @return Map object
     */
    public <K, V> RMap<K, V> getMap(String name, Codec codec, MapOptions<K, V> options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMap(name, codec, options);
    }

    /**
     * Returns Set based Multimap instance by name.
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name - name of object
     * @return SetMultimap object
     */
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSetMultimap(name);
    }

    /**
     * Returns Set based Multimap instance by name
     * using provided codec for both map keys and values.
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for keys and values
     * @return SetMultimap object
     */
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSetMultimap(name, codec);
    }

    /**
     * Returns Set based Multimap instance by name.
     * Supports key-entry eviction with a given TTL value.
     *
     * <p>If eviction is not required then it's better to use regular map {@link #getSetMultimap(String)}.</p>
     *
     * @param <K>  type of key
     * @param <V>  type of value
     * @param name - name of object
     * @return SetMultimapCache object
     */
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSetMultimapCache(name);
    }

    /**
     * Returns Set based Multimap instance by name
     * using provided codec for both map keys and values.
     * Supports key-entry eviction with a given TTL value.
     *
     * <p>If eviction is not required then it's better to use regular map {@link #getSetMultimap(String, Codec)}.</p>
     *
     * @param <K>   type of key
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for keys and values
     * @return SetMultimapCache object
     */
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSetMultimapCache(name, codec);
    }

    /**
     * Returns semaphore instance by name
     *
     * @param name - name of object
     * @return Semaphore object
     */
    public RSemaphore getSemaphore(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSemaphore(name);
    }

    /**
     * Returns semaphore instance by name.
     * Supports lease time parameter for each acquired permit.
     *
     * @param name - name of object
     * @return PermitExpirableSemaphore object
     */
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPermitExpirableSemaphore(name);
    }

    /**
     * Returns Lock instance by name.
     * <p>
     * Implements a <b>non-fair</b> locking so doesn't guarantees an acquire order by threads.
     * <p>
     * To increase reliability during failover, all operations wait for propagation to all Redis slaves.
     *
     * @param name - name of object
     * @return Lock object
     */
    public RLock getLock(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getLock(name);
    }

    /**
     * Returns Spin lock instance by name.
     * <p>
     * Implements a <b>non-fair</b> locking so doesn't guarantees an acquire order by threads.
     * <p>
     * Lock doesn't use a pub/sub mechanism
     *
     * @param name - name of object
     * @return Lock object
     */
    public RLock getSpinLock(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSpinLock(name);
    }

    /**
     * Returns Spin lock instance by name with specified back off options.
     * <p>
     * Implements a <b>non-fair</b> locking so doesn't guarantees an acquire order by threads.
     * <p>
     * Lock doesn't use a pub/sub mechanism
     *
     * @param name - name of object
     * @return Lock object
     */
    public RLock getSpinLock(String name, LockOptions.BackOff backOff) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSpinLock(name, backOff);
    }

    public RFencedLock getFencedLock(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getFencedLock(name);
    }

    /**
     * Returns MultiLock instance associated with specified <code>locks</code>
     *
     * @param locks - collection of locks
     * @return MultiLock object
     */
    public RLock getMultiLock(RLock... locks) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getMultiLock(locks);
    }

    /*
     * Use getLock() or getFencedLock() method instead.
     */
    @Deprecated
    public RLock getRedLock(RLock... locks) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRedLock(locks);
    }

    /**
     * Returns Lock instance by name.
     * <p>
     * Implements a <b>fair</b> locking so it guarantees an acquire order by threads.
     * <p>
     * To increase reliability during failover, all operations wait for propagation to all Redis slaves.
     *
     * @param name - name of object
     * @return Lock object
     */
    public RLock getFairLock(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getFairLock(name);
    }

    /**
     * Returns ReadWriteLock instance by name.
     * <p>
     * To increase reliability during failover, all operations wait for propagation to all Redis slaves.
     *
     * @param name - name of object
     * @return Lock object
     */
    public RReadWriteLock getReadWriteLock(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getReadWriteLock(name);
    }

    /**
     * Returns set instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return Set object
     */
    public <V> RSet<V> getSet(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSet(name);
    }

    /**
     * Returns set instance by name
     * using provided codec for set objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return Set object
     */
    public <V> RSet<V> getSet(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSet(name, codec);
    }

    /**
     * Returns sorted set instance by name.
     * This sorted set uses comparator to sort objects.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return SortedSet object
     */
    public <V> RSortedSet<V> getSortedSet(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSortedSet(name);
    }

    /**
     * Returns sorted set instance by name
     * using provided codec for sorted set objects.
     * This sorted set sorts objects using comparator.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return SortedSet object
     */
    public <V> RSortedSet<V> getSortedSet(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getSortedSet(name, codec);
    }

    /**
     * Returns Redis Sorted Set instance by name.
     * This sorted set sorts objects by object score.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return ScoredSortedSet object
     */
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getScoredSortedSet(name);
    }

    /**
     * Returns Redis Sorted Set instance by name
     * using provided codec for sorted set objects.
     * This sorted set sorts objects by object score.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return ScoredSortedSet object
     */
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getScoredSortedSet(name, codec);
    }

    /**
     * Returns String based Redis Sorted Set instance by name
     * All elements are inserted with the same score during addition,
     * in order to force lexicographical ordering
     *
     * @param name - name of object
     * @return LexSortedSet object
     */
    public RLexSortedSet getLexSortedSet(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getLexSortedSet(name);
    }

    /**
     * Returns Sharded Topic instance by name.
     * <p>
     * Messages are delivered to message listeners connected to the same Topic.
     * <p>
     *
     * @param name - name of object
     * @return Topic object
     */
    public RShardedTopic getShardedTopic(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getShardedTopic(name);
    }

    /**
     * Returns Sharded Topic instance by name using provided codec for messages.
     * <p>
     * Messages are delivered to message listeners connected to the same Topic.
     * <p>
     *
     * @param name  - name of object
     * @param codec - codec for message
     * @return Topic object
     */
    public RShardedTopic getShardedTopic(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getShardedTopic(name, codec);
    }

    /**
     * Returns topic instance by name.
     * <p>
     * Messages are delivered to message listeners connected to the same Topic.
     * <p>
     *
     * @param name - name of object
     * @return Topic object
     */
    public RTopic getTopic(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getTopic(name);
    }

    /**
     * Returns topic instance by name
     * using provided codec for messages.
     * <p>
     * Messages are delivered to message listeners connected to the same Topic.
     * <p>
     *
     * @param name  - name of object
     * @param codec - codec for message
     * @return Topic object
     */
    public RTopic getTopic(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getTopic(name, codec);
    }

    /**
     * Returns reliable topic instance by name.
     * <p>
     * Dedicated Redis connection is allocated per instance (subscriber) of this object.
     * Messages are delivered to all listeners attached to the same Redis setup.
     * <p>
     * Requires <b>Redis 5.0.0 and higher.</b>
     *
     * @param name - name of object
     * @return ReliableTopic object
     */
    public RReliableTopic getReliableTopic(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getReliableTopic(name);
    }

    /**
     * Returns reliable topic instance by name
     * using provided codec for messages.
     * <p>
     * Dedicated Redis connection is allocated per instance (subscriber) of this object.
     * Messages are delivered to all listeners attached to the same Redis setup.
     * <p>
     * Requires <b>Redis 5.0.0 and higher.</b>
     *
     * @param name  - name of object
     * @param codec - codec for message
     * @return ReliableTopic object
     */
    public RReliableTopic getReliableTopic(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getReliableTopic(name, codec);
    }

    /**
     * Returns topic instance satisfies by pattern name.
     * <p>
     * Supported glob-style patterns:
     * h?llo subscribes to hello, hallo and hxllo
     * h*llo subscribes to hllo and heeeello
     * h[ae]llo subscribes to hello and hallo, but not hillo
     *
     * @param pattern of the topic
     * @return PatterTopic object
     */
    public RPatternTopic getPatternTopic(String pattern) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPatternTopic(pattern);
    }

    /**
     * Returns topic instance satisfies by pattern name
     * using provided codec for messages.
     * <p>
     * Supported glob-style patterns:
     * h?llo subscribes to hello, hallo and hxllo
     * h*llo subscribes to hllo and heeeello
     * h[ae]llo subscribes to hello and hallo, but not hillo
     *
     * @param pattern of the topic
     * @param codec   - codec for message
     * @return PatterTopic object
     */
    public RPatternTopic getPatternTopic(String pattern, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPatternTopic(pattern, codec);
    }

    /**
     * Returns unbounded queue instance by name.
     *
     * @param <V>  type of value
     * @param name of object
     * @return queue object
     */
    public <V> RQueue<V> getQueue(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getQueue(name);
    }

    /**
     * Returns transfer queue instance by name.
     *
     * @param <V>  type of values
     * @param name - name of object
     * @return TransferQueue object
     */
    public <V> RTransferQueue<V> getTransferQueue(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getTransferQueue(name);
    }

    /**
     * Returns transfer queue instance by name
     * using provided codec for queue objects.
     *
     * @param <V>   type of values
     * @param name  - name of object
     * @param codec - code for values
     * @return TransferQueue object
     */
    public <V> RTransferQueue<V> getTransferQueue(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getTransferQueue(name, codec);
    }

    /**
     * Returns unbounded delayed queue instance by name.
     * <p>
     * Could be attached to destination queue only.
     * All elements are inserted with transfer delay to destination queue.
     *
     * @param <V>              type of value
     * @param destinationQueue - destination queue
     * @return Delayed queue object
     */
    public <V> RDelayedQueue<V> getDelayedQueue(RQueue<V> destinationQueue) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getDelayedQueue(destinationQueue);
    }

    /**
     * Returns unbounded queue instance by name
     * using provided codec for queue objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for message
     * @return Queue object
     */
    public <V> RQueue<V> getQueue(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getQueue(name, codec);
    }

    /**
     * Returns RingBuffer based queue.
     *
     * @param <V>  value type
     * @param name - name of object
     * @return RingBuffer object
     */
    public <V> RRingBuffer<V> getRingBuffer(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRingBuffer(name);
    }

    /**
     * Returns RingBuffer based queue.
     *
     * @param <V>   value type
     * @param name  - name of object
     * @param codec - codec for values
     * @return RingBuffer object
     */
    public <V> RRingBuffer<V> getRingBuffer(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRingBuffer(name, codec);
    }

    /**
     * Returns priority unbounded queue instance by name.
     * It uses comparator to sort objects.
     *
     * @param <V>  type of value
     * @param name of object
     * @return Queue object
     */
    public <V> RPriorityQueue<V> getPriorityQueue(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityQueue(name);
    }

    /**
     * Returns priority unbounded queue instance by name
     * using provided codec for queue objects.
     * It uses comparator to sort objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for message
     * @return Queue object
     */
    public <V> RPriorityQueue<V> getPriorityQueue(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityQueue(name, codec);
    }

    /**
     * Returns unbounded priority blocking queue instance by name.
     * It uses comparator to sort objects.
     *
     * @param <V>  type of value
     * @param name of object
     * @return Queue object
     */
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityBlockingQueue(name);
    }

    /**
     * Returns unbounded priority blocking queue instance by name
     * using provided codec for queue objects.
     * It uses comparator to sort objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for message
     * @return Queue object
     */
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityBlockingQueue(name, codec);
    }

    /**
     * Returns unbounded priority blocking deque instance by name.
     * It uses comparator to sort objects.
     *
     * @param <V>  type of value
     * @param name of object
     * @return Queue object
     */
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityBlockingDeque(name);
    }

    /**
     * Returns unbounded priority blocking deque instance by name
     * using provided codec for queue objects.
     * It uses comparator to sort objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for message
     * @return Queue object
     */
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityBlockingDeque(name, codec);
    }

    /**
     * Returns priority unbounded deque instance by name.
     * It uses comparator to sort objects.
     *
     * @param <V>  type of value
     * @param name of object
     * @return Queue object
     */
    public <V> RPriorityDeque<V> getPriorityDeque(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityDeque(name);
    }

    /**
     * Returns priority unbounded deque instance by name
     * using provided codec for queue objects.
     * It uses comparator to sort objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for message
     * @return Queue object
     */
    public <V> RPriorityDeque<V> getPriorityDeque(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getPriorityDeque(name, codec);
    }

    /**
     * Returns unbounded blocking queue instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return BlockingQueue object
     */
    public <V> RBlockingQueue<V> getBlockingQueue(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBlockingQueue(name);
    }

    /**
     * Returns unbounded blocking queue instance by name
     * using provided codec for queue objects.
     *
     * @param <V>   type of value
     * @param name  - name of queue
     * @param codec - queue objects codec
     * @return BlockingQueue object
     */
    public <V> RBlockingQueue<V> getBlockingQueue(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBlockingQueue(name, codec);
    }

    /**
     * Returns bounded blocking queue instance by name.
     *
     * @param <V>  type of value
     * @param name of queue
     * @return BoundedBlockingQueue object
     */
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return getBoundedBlockingQueue(name);
    }

    /**
     * Returns bounded blocking queue instance by name
     * using provided codec for queue objects.
     *
     * @param <V>   type of value
     * @param name  - name of queue
     * @param codec - codec for values
     * @return BoundedBlockingQueue object
     */
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBoundedBlockingQueue(name, codec);
    }

    /**
     * Returns unbounded deque instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return Deque object
     */
    public <V> RDeque<V> getDeque(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return getDeque(name);
    }

    /**
     * Returns unbounded deque instance by name
     * using provided codec for deque objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return Deque object
     */
    public <V> RDeque<V> getDeque(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getDeque(name, codec);
    }

    /**
     * Returns unbounded blocking deque instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return BlockingDeque object
     */
    public <V> RBlockingDeque<V> getBlockingDeque(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBlockingDeque(name);
    }

    /**
     * Returns unbounded blocking deque instance by name
     * using provided codec for deque objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - deque objects codec
     * @return BlockingDeque object
     */
    public <V> RBlockingDeque<V> getBlockingDeque(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBlockingDeque(name, codec);
    }

    /**
     * Returns atomicLong instance by name.
     *
     * @param name - name of object
     * @return AtomicLong object
     */
    public RAtomicLong getAtomicLong(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getAtomicLong(name);
    }

    /**
     * Returns atomicDouble instance by name.
     *
     * @param name - name of object
     * @return AtomicDouble object
     */
    public RAtomicDouble getAtomicDouble(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getAtomicDouble(name);
    }

    /**
     * Returns LongAdder instances by name.
     *
     * @param name - name of object
     * @return LongAdder object
     */
    public RLongAdder getLongAdder(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getLongAdder(name);
    }

    /**
     * Returns DoubleAdder instances by name.
     *
     * @param name - name of object
     * @return LongAdder object
     */
    public RDoubleAdder getDoubleAdder(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getDoubleAdder(name);
    }

    /**
     * Returns countDownLatch instance by name.
     *
     * @param name - name of object
     * @return CountDownLatch object
     */
    public RCountDownLatch getCountDownLatch(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getCountDownLatch(name);
    }

    /**
     * Returns bitSet instance by name.
     *
     * @param name - name of object
     * @return BitSet object
     */
    public RBitSet getBitSet(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBitSet(name);
    }

    /**
     * Returns bloom filter instance by name.
     *
     * @param <V>  type of value
     * @param name - name of object
     * @return BloomFilter object
     */
    public <V> RBloomFilter<V> getBloomFilter(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBloomFilter(name);
    }

    /**
     * Returns bloom filter instance by name
     * using provided codec for objects.
     *
     * @param <V>   type of value
     * @param name  - name of object
     * @param codec - codec for values
     * @return BloomFilter object
     */
    public <V> RBloomFilter<V> getBloomFilter(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getBloomFilter(name, codec);
    }

    /**
     * Returns id generator by name.
     *
     * @param name - name of object
     * @return IdGenerator object
     */
    public RIdGenerator getIdGenerator(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getIdGenerator(name);
    }

    /**
     * Returns interface for Redis Function feature
     *
     * @return function object
     */
    public RFunction getFunction() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getFunction();
    }

    /**
     * Returns interface for Redis Function feature using provided codec
     *
     * @param codec - codec for params and result
     * @return function interface
     */
    public RFunction getFunction(Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getFunction(codec);
    }

    /**
     * Returns script operations object
     *
     * @return Script object
     */
    public RScript getScript() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getScript();
    }

    /**
     * Returns script operations object using provided codec.
     *
     * @param codec - codec for params and result
     * @return Script object
     */
    public RScript getScript(Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getScript(codec);
    }

    /**
     * Returns ScheduledExecutorService by name
     *
     * @param name - name of object
     * @return ScheduledExecutorService object
     */
    public RScheduledExecutorService getExecutorService(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getExecutorService(name);
    }

    /**
     * Returns ScheduledExecutorService by name
     *
     * @param name    - name of object
     * @param options - options for executor
     * @return ScheduledExecutorService object
     */
    public RScheduledExecutorService getExecutorService(String name, ExecutorOptions options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getExecutorService(name, options);
    }

    /**
     * Returns ScheduledExecutorService by name
     * using provided codec for task, response and request serialization
     *
     * @param name  - name of object
     * @param codec - codec for task, response and request
     * @return ScheduledExecutorService object
     * @since 2.8.2
     */
    public RScheduledExecutorService getExecutorService(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getExecutorService(name, codec);
    }

    /**
     * Returns ScheduledExecutorService by name
     * using provided codec for task, response and request serialization
     *
     * @param name    - name of object
     * @param codec   - codec for task, response and request
     * @param options - options for executor
     * @return ScheduledExecutorService object
     */
    public RScheduledExecutorService getExecutorService(String name, Codec codec, ExecutorOptions options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getExecutorService(name, codec, options);
    }

    /**
     * Returns object for remote operations prefixed with the public name (redisson_remote_service)
     *
     * @return RemoteService object
     */
    public RRemoteService getRemoteService() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRemoteService();
    }

    /**
     * Returns object for remote operations prefixed with the public name (redisson_remote_service)
     * and uses provided codec for method arguments and result.
     *
     * @param codec - codec for response and request
     * @return RemoteService object
     */
    public RRemoteService getRemoteService(Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRemoteService(codec);
    }

    /**
     * Returns object for remote operations prefixed with the specified name
     *
     * @param name - the name used as the Redis key prefix for the services
     * @return RemoteService object
     */
    public RRemoteService getRemoteService(String name) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRemoteService(name);
    }

    /**
     * Returns object for remote operations prefixed with the specified name
     * and uses provided codec for method arguments and result.
     *
     * @param name  - the name used as the Redis key prefix for the services
     * @param codec - codec for response and request
     * @return RemoteService object
     */
    public RRemoteService getRemoteService(String name, Codec codec) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRemoteService(name, codec);
    }

    /**
     * Creates transaction with <b>READ_COMMITTED</b> isolation level.
     *
     * @param options - transaction configuration
     * @return Transaction object
     */
    public RTransaction createTransaction(TransactionOptions options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.createTransaction(options);
    }

    /**
     * Creates batch object which could be executed later
     * with collected group of commands in pipeline mode.
     * <p>
     * See <a href="http://redis.io/topics/pipelining">http://redis.io/topics/pipelining</a>
     *
     * @param options - batch configuration
     * @return Batch object
     */
    public RBatch createBatch(BatchOptions options) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.createBatch(options);
    }

    /**
     * Creates batch object which could be executed later
     * with collected group of commands in pipeline mode.
     * <p>
     * See <a href="http://redis.io/topics/pipelining">http://redis.io/topics/pipelining</a>
     *
     * @return Batch object
     */
    public RBatch createBatch() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.createBatch();
    }

    /**
     * Returns interface with methods for Redis keys.
     * Each of Redis/Redisson object associated with own key
     *
     * @return Keys object
     */
    public RKeys getKeys() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getKeys();
    }

    /**
     * Returns RedissonAttachedLiveObjectService which can be used to
     * retrieve live REntity(s)
     *
     * @return LiveObjectService object
     */
    public RLiveObjectService getLiveObjectService() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getLiveObjectService();
    }

    /**
     * Returns RxJava Redisson instance
     *
     * @return redisson instance
     */
    public RedissonRxClient rxJava() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.rxJava();
    }

    /**
     * Returns Reactive Redisson instance
     *
     * @return redisson instance
     */
    public RedissonReactiveClient reactive() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.reactive();
    }

    /**
     * Shutdown Redisson instance but <b>NOT</b> Redis server
     * <p>
     * This equates to invoke shutdown(0, 2, TimeUnit.SECONDS);
     */
    public void shutdown() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        redissonClient.shutdown();
    }

    /**
     * Shuts down Redisson instance but <b>NOT</b> Redis server
     * <p>
     * Shutdown ensures that no tasks are submitted for <i>'the quiet period'</i>
     * (usually a couple seconds) before it shuts itself down.  If a task is submitted during the quiet period,
     * it is guaranteed to be accepted and the quiet period will start over.
     *
     * @param quietPeriod the quiet period as described in the documentation
     * @param timeout     the maximum amount of time to wait until the executor is {@linkplain #shutdown()}
     *                    regardless if a task was submitted during the quiet period
     * @param unit        the unit of {@code quietPeriod} and {@code timeout}
     */
    public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        redissonClient.shutdown(quietPeriod, timeout, unit);
    }

    /**
     * Allows to get configuration provided
     * during Redisson instance creation. Further changes on
     * this object not affect Redisson instance.
     *
     * @return Config object
     */
    public Config getConfig() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getConfig();
    }

    /**
     * Returns API to manage Redis nodes
     *
     * @param nodes Redis nodes API class
     * @param <T>   type of Redis nodes API
     * @return Redis nodes API object
     * @see RedisNodes#CLUSTER
     * @see RedisNodes#MASTER_SLAVE
     * @see RedisNodes#SENTINEL_MASTER_SLAVE
     * @see RedisNodes#SINGLE
     */
    public <T extends BaseRedisNodes> T getRedisNodes(RedisNodes<T> nodes) {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getRedisNodes(nodes);
    }

    /*
     * Use getRedisNodes() method instead
     */
    @Deprecated
    public NodesGroup<Node> getNodesGroup() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getNodesGroup();
    }

    /*
     * Use getRedisNodes() method instead
     */
    @Deprecated
    public ClusterNodesGroup getClusterNodesGroup() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getClusterNodesGroup();
    }

    /**
     * Returns {@code true} if this Redisson instance has been shut down.
     *
     * @return {@code true} if this Redisson instance has been shut down overwise <code>false</code>
     */
    public boolean isShutdown() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.isShutdown();
    }

    /**
     * Returns {@code true} if this Redisson instance was started to be shutdown
     * or was shutdown {@link #isShutdown()} already.
     *
     * @return {@code true} if this Redisson instance was started to be shutdown
     * or was shutdown {@link #isShutdown()} already.
     */
    public boolean isShuttingDown() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.isShuttingDown();
    }

    /**
     * Returns id of this Redisson instance
     *
     * @return id
     */
    public String getId() {
        RedissonClient redissonClient = this.multiRedisson.getRedissonClient();
        return redissonClient.getId();
    }

}
