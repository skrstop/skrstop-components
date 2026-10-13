package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class PropertyValuesUtilTest {

    @Test
    public void testGetSubPropertyValues() {
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "n");
        map.put("user.age", "1");
        map.put("other", "x");
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("test", map));
        MutablePropertyValues values = (MutablePropertyValues) PropertyValuesUtil.getSubPropertyValues(environment, "user");
        Assert.assertTrue(values.size() >= 2);
        // 环境含系统属性，仅校验自有键
        PropertyValue pv = values.getPropertyValue("name");
        Assert.assertNotNull(pv);
        Assert.assertEquals("n", pv.getValue());
        Assert.assertNotNull(values.getPropertyValue("age"));
    }

}