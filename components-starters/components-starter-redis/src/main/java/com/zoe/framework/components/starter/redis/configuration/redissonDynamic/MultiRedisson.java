package com.zoe.framework.components.starter.redis.configuration.redissonDynamic;

import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2023-12-15 17:17:21
 */
@Slf4j
public class MultiRedisson implements InitializingBean, DisposableBean {

    private final ConcurrentHashMap<String, RedissonClient> multiRedissonClient = new ConcurrentHashMap<>();
    private final DynamicRedissonProperties dynamicRedissonProperties;
    private final ApplicationContext ctx;

    public MultiRedisson(DynamicRedissonProperties dynamicRedissonProperties, ApplicationContext ctx) {
        this.dynamicRedissonProperties = dynamicRedissonProperties;
        this.ctx = ctx;
        if (CollectionUtil.isEmpty(dynamicRedissonProperties.getDataSources())) {
            throw new RuntimeException("未配置redisson多数据源");
        }
        if (StrUtil.isBlank(dynamicRedissonProperties.getPrimary())) {
            throw new RuntimeException("未配置redisson默认数据源");
        }
        if (!dynamicRedissonProperties.getDataSources().containsKey(dynamicRedissonProperties.getPrimary())) {
            throw new RuntimeException("redisson默认数据源未配置");
        }
    }

    public RedissonClient getRedissonClient() {
        String dsKey = DynamicRedissonContextHolder.peek();
        if (StrUtil.isBlank(dsKey)) {
            // 当前方法未指定注解时，使用默认数据源
            return this.multiRedissonClient.get(dynamicRedissonProperties.getPrimary());
        }
        RedissonClient redissonClient = this.multiRedissonClient.get(dsKey);
        if (ObjectUtil.isNotNull(redissonClient)) {
            return redissonClient;
        }
        if (dynamicRedissonProperties.isExceptionWhileNotFound()) {
            throw new RuntimeException("未找到redisson数据源，请检查是否配置了redis数据源: " + dsKey);
        }
        // 使用默认数据源
        return this.multiRedissonClient.get(dynamicRedissonProperties.getPrimary());
    }

    @Override
    public void destroy() throws Exception {
        multiRedissonClient.forEach((key, ds) -> {
            if (ObjectUtil.isNull(ds) || ds.isShutdown() || ds.isShuttingDown()) {
                return;
            }
            log.info("关闭redisson数据源：{}", key);
            try {
                ds.shutdown();
            } catch (Exception e) {
                log.error("关闭redisson数据源失败：{}", key, e);
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Map.Entry<String, DynamicRedissonProperties.Config> entry : dynamicRedissonProperties.getDataSources().entrySet()) {
            String key = entry.getKey();
            DynamicRedissonProperties.Config ds = entry.getValue();
            log.info("初始化redisson数据源：{}", key);
            Config config = null;
            if (StrUtil.isNotBlank(ds.getConfigFile())) {
                Resource resource = ctx.getResource(ds.getConfigFile());
                InputStream is = resource.getInputStream();
                config = Config.fromYAML(is);
            } else if (StrUtil.isNotBlank(ds.getConfigYamlStr())) {
                config = Config.fromYAML(ds.getConfigYamlStr());
            } else if (StrUtil.isNotBlank(ds.getConfigJsonStr())) {
                config = Config.fromJSON(ds.getConfigJsonStr());
            }
            if (ObjectUtil.isNull(config)) {
                throw new ZoeRuntimeException("redisson未配置相关参数: " + key);
            }
            multiRedissonClient.put(key, Redisson.create(config));
        }
    }
}
