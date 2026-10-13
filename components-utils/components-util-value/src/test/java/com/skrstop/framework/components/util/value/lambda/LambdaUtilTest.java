package com.skrstop.framework.components.util.value.lambda;

import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.lang.func.Func1;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class LambdaUtilTest {

    @Data
    public static class Bean {
        private String userName;
        private Long userId;
    }

    @Test
    public void testConvertToFieldName() throws Exception {
        Func1<Bean, String> getter = Bean::getUserName;
        Assert.assertEquals("userName", LambdaUtil.convertToFieldName(getter));
        Assert.assertEquals("user_name", LambdaUtil.convertToFieldNameByUnderline(getter));
        // 源码行为：无下划线输入会整体转小写
        Assert.assertEquals("username", LambdaUtil.convertToFieldNameByHump(getter));
    }

    @Test
    public void testFunc0() {
        Bean bean = new Bean();
        bean.setUserName("u");
        // 无参 getter 方法引用可正常解析
        Func0<String> f = bean::getUserName;
        Assert.assertEquals("userName", LambdaUtil.convertToFieldName(f));
        Assert.assertEquals("user_name", LambdaUtil.convertToFieldNameByUnderline(f));
        Assert.assertEquals("username", LambdaUtil.convertToFieldNameByHump(f));
    }

    @Test
    public void testFunc0NotSupported() {
        // 构造器引用无法解析字段名，各入口均抛 IllegalArgumentException
        Func0<Bean> supplier = Bean::new;
        for (Runnable r : new Runnable[]{
                () -> {
                    try {
                        LambdaUtil.convertToFieldName(supplier);
                        Assert.fail();
                    } catch (IllegalArgumentException ignored) {
                    }
                },
                () -> {
                    try {
                        LambdaUtil.convertToFieldNameByUnderline(supplier);
                        Assert.fail();
                    } catch (IllegalArgumentException ignored) {
                    }
                },
                () -> {
                    try {
                        LambdaUtil.convertToFieldNameByHump(supplier);
                        Assert.fail();
                    } catch (IllegalArgumentException ignored) {
                    }
                }}) {
            r.run();
        }
    }

}
