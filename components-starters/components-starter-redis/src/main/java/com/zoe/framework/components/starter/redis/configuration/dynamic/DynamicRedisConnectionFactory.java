package com.zoe.framework.components.starter.redis.configuration.dynamic;

import cn.hutool.core.util.ObjectUtil;
import com.zoe.framework.components.starter.redis.configuration.dynamic.builder.JedisConnectionConfiguration;
import com.zoe.framework.components.starter.redis.configuration.dynamic.builder.LettuceConnectionConfiguration;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.ClientResourcesBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2023-12-07 16:41:52
 */
@Slf4j
public class DynamicRedisConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory, ReactiveRedisConnectionFactory {

    private ConcurrentHashMap<String, RedisConnectionFactory> multiRedisConnectionFactory = new ConcurrentHashMap<>();

    private final DynamicRedisProperties dynamicRedisProperties;
    private final ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider;
    private final ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration;
    private final ObjectProvider<RedisClusterConfiguration> clusterConfiguration;
    private final ObjectProvider<JedisClientConfigurationBuilderCustomizer> jedisBuilderCustomizers;
    private final ObjectProvider<LettuceClientConfigurationBuilderCustomizer> lettuceBuilderCustomizers;
    private final ObjectProvider<ClientResourcesBuilderCustomizer> lettuceClientResourcesCustomizers;

    public DynamicRedisConnectionFactory(DynamicRedisProperties dynamicRedisProperties
            , ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider
            , ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration
            , ObjectProvider<RedisClusterConfiguration> clusterConfiguration
            , ObjectProvider<JedisClientConfigurationBuilderCustomizer> jedisBuilderCustomizers
            , ObjectProvider<LettuceClientConfigurationBuilderCustomizer> lettuceBuilderCustomizers
            , ObjectProvider<ClientResourcesBuilderCustomizer> lettuceClientResourcesCustomizers) {
        this.dynamicRedisProperties = dynamicRedisProperties;
        this.standaloneConfigurationProvider = standaloneConfigurationProvider;
        this.sentinelConfiguration = sentinelConfiguration;
        this.clusterConfiguration = clusterConfiguration;
        this.jedisBuilderCustomizers = jedisBuilderCustomizers;
        this.lettuceBuilderCustomizers = lettuceBuilderCustomizers;
        this.lettuceClientResourcesCustomizers = lettuceClientResourcesCustomizers;
        if (CollectionUtil.isEmpty(dynamicRedisProperties.getDataSources())) {
            throw new RuntimeException("未配置redis多数据源");
        }
        if (StrUtil.isBlank(dynamicRedisProperties.getPrimary())) {
            throw new RuntimeException("未配置redis默认数据源");
        }
    }

    @Override
    public void destroy() {
        multiRedisConnectionFactory.forEach((key, ds) -> {
            if (ObjectUtil.isNull(ds)) {
                return;
            }
            if (!(ds instanceof DisposableBean)) {
                return;
            }
            log.info("关闭Redis数据源：{}", key);
            try {
                ((DisposableBean) ds).destroy();
            } catch (Exception e) {
                log.error("关闭Redis数据源失败：{}", key, e);
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化
        dynamicRedisProperties.getDataSources().forEach((key, redisProperties) -> {
            RedisProperties.ClientType type = null;
            // 优先判断client-type, 低版本没有此属性
            if (ObjectUtil.isNotNull(redisProperties.getClientType())) {
                type = redisProperties.getClientType();
            } else {
                // 自动选择，依次判断是否有luttuce/jedis
                Class<?> clazz = null;
                try {
                    clazz = Class.forName("io.lettuce.core.RedisClient");
                } catch (Exception e) {
                }
                if (clazz != null) {
                    type = RedisProperties.ClientType.LETTUCE;
                } else {
                    try {
                        clazz = Class.forName("redis.clients.jedis.Jedis");
                    } catch (Exception e) {
                    }
                    if (clazz != null) {
                        type = RedisProperties.ClientType.JEDIS;
                    }
                }
            }
            // 创建
            if (RedisProperties.ClientType.LETTUCE.equals(type)) {
                log.info("初始化lettuce数据源：{}", key);
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionConfiguration(redisProperties, standaloneConfigurationProvider, sentinelConfiguration, clusterConfiguration)
                        .build(this.lettuceClientResourcesCustomizers, this.lettuceBuilderCustomizers);
                // 初始化
                lettuceConnectionFactory.afterPropertiesSet();
                multiRedisConnectionFactory.put(key, lettuceConnectionFactory);
            } else if (RedisProperties.ClientType.JEDIS.equals(type)) {
                log.info("初始化jedis数据源：{}", key);
                JedisConnectionFactory jedisConnectionFactory = new JedisConnectionConfiguration(redisProperties, standaloneConfigurationProvider, sentinelConfiguration, clusterConfiguration)
                        .build(this.jedisBuilderCustomizers);
                // 初始化
                jedisConnectionFactory.afterPropertiesSet();
                multiRedisConnectionFactory.put(key, jedisConnectionFactory);
            } else {
                throw new RuntimeException("未找到redis客户端，请检查是否引入了lettuce或jedis");
            }
        });
        if (!multiRedisConnectionFactory.containsKey(this.dynamicRedisProperties.getPrimary())) {
            throw new RuntimeException("未找到redis默认数据源，请检查是否配置了redis默认数据源");
        }
    }

    private RedisConnectionFactory getConnectionFactoryFromCurrentKey() {
        String connectionFactoryKey = DynamicConnectFactoryContextHolder.peek();
        if (StrUtil.isBlank(connectionFactoryKey)) {
            // 当前方法未指定注解时，使用默认数据源
            return this.multiRedisConnectionFactory.get(dynamicRedisProperties.getPrimary());
        }
        RedisConnectionFactory redisConnectionFactory = this.multiRedisConnectionFactory.get(connectionFactoryKey);
        if (ObjectUtil.isNotNull(redisConnectionFactory)) {
            return redisConnectionFactory;
        }
        if (dynamicRedisProperties.isExceptionWhileNotFound()) {
            throw new RuntimeException("未找到redis数据源，请检查是否配置了redis数据源");
        }
        // 使用默认数据源
        return this.multiRedisConnectionFactory.get(dynamicRedisProperties.getPrimary());
    }

    @Override
    public ReactiveRedisConnection getReactiveConnection() {
        RedisConnectionFactory pointConnectFactory = this.getConnectionFactoryFromCurrentKey();
        if (pointConnectFactory instanceof ReactiveRedisConnectionFactory) {
            return ((ReactiveRedisConnectionFactory) pointConnectFactory).getReactiveConnection();
        }
        throw new RuntimeException("当前数据源不支持ReactiveRedisConnection");
    }

    @Override
    public ReactiveRedisClusterConnection getReactiveClusterConnection() {
        RedisConnectionFactory pointConnectFactory = this.getConnectionFactoryFromCurrentKey();
        if (pointConnectFactory instanceof ReactiveRedisConnectionFactory) {
            return ((ReactiveRedisConnectionFactory) pointConnectFactory).getReactiveClusterConnection();
        }
        throw new RuntimeException("当前数据源不支持ReactiveRedisClusterConnection");
    }

    @Override
    public RedisConnection getConnection() {
        return this.getConnectionFactoryFromCurrentKey().getConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return this.getConnectionFactoryFromCurrentKey().getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return this.getConnectionFactoryFromCurrentKey().getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return this.getConnectionFactoryFromCurrentKey().getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        return this.getConnectionFactoryFromCurrentKey().translateExceptionIfPossible(e);
    }

}
