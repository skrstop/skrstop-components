package com.skrstop.framework.components.util.compression.bean;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class BeanTests {

    @Test
    public void testCompressionType() {
        Assert.assertEquals("ZIP", CompressionType.ZIP.getName());
        Assert.assertEquals("zip", CompressionType.ZIP.getExtension());
        Assert.assertEquals("application/zip", CompressionType.ZIP.getMimeType());
        Assert.assertNull(CompressionType.AR.getMimeType());
        Assert.assertNotNull(CompressionType.ZIP.getCompressProcessor());
        Assert.assertEquals(11, CompressionType.values().length);
    }

    @Test
    public void testLevels() {
        Assert.assertEquals(0, ZipLevel.NO_COMPRESS.getValue());
        Assert.assertEquals(3, ZipLevel.NORMAL.getValue());
        Assert.assertEquals(5, ZipLevel.HIGHEST.getValue());
        Assert.assertEquals(0, GzipLevel.NO_COMPRESS.getValue());
        Assert.assertEquals(3, GzipLevel.NORMAL.getValue());
        Assert.assertEquals(0, Bzip2Level.NO_COMPRESS.getValue());
        Assert.assertEquals(5, Bzip2Level.HIGHEST.getValue());
        Assert.assertEquals(0, JarLevel.NO_COMPRESS.getValue());
        Assert.assertEquals(5, JarLevel.HIGHEST.getValue());
    }

    @Test
    public void testBinaryFile() throws Exception {
        java.io.File temp = java.io.File.createTempFile("prefixbf", ".dat");
        temp.deleteOnExit();
        java.io.FileOutputStream fo = new java.io.FileOutputStream(temp);
        fo.write("content".getBytes());
        fo.close();

        BinaryFile fromData = new BinaryFile("inner/path.txt", "content2".getBytes());
        Assert.assertEquals("inner/path.txt", fromData.getDesPath());
        Assert.assertEquals(8, fromData.getActualSize());

        BinaryFile fromFile = new BinaryFile(temp.getAbsolutePath(), "inner/file.dat", false);
        Assert.assertEquals(7, fromFile.getActualSize());
        Assert.assertEquals(temp.getAbsolutePath(), fromFile.getSrcPath());
        Assert.assertNotNull(fromFile.getData());

        // newInstance + deleteSrc
        java.io.File temp2 = java.io.File.createTempFile("prefixbf2", ".dat");
        java.io.FileOutputStream fo2 = new java.io.FileOutputStream(temp2);
        fo2.write("abc".getBytes());
        fo2.close();
        BinaryFile deleted = BinaryFile.newInstance(temp2.getAbsolutePath(), "x/y.bin", true);
        Assert.assertEquals(3, deleted.getActualSize());
        Assert.assertFalse(temp2.exists());
    }

}
