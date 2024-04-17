package com.skrstop.framework.components.starter.annotation;

import com.skrstop.framework.components.starter.annotation.anno.server.SApplication;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 蒋时华
 * @date 2023-12-18 18:33:33
 */
public class AnnotationFIndUtilTest {

    @SApplication
    public static class AnnoClass {

        public void print() {
            System.out.println("hello world");
        }

    }

    @Test
    public void test() throws NoSuchMethodException {
        Method printMethod = AnnoClass.class.getDeclaredMethod("print");
        boolean has = AnnoFindUtil.has(printMethod, SApplication.class);
        Assert.assertEquals(has, true);
        has = AnnoFindUtil.has(printMethod, Component.class);
        Assert.assertEquals(has, true);
    }

}
