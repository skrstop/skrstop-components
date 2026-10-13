package com.skrstop.framework.components.starter.web.utils;

import org.apache.commons.fileupload.FileItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class MultipartFileUtilTest {

    @Test
    public void testCreateFileItem() throws Exception {
        byte[] data = "file-content".getBytes(StandardCharsets.UTF_8);
        FileItem item = MultipartFileUtil.createFileItem(new ByteArrayInputStream(data), "test.txt");
        Assert.assertEquals("test.txt", item.getName());
        byte[] out = new byte[(int) item.getSize()];
        item.getInputStream().read(out);
        Assert.assertArrayEquals(data, out);
        java.lang.reflect.Constructor<?> c = MultipartFileUtil.class.getDeclaredConstructor();
        c.setAccessible(true);
        Assert.assertNotNull(c.newInstance());
    }
}
