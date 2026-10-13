package com.skrstop.framework.components.starter.objectStorage.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class DSObjectStorageTest {

    public static class Holder {
        @DSObjectStorage
        public void def() {
        }

        @DSObjectStorage("ds1")
        public void named() {
        }

        public void plain() {
        }
    }

    @Test
    public void defaults() throws Exception {
        Method def = Holder.class.getMethod("def");
        DSObjectStorage anno = def.getAnnotation(DSObjectStorage.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("default", anno.value());

        Method named = Holder.class.getMethod("named");
        Assert.assertEquals("ds1", named.getAnnotation(DSObjectStorage.class).value());

        Method plain = Holder.class.getMethod("plain");
        Assert.assertNull(plain.getAnnotation(DSObjectStorage.class));
    }
}
