package com.skrstop.framework.components.starter.redis.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Documented;
import java.lang.reflect.Method;

public class AnnotationsTest {

    @DSRedis("r1")
    @DSRedisson("#spel")
    static class Host {
        @DSRedis
        @DSRedisson
        public void m() {
        }
    }

    @Test
    public void dsRedis() throws Exception {
        Assert.assertEquals("r1", Host.class.getAnnotation(DSRedis.class).value());
        Method m = Host.class.getMethod("m");
        Assert.assertEquals("default", m.getAnnotation(DSRedis.class).value());
        Assert.assertNotNull(DSRedis.class.getAnnotation(Documented.class));
        Assert.assertNotNull(DSRedisson.class.getAnnotation(Documented.class));
        Assert.assertEquals("#spel", Host.class.getAnnotation(DSRedisson.class).value());
        Assert.assertEquals("default", m.getAnnotation(DSRedisson.class).value());
    }
}
