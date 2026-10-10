package com.skrstop.framework.components.starter.redis.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * redisson 4.x 配置兼容工具
 * <p>redisson 4 起移除了 Config.fromJSON，且 YAML 不再支持 codec.class 嵌套写法，
 * 此处将 JSON 配置及嵌套 codec 提示归一化后解析，保持既有配置能力
 *
 * @author 蒋时华
 * @since 3.5.1.20261009
 */
public class RedissonConfigCompat {

    private RedissonConfigCompat() {
    }

    public static Config fromJson(String json) {
        JSONObject jsonObject = JSONUtil.parseObj(json);
        return fromMap(jsonObject);
    }

    public static Config fromYaml(String yamlStr) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(yamlStr);
        return fromMap(map);
    }

    private static Config fromMap(Map<String, Object> map) {
        String codecClass = null;
        Object codec = map.get("codec");
        if (codec instanceof Map) {
            Object clazz = ((Map<?, ?>) codec).get("class");
            if (clazz instanceof String) {
                codecClass = (String) clazz;
                map.remove("codec");
            }
        }
        Yaml yaml = new Yaml();
        Config config = Config.fromYAML(yaml.dump(map));
        if (codecClass != null) {
            try {
                config.setCodec((Codec) Class.forName(codecClass).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new IllegalArgumentException("redisson codec 类实例化失败: " + codecClass, e);
            }
        }
        return config;
    }
}
