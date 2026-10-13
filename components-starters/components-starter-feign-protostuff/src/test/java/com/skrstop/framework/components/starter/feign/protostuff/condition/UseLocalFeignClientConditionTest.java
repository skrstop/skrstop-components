package com.skrstop.framework.components.starter.feign.protostuff.condition;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.starter.feign.protostuff.scanapp.AutoLocalAppClient;
import com.skrstop.framework.components.starter.feign.protostuff.scanapp.AutoLocalNoImplClient;
import com.skrstop.framework.components.starter.feign.protostuff.scanapp.AutoLocalPlainClient;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

public class UseLocalFeignClientConditionTest {

    private final UseLocalFeignClientCondition condition = new UseLocalFeignClientCondition();

    private AnnotationMetadata metadata(Class<?> clazz) throws Exception {
        return new SimpleMetadataReaderFactory().getMetadataReader(clazz.getName()).getAnnotationMetadata();
    }

    @Test
    public void localTrue() throws Exception {
        // local=true -> 不走本地 -> false
        Assert.assertFalse(condition.matches(null, metadata(ConditionLocalTrue.class)));
    }

    @Test
    public void plainUseFeign() throws Exception {
        // local=false, autoLocal=false -> true
        Assert.assertTrue(condition.matches(null, metadata(ConditionPlain.class)));
    }

    @Test
    public void autoLocalCases() throws Exception {
        // getSubTypesOf 恒返回空集合 -> autoLocal 恒走 feign(true)
        Assert.assertTrue(condition.matches(null, metadata(AutoLocalAppClient.class)));
        Assert.assertTrue(condition.matches(null, metadata(AutoLocalPlainClient.class)));
        Assert.assertTrue(condition.matches(null, metadata(AutoLocalNoImplClient.class)));
    }

    @ProtostuffFeignClient(local = true)
    interface ConditionLocalTrue {
    }

    @ProtostuffFeignClient
    interface ConditionPlain {
    }
}
