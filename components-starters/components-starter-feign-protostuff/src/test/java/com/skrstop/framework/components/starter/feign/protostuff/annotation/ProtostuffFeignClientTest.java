package com.skrstop.framework.components.starter.feign.protostuff.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

public class ProtostuffFeignClientTest {

    @ProtostuffFeignClient(value = "svc", path = "/p", local = true, autoLocal = true, autoLocalBasePackage = "bp")
    interface Client {
    }

    @Test
    public void defaultsAndAliases() {
        // 默认值
        ProtostuffFeignClient def = Defaults.class.getAnnotation(ProtostuffFeignClient.class);
        Assert.assertNotNull(def);
        Assert.assertEquals("", def.value());
        Assert.assertEquals("", def.contextId());
        Assert.assertEquals("", def.name());
        Assert.assertEquals(0, def.qualifiers().length);
        Assert.assertEquals("", def.url());
        Assert.assertFalse(def.dismiss404());
        Assert.assertEquals(0, def.configuration().length);
        Assert.assertEquals(void.class, def.fallback());
        Assert.assertEquals(void.class, def.fallbackFactory());
        Assert.assertEquals("", def.path());
        Assert.assertTrue(def.primary());
        Assert.assertFalse(def.local());
        Assert.assertFalse(def.autoLocal());
        Assert.assertEquals("", def.autoLocalBasePackage());

        // 别名与属性
        ProtostuffFeignClient anno = AnnotationUtils.findAnnotation(Client.class, ProtostuffFeignClient.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("svc", anno.value());
        Assert.assertEquals("/p", anno.path());
        Assert.assertTrue(anno.local());
        Assert.assertTrue(anno.autoLocal());
        Assert.assertEquals("bp", anno.autoLocalBasePackage());
        // @AliasFor -> FeignClient 元注解(合并属性)
        Assert.assertEquals("svc", AnnotationUtils.getAnnotationAttributes(anno).get("value"));

        Assert.assertNotNull(ProtostuffFeignClient.class.getAnnotation(Inherited.class));
        Assert.assertNotNull(ProtostuffFeignClient.class.getAnnotation(Documented.class));
    }

    @ProtostuffFeignClient
    interface Defaults {
    }
}
