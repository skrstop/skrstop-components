package com.skrstop.framework.components.starter.spring.support.bean;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class SpringUtilTest {

    @Test
    public void testClassAnnotation() {
        Assert.assertNotNull(SpringUtil.class.getAnnotation(org.springframework.context.annotation.Configuration.class));
        Assert.assertNotNull(cn.hutool.extra.spring.SpringUtil.class.cast(new SpringUtil()));
    }

}