package cn.auntec.framework.components.starter.redis.autoconfig;

import cn.auntec.framework.components.starter.redis.config.FastJsonRedisTemplate;
import cn.auntec.framework.components.starter.redis.config.GlobalRedisConfig;
import cn.auntec.framework.components.starter.redis.constant.GlobalConfigConst;
import cn.auntec.framework.components.starter.redis.constant.ValueProcessorConst;
import cn.auntec.framework.components.starter.redis.filter.BinaryValueFilter;
import cn.auntec.framework.components.starter.redis.filter.FastjsonValueFilter;
import cn.auntec.framework.components.starter.redis.filter.StringValueFilter;
import cn.auntec.framework.components.starter.redis.filter.ValueFilter;
import cn.auntec.framework.components.starter.redis.service.RedisService;
import cn.auntec.framework.components.starter.redis.service.impl.RedisServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Created by 蒋时华 on 2017/9/21.
 */
@Configuration
@EnableCaching
@ConditionalOnClass({RedisConnectionFactory.class, RedisTemplate.class})
@ConditionalOnBean(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({GlobalRedisConfig.class})
@ConditionalOnProperty(name = GlobalConfigConst.REDIS_PREFIX + ".enabled", havingValue = "true", matchIfMissing = true)
public class CustomRedisAutoConfiguration extends CachingConfigurerSupport {

    /**
     * 生成key的策略
     *
     * @return
     */
    @Bean
    public KeyGenerator KeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 管理缓存
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }


    @Bean("fastJsonRedisService")
    @ConditionalOnBean(RedisTemplate.class)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public RedisService redisService(RedisConnectionFactory connectionFactory
            , GlobalRedisConfig globalRedisConfig) {
        RedisTemplate redisServiceTemplate;
        ValueFilter valueFilter;
        switch (globalRedisConfig.getValueProcessor()) {
            case ValueProcessorConst.BINARY:
                redisServiceTemplate = new RedisTemplate<String, Object>();
                redisServiceTemplate.setConnectionFactory(connectionFactory);
                redisServiceTemplate.afterPropertiesSet();
                valueFilter = new BinaryValueFilter();
                break;
            case ValueProcessorConst.STRING:
                redisServiceTemplate = new StringRedisTemplate(connectionFactory);
                valueFilter = new StringValueFilter();
                break;
            case ValueProcessorConst.FAST_JSON:
            default:
                redisServiceTemplate = new FastJsonRedisTemplate(connectionFactory
                        , globalRedisConfig.getFastjsonPrettyFormatJson()
                        , globalRedisConfig.getFastjsonSafeMode()
                        , globalRedisConfig.getFastjsonAutoType());
                // autoType： !globalRedisConfig.getFastjsonSafeMode() && globalRedisConfig.getFastjsonAutoType()
                valueFilter = new FastjsonValueFilter(
                        globalRedisConfig.getFastjsonFilterEach()
                        , !globalRedisConfig.getFastjsonSafeMode() && globalRedisConfig.getFastjsonAutoType()
                );
                break;
        }
        return new RedisServiceImpl(redisServiceTemplate, valueFilter);
    }

}
