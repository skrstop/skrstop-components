package com.skrstop.framework.components.starter.annotation.handle.trim;

import com.skrstop.framework.components.starter.annotation.anno.trim.Trim;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * @author 蒋时华
 */
public class TrimParseTest {

    @SuppressWarnings("unused")
    private final Object ignore = new com.skrstop.framework.components.starter.annotation.handle.trim.TrimParse();

    private Trim trim(final TrimmerType type) {
        return new Trim() {
            @Override
            public TrimmerType value() {
                return type;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Trim.class;
            }
        };
    }

    @Test
    public void testNull() {
        Assert.assertEquals("x", TrimParse.parse(null, "x"));
        Assert.assertNull(TrimParse.parse(trim(TrimmerType.ALL), null));
    }

    @Test
    public void testAll() {
        Assert.assertEquals("abc", TrimParse.parse(trim(TrimmerType.ALL), " a b c "));
    }

    @Test
    public void testLeftRight() {
        Assert.assertEquals("abc", TrimParse.parse(trim(TrimmerType.LEFT_RIGHT), " abc "));
    }

    @Test
    public void testLeft() {
        Assert.assertEquals("abc  ", TrimParse.parse(trim(TrimmerType.LEFT), "  abc  "));
        // 全角空格
        Assert.assertEquals("abc", TrimParse.parse(trim(TrimmerType.LEFT), "　abc"));
    }

    @Test
    public void testRight() {
        Assert.assertEquals("  abc", TrimParse.parse(trim(TrimmerType.RIGHT), "  abc  "));
        Assert.assertEquals("abc", TrimParse.parse(trim(TrimmerType.RIGHT), "abc　"));
    }

}