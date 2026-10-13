package com.skrstop.framework.components.util.value.validate;

import com.skrstop.framework.components.core.exception.defined.illegal.ParameterException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author 蒋时华
 */
public class ErrorMessageUtilTest {

    @Test
    public void testGetErrorMessage() {
        Assert.assertEquals(new ArrayList<>(), ErrorMessageUtil.getErrorMessage(null));
        Assert.assertEquals(new ArrayList<>(), ErrorMessageUtil.getErrorMessage(Collections.emptyList()));
        List<String> sorted = ErrorMessageUtil.getErrorMessage(Arrays.asList("2##b", "1##a", "3##c"));
        Assert.assertEquals(Arrays.asList("1##a", "2##b", "3##c"), sorted);
        // 无序号的消息排在后面
        List<String> sorted2 = ErrorMessageUtil.getErrorMessage(Arrays.asList("b", "1##a"));
        Assert.assertEquals("1##a", sorted2.get(0));
        // blank 消息排最后
        List<String> sorted3 = ErrorMessageUtil.getErrorMessage(Arrays.asList(" ", "1##a"));
        Assert.assertEquals(" ", sorted3.get(sorted3.size() - 1));
        // 双 blank → 0
        List<String> sorted4 = ErrorMessageUtil.getErrorMessage(Arrays.asList(" ", "  "));
        Assert.assertEquals(2, sorted4.size());
        // o2 blank → -1
        List<String> sorted5 = ErrorMessageUtil.getErrorMessage(Arrays.asList("a", " "));
        Assert.assertEquals("a", sorted5.get(0));
        // 无 ## 与有序号混合，无序号靠后
        List<String> sorted6 = ErrorMessageUtil.getErrorMessage(Arrays.asList("x", "2##b", "y"));
        Assert.assertEquals("2##b", sorted6.get(0));
        // "a##b" 无序号前缀
        Assert.assertEquals("a##b", ErrorMessageUtil.getFirstErrorMessage(Arrays.asList("a##b")));
        // \u00a0 属于空白且自然排序靠后 → 比较 (blank, non-blank) → return 1
        List<String> sorted7 = ErrorMessageUtil.getErrorMessage(Arrays.asList("b", "\u00a0"));
        Assert.assertEquals("b", sorted7.get(0));
        // 有 ## 项自然排序靠后 → (o1Index!=-1, o2Index==-1) → return -1
        List<String> sorted8 = ErrorMessageUtil.getErrorMessage(Arrays.asList("b", "c##x"));
        Assert.assertEquals("c##x", sorted8.get(0));
    }

    @Test
    public void testGetFirstErrorMessage() {
        Assert.assertNull(ErrorMessageUtil.getFirstErrorMessage(null));
        Assert.assertNull(ErrorMessageUtil.getFirstErrorMessage(Collections.emptyList()));
        Assert.assertEquals("msg", ErrorMessageUtil.getFirstErrorMessage(Arrays.asList("1##msg", "2##x")));
        // 无序号前缀
        Assert.assertEquals("plain", ErrorMessageUtil.getFirstErrorMessage(Arrays.asList("plain")));
        // 有 ## 但无序号
        Assert.assertEquals("plain##x", ErrorMessageUtil.getFirstErrorMessage(Arrays.asList("plain##x")));
    }

    public static class ValidBean {
        @jakarta.validation.constraints.NotNull
        private final String name;

        public ValidBean(String name) {this.name = name;}

        public String getName() {return name;}
    }

    @Test
    public void testParameterValidate() {
        ParameterValidateUtil.validate(new ValidBean("x"));
        try {
            ParameterValidateUtil.validate(new ValidBean(null));
            Assert.fail();
        } catch (ParameterException e) {
            Assert.assertTrue(e.getMessage().contains("参数校验失败"));
        }
        // message
        Assert.assertTrue(ParameterValidateUtil.validateMessage(new ValidBean(null)).size() > 0);
        Assert.assertEquals(0, ParameterValidateUtil.validateMessage(new ValidBean("x")).size());
    }

    @Test
    public void testHibernateValidate() {
        ParameterValidateUtil.hibernateValidate(new ValidBean("x"));
        try {
            ParameterValidateUtil.hibernateValidate(new ValidBean(null));
            Assert.fail();
        } catch (ParameterException e) {
            // failFast 只返回第一条
            Assert.assertTrue(e.getMessage().contains("参数校验失败"));
        }
        Assert.assertEquals(1, ParameterValidateUtil.hibernateValidateMessage(new ValidBean(null)).size());
        Assert.assertEquals(0, ParameterValidateUtil.hibernateValidateMessage(new ValidBean("x")).size());
    }

}