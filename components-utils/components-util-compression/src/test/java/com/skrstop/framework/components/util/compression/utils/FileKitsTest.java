package com.skrstop.framework.components.util.compression.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author 蒋时华
 */
public class FileKitsTest {

    @Test
    public void testGetFileName() {
        Assert.assertEquals("", FileKits.getFileName(null));
        Assert.assertEquals("a.txt", FileKits.getFileName("/tmp/x/a.txt"));
    }

    @Test
    public void testGetFolderPath() throws Exception {
        Assert.assertEquals("", FileKits.getFolderPath(null));
        File temp = File.createTempFile("prefixfk", ".txt");
        temp.deleteOnExit();
        String folder = FileKits.getFolderPath(temp.getAbsolutePath());
        Assert.assertNotNull(folder);
    }

    @Test
    public void testGetExtension() {
        Assert.assertEquals("", FileKits.getExtension(null));
        Assert.assertEquals("", FileKits.getExtension("noext"));
        Assert.assertEquals("txt", FileKits.getExtension("a.TXT"));
        Assert.assertEquals("gz", FileKits.getExtension("a.tar.gz"));
    }

    @Test
    public void testIsExtension() {
        Assert.assertFalse(FileKits.isExtension(null, "txt"));
        Assert.assertFalse(FileKits.isExtension("a.txt", null));
        Assert.assertFalse(FileKits.isExtension("txt", "txt"));
        Assert.assertTrue(FileKits.isExtension("a.txt", "txt"));
        Assert.assertTrue(FileKits.isExtension("A.TXT ", "TXT"));
        Assert.assertFalse(FileKits.isExtension("a.txt", "zip"));
    }

    @Test
    public void testGetFileNameWithouExtension() {
        Assert.assertEquals("", FileKits.getFileNameWithouExtension(null));
        Assert.assertEquals("noext", FileKits.getFileNameWithouExtension("noext"));
        Assert.assertEquals("a.tar", FileKits.getFileNameWithouExtension("a.tar.gz"));
    }

    @Test
    public void testGetSafePath() {
        Assert.assertNull(FileKits.getSafePath(null));
        Assert.assertEquals("/a/b", FileKits.getSafePath("\\a\\b"));
        Assert.assertEquals("/a/b", FileKits.getSafePath("//a//b"));
    }

    @Test
    public void testConvertRoundTrip() throws Exception {
        File temp = File.createTempFile("prefixconv", ".dat");
        temp.deleteOnExit();
        byte[] data = "hello world".getBytes();
        FileKits.convertByteToFile(temp.getAbsolutePath(), data);
        byte[] read = FileKits.convertFileToByte(temp.getAbsolutePath());
        Assert.assertArrayEquals(data, read);
        // 嵌套目录自动创建
        String nested = temp.getParent() + "/nested1/nested2/out.txt";
        FileKits.convertByteToFile(nested, data);
        Assert.assertTrue(new File(nested).exists());
    }

    @Test
    public void testConvertFileToByteError() throws Exception {
        // 不存在的文件 → 异常被捕获返回 null
        Assert.assertNull(FileKits.convertFileToByte("/not/exist/file.txt"));
    }

    @Test
    public void testDelete() throws Exception {
        File temp = File.createTempFile("prefixdel", ".txt");
        FileKits.delete(temp.getAbsolutePath());
        Assert.assertFalse(temp.exists());
        // 不存在 → 记录日志不抛异常
        FileKits.delete("/not/exist/file.txt");
    }

    @Test
    public void testDeleteParent() throws Exception {
        File temp = File.createTempFile("prefixdp", ".txt");
        FileKits.deleteParent(temp.getAbsolutePath());
        Assert.assertFalse(temp.exists());
        FileKits.deleteParent("/not/exist/file.txt");
    }

}
