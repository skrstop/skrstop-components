package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class PropertySourcesUtilTest {

    private MutablePropertySources onlySources() {
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "user-name-value");
        map.put("user.age", "31");
        map.put("user.ageReal", "1");
        map.put("userData.name", "userData-name-value");
        map.put("user.home", "${user.age}");
        map.put("abstractEnvironment.key", "value");
        map.put("message", "message");
        MutablePropertySources sources = new MutablePropertySources();
        sources.addFirst(new MapPropertySource("test", map));
        return sources;
    }

    private ConfigurableEnvironment environment() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(onlySources().get("test"));
        return environment;
    }

    @Test
    public void testNormalizePrefix() {
        Assert.assertEquals("user.", PropertySourcesUtil.normalizePrefix("user"));
        Assert.assertEquals("user.", PropertySourcesUtil.normalizePrefix("user."));
    }

    @Test
    public void testGetSubProperties() {
        Map<String, Object> subProperties =
                PropertySourcesUtil.getSubProperties(onlySources(), "user");
        Assert.assertEquals(4, subProperties.size());
        Assert.assertEquals("user-name-value", subProperties.get("name"));
        // placeholder 已解析
        Assert.assertEquals("31", subProperties.get("home"));
    }

    @Test
    public void testGetSubPropertiesEnv() {
        Map<String, Object> subProperties =
                PropertySourcesUtil.getSubProperties(onlySources(), "user");
        Assert.assertEquals(4, subProperties.size());
        ConfigurableEnvironment environment = environment();
        Map<String, Object> result =
                PropertySourcesUtil.getSubProperties(environment.getPropertySources(), environment, "user");
        // 环境含系统属性，只比对自有键
        for (String key : subProperties.keySet()) {
            Assert.assertEquals(subProperties.get(key), result.get(key));
        }
        // Iterable 版本
        Map<String, Object> result2 =
                PropertySourcesUtil.getSubProperties(environment.getPropertySources(), "user");
        for (String key : subProperties.keySet()) {
            Assert.assertEquals(subProperties.get(key), result2.get(key));
        }
    }

    @Test
    public void testGetSubPropertiesPlaceholderResolved() {
        ConfigurableEnvironment environment = environment();
        Map<String, Object> result =
                PropertySourcesUtil.getSubProperties(environment.getPropertySources(), environment, "user");
        Assert.assertEquals("31", result.get("home"));
        // String[] 数组属性
        Map<String, Object> map = new HashMap<>();
        map.put("a.list", "x,y");
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("arr", map));
        Map<String, Object> result3 = PropertySourcesUtil.getSubProperties(env.getPropertySources(), env, "a");
        Assert.assertEquals("x,y", result3.get("list"));
    }

    @Test
    public void testGetSubPropertiesIterableOverload() {
        // Iterable 版本（36-38 行）
        Map<String, Object> result = PropertySourcesUtil.getSubProperties(onlySources(), "user");
        Assert.assertEquals(4, result.size());
        // environment 2 参版本（53 行）
        Map<String, Object> envResult = PropertySourcesUtil.getSubProperties(environment(), "user");
        Assert.assertEquals("user-name-value", envResult.get("name"));
    }

    @Test
    public void testGetSubPropertiesNonStringValue() {
        // 非 String 值（109 行 false 分支）+ 前缀不匹配（104 行 false 分支）
        Map<String, Object> map = new HashMap<>();
        map.put("n.count", 5);
        map.put("other", "x");
        MutablePropertySources sources = new MutablePropertySources();
        sources.addFirst(new MapPropertySource("t", map));
        Map<String, Object> result = PropertySourcesUtil.getSubProperties(sources, "n");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(5, result.get("count"));
    }

    @Test
    public void testGetSubPropertiesFullNameCollision() {
        // 已收集的子属性名与后续 source 的完整属性名相同 → containsKey(name) true 跳过
        Map<String, Object> first = new HashMap<>();
        first.put("a.b", "1");
        Map<String, Object> second = new HashMap<>();
        second.put("b", "2");
        MutablePropertySources sources = new MutablePropertySources();
        sources.addFirst(new MapPropertySource("first", first));
        sources.addLast(new MapPropertySource("second", second));
        Map<String, Object> result = PropertySourcesUtil.getSubProperties(sources, "a");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("1", result.get("b"));
    }

    @Test
    public void testGetSubPropertiesDuplicateName() {
        // 同一 source 中 name 已存在（104 行 !containsKey false 分支）：
        // 两个 source 有相同的属性名 → 第二个被跳过
        Map<String, Object> first = new HashMap<>();
        first.put("d.k", "1");
        Map<String, Object> second = new HashMap<>();
        second.put("d.k", "2");
        MutablePropertySources sources = new MutablePropertySources();
        sources.addFirst(new MapPropertySource("first", first));
        sources.addLast(new MapPropertySource("second", second));
        // 通过 Iterable 重载验证 addLast 循环
        Map<String, Object> result =
                PropertySourcesUtil.getSubProperties(java.util.Arrays.asList(
                        new MapPropertySource("first", first),
                        new MapPropertySource("second", second)), "d");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("1", result.get("k"));
    }

    @Test
    public void testGetSubPropertiesPriority() {
        // 同名属性取先出现的 source
        Map<String, Object> first = new HashMap<>();
        first.put("p.key", "first");
        Map<String, Object> second = new HashMap<>();
        second.put("p.key", "second");
        MutablePropertySources sources = new MutablePropertySources();
        sources.addFirst(new MapPropertySource("first", first));
        sources.addLast(new MapPropertySource("second", second));
        Assert.assertEquals("first", PropertySourcesUtil.getSubProperties(sources, "p").get("key"));
    }

    @Test
    public void testGetSubPropertiesNonEnumerableIgnored() {
        // 非 EnumerablePropertySource 被忽略
        MutablePropertySources sources = new MutablePropertySources();
        sources.addFirst(new org.springframework.core.env.PropertySource<Object>("stub", new Object()) {
            @Override
            public Object getProperty(String name) {
                return null;
            }
        });
        Assert.assertEquals(0, PropertySourcesUtil.getSubProperties(sources, "x").size());
        Assert.assertTrue(PropertySourcesUtil.getSubProperties(Arrays.asList(), "x").isEmpty());
    }

}