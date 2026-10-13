package com.skrstop.framework.components.util.value.format;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class TextFormatUtilTest {

    @Test
    public void testStringFormatPath() {
        Assert.assertEquals("a=1", TextFormatUtil.formatString("a=%s", 1));
        Assert.assertEquals("10%", TextFormatUtil.formatString("10%%"));
        Assert.assertEquals("x", TextFormatUtil.formatString("%s", "x"));
    }

    @Test
    public void testMessageFormatterPath() {
        Assert.assertEquals("Hi there.", TextFormatUtil.formatString("Hi {}.", "there"));
        Assert.assertEquals("1-2", TextFormatUtil.formatString("{}-{}", 1, 2));
        Assert.assertEquals("1-{}", TextFormatUtil.formatString("{}-{}", 1));
        Assert.assertEquals("1", TextFormatUtil.formatString("{}", 1, 2));
        Assert.assertEquals("plain", TextFormatUtil.formatString("plain", 1));
        Assert.assertEquals("", TextFormatUtil.formatString(null, 1));
        Assert.assertEquals("{}x", TextFormatUtil.formatString("\\{}{}", "x"));
        Assert.assertEquals("\\x{}", TextFormatUtil.formatString("\\\\{}{}", "x"));
        Assert.assertEquals("null", TextFormatUtil.formatString("{}", (Object) null));
    }

    @Test
    public void testArrayParams() {
        Assert.assertEquals("[1, 2]", TextFormatUtil.formatString("{}", new Object[]{new int[]{1, 2}}));
        Assert.assertEquals("[a, b]", TextFormatUtil.formatString("{}", new Object[]{new Object[]{"a", "b"}}));
        Assert.assertEquals("[[1]]", TextFormatUtil.formatString("{}", new Object[]{new int[][]{{1}}}));
        Assert.assertEquals("[true]", TextFormatUtil.formatString("{}", new Object[]{new boolean[]{true}}));
        Assert.assertEquals("[1]", TextFormatUtil.formatString("{}", new Object[]{new byte[]{1}}));
        Assert.assertEquals("[a]", TextFormatUtil.formatString("{}", new Object[]{new char[]{'a'}}));
        Assert.assertEquals("[1]", TextFormatUtil.formatString("{}", new Object[]{new short[]{1}}));
        Assert.assertEquals("[1]", TextFormatUtil.formatString("{}", new Object[]{new long[]{1}}));
        Assert.assertEquals("[1.0]", TextFormatUtil.formatString("{}", new Object[]{new float[]{1.0f}}));
        Assert.assertEquals("[1.0]", TextFormatUtil.formatString("{}", new Object[]{new double[]{1.0}}));
        Assert.assertEquals("[a][b]", TextFormatUtil.formatString("{}{}", new Object[]{new Object[]{"a"}, new Object[]{"b"}}));
        Assert.assertEquals("obj", TextFormatUtil.formatString("{}", new Object() {
            @Override
            public String toString() {return "obj";}
        }));
        // arrayFormat 顶层带异常参数
        RuntimeException ex = new RuntimeException("boom");
        Assert.assertEquals("a-b", TextFormatUtil.formatString("{}-{}", new Object[]{"a", "b", ex}));
        // 单参数 + 异常 → trimmedLen=0
        Assert.assertEquals("a", TextFormatUtil.formatString("{}", new Object[]{"a", ex}));
        // 仅异常参数 → trimmedLen=0 F 分支
        Assert.assertEquals("{}", TextFormatUtil.formatString("{}", new Object[]{ex}));
        // argArray 为 null
        Assert.assertEquals("plain", TextFormatUtil.formatString("plain", (Object[]) null));
        // 单元素数组（", " 分隔符 F 分支）
        Assert.assertEquals("[true]", TextFormatUtil.formatString("{}", new Object[]{new boolean[]{true}}));
        Assert.assertEquals("[a]", TextFormatUtil.formatString("{}", new Object[]{new char[]{'a'}}));
        Assert.assertEquals("[1]", TextFormatUtil.formatString("{}", new Object[]{new short[]{1}}));
        Assert.assertEquals("[1]", TextFormatUtil.formatString("{}", new Object[]{new long[]{1}}));
        Assert.assertEquals("[1.0]", TextFormatUtil.formatString("{}", new Object[]{new float[]{1.0f}}));
        // toString 抛异常的对象
        Assert.assertEquals("[FAILED toString()]", TextFormatUtil.formatString("{}", new Object[]{new Object() {
            @Override
            public String toString() {throw new IllegalStateException("boom");}
        }}));
        // 自引用数组 → "..."
        Object[] self = new Object[1];
        self[0] = self;
        Assert.assertEquals("[[...]]", TextFormatUtil.formatString("{}", new Object[]{self}));
        // 转义但前一字符非反斜杠（isDoubleEscaped F）
        Assert.assertEquals("ab{}x", TextFormatUtil.formatString("ab\\{}{}", "x"));
    }

}