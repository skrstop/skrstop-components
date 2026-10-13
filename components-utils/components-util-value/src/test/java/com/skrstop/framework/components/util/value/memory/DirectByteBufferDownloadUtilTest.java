package com.skrstop.framework.components.util.value.memory;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;

/**
 * @author 蒋时华
 */
public class DirectByteBufferDownloadUtilTest {

    @Test
    public void testDownloadToFile() throws Exception {
        byte[] data = new byte[1024];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 128);
        }
        File target = File.createTempFile("dlf", ".bin");
        try {
            DirectByteBufferDownloadUtil.downloadToFile(new ByteArrayInputStream(data), target);
            Assert.assertArrayEquals(data, Files.readAllBytes(target.toPath()));
        } finally {
            target.delete();
        }
        File target2 = File.createTempFile("dlf", ".bin");
        try {
            DirectByteBufferDownloadUtil.downloadToFile(
                    new ByteArrayInputStream(data), target2, 128);
            Assert.assertArrayEquals(data, Files.readAllBytes(target2.toPath()));
        } finally {
            target2.delete();
        }
        File target3 = File.createTempFile("dlf", ".bin");
        try {
            DirectByteBufferDownloadUtil.downloadToFile(new ByteArrayInputStream(new byte[0]), target3, 128);
            Assert.assertEquals(0, target3.length());
        } finally {
            target3.delete();
        }
    }

    @Test
    public void testDownloadToTempFile() throws Exception {
        File temp = DirectByteBufferDownloadUtil.downloadToTempFile(
                new ByteArrayInputStream("hello".getBytes()));
        try {
            Assert.assertEquals("hello", new String(Files.readAllBytes(temp.toPath())));
        } finally {
            temp.delete();
        }
        File temp2 = DirectByteBufferDownloadUtil.downloadToTempFile(
                new ByteArrayInputStream("world".getBytes()), 64);
        try {
            Assert.assertEquals("world", new String(Files.readAllBytes(temp2.toPath())));
        } finally {
            temp2.delete();
        }
    }

    @Test
    public void testClose() throws Exception {
        DirectByteBufferDownloadUtil.close(null);
        DirectByteBufferDownloadUtil.close(ByteBuffer.allocate(16));
        ByteBuffer direct = ByteBuffer.allocateDirect(64);
        direct.put((byte) 1);
        Assert.assertEquals(1, direct.position());
        // 释放后不可再访问其内存，否则 JVM 崩溃
        DirectByteBufferDownloadUtil.close(direct);
    }

}
