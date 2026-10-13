package com.skrstop.framework.components.util.extra;

import com.skrstop.framework.components.util.extra.pinyin.DoubbPinyinEngine;
import org.junit.Assert;
import org.junit.Test;

public class DoubbPinyinEngineTest {

    @Test
    public void testGetPinyinChar() {
        DoubbPinyinEngine engine = new DoubbPinyinEngine();
        String pinyin = engine.getPinyin('中');
        Assert.assertNotNull(pinyin);
        Assert.assertFalse(pinyin.isEmpty());
    }

    @Test
    public void testGetPinyinString() {
        DoubbPinyinEngine engine = new DoubbPinyinEngine();
        String pinyin = engine.getPinyin("中文", " ");
        Assert.assertNotNull(pinyin);
        Assert.assertTrue(pinyin.contains(" "));
    }
}
