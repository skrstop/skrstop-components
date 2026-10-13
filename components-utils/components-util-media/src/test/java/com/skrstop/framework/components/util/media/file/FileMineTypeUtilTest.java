package com.skrstop.framework.components.util.media.file;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class FileMineTypeUtilTest {

    private InputStream png() {
        // PNG 魔数
        byte[] png = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0};
        return new ByteArrayInputStream(png);
    }

    @Test
    public void testEquals() throws Exception {
        Assert.assertTrue(FileMineTypeUtil.equals(png(), "image/png"));
        Assert.assertFalse(FileMineTypeUtil.equals(png(), "image/jpeg"));
        Assert.assertFalse(FileMineTypeUtil.equals(png(), " "));
        Assert.assertFalse(FileMineTypeUtil.equals(png(), null));
    }

    @Test
    public void testContains() throws Exception {
        Assert.assertTrue(FileMineTypeUtil.contains(png(), Arrays.asList("image/gif", "image/png")));
        Assert.assertFalse(FileMineTypeUtil.contains(png(), Arrays.asList("image/gif", "image/jpeg")));
        Assert.assertFalse(FileMineTypeUtil.contains(png(), Collections.emptyList()));
        Assert.assertFalse(FileMineTypeUtil.contains(png(), null));
    }


    @Test
    public void testEqualsDetectNull() throws Exception {
        // Tika.detect 返回 null → parse 为 null → false
        try (org.mockito.MockedConstruction<org.apache.tika.Tika> mocked =
                     org.mockito.Mockito.mockConstruction(org.apache.tika.Tika.class,
                             (mock, context) -> org.mockito.Mockito.when(mock.detect(org.mockito.ArgumentMatchers.any(java.io.InputStream.class))).thenReturn(null))) {
            Assert.assertFalse(FileMineTypeUtil.equals(png(), "image/png"));
            Assert.assertFalse(FileMineTypeUtil.contains(png(), Arrays.asList("image/png")));
        }
    }


}
