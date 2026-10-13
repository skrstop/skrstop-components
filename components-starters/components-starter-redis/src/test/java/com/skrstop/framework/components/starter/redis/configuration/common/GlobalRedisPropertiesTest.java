package com.skrstop.framework.components.starter.redis.configuration.common;

import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.redis.constant.ValueProcessorConst;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class GlobalRedisPropertiesTest {

    @Test
    public void processor() {
        GlobalRedisProperties p = new GlobalRedisProperties();
        p.setValueProcessor(ValueProcessorConst.STRING);
        Assert.assertEquals((Object) ValueProcessorConst.STRING, (Object) p.getValueProcessor());
        p.setValueProcessor(ValueProcessorConst.BINARY);
        Assert.assertEquals((Object) ValueProcessorConst.BINARY, (Object) p.getValueProcessor());
        p.setValueProcessor(ValueProcessorConst.FAST_JSON);
        Assert.assertEquals((Object) ValueProcessorConst.FAST_JSON, (Object) p.getValueProcessor());
        try {
            p.setValueProcessor((byte) 99);
            Assert.fail();
        } catch (NotSupportedException e) {
            Assert.assertTrue(e.getMessage().contains("99"));
        }
        // 默认值
        Assert.assertEquals((Object) ValueProcessorConst.FAST_JSON, (Object) p.getValueProcessor());
        Assert.assertTrue(p.isEnabled());
        Assert.assertFalse(p.isFastjsonPrettyFormatJson());
        Assert.assertTrue(p.isFastjsonSafeMode());
        Assert.assertFalse(p.isFastjsonAutoType());
        Assert.assertFalse(p.isFastjsonFilterEach());
        Assert.assertNotNull(p.getDatetimeFormat());
        Assert.assertNotNull(p.getDateFormat());
        Assert.assertNotNull(p.getTimeFormat());
    }

    @Test
    public void invalidDirectValue() throws Exception {
        GlobalRedisProperties p = new GlobalRedisProperties();
        Field f = GlobalRedisProperties.class.getDeclaredField("valueProcessor");
        f.setAccessible(true);
        f.set(p, Byte.valueOf((byte) 9));
        Assert.assertEquals(Byte.valueOf((byte) 9), (Object) p.getValueProcessor());
    }
}
