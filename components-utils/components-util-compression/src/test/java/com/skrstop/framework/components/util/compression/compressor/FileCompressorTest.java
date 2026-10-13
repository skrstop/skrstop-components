package com.skrstop.framework.components.util.compression.compressor;

import com.skrstop.framework.components.util.compression.bean.*;
import com.skrstop.framework.components.util.compression.processor.SevenZipProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

/**
 * @author 蒋时华
 */
public class FileCompressorTest {

    private File workDir;
    private File srcFile;

    @Before
    public void setUp() throws Exception {
        workDir = Files.createTempDirectory("fcompressor").toFile();
        srcFile = new File(workDir, "src.txt");
        Files.write(srcFile.toPath(), "hello compression".getBytes());
    }

    @After
    public void tearDown() {
        delete(workDir);
    }

    private static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                delete(f);
            }
        }
        file.delete();
    }

    private void roundTrip(CompressionType type, CompressionLevel level) throws Exception {
        String compressedPath = new File(workDir, "out." + type.getExtension()).getAbsolutePath();
        FileCompressor compressor = new FileCompressor();
        compressor.add(srcFile.getAbsolutePath(), "inner/src.txt");
        compressor.setType(type);
        compressor.setLevel(level);
        compressor.setComment("comment");
        compressor.setCompressedPath(compressedPath);
        compressor.compress();
        Assert.assertTrue(new File(compressedPath).exists() || type == CompressionType.SEVENZIP);
        Assert.assertTrue(compressor.getCompressedSize() >= 0);
        Assert.assertEquals(type, compressor.getCompressedType());
        Assert.assertEquals(type.getName(), compressor.getCompressedTypeName());

        FileCompressor reader = FileCompressor.read(compressedPath);
        Assert.assertNotNull(reader);
        Assert.assertNotNull(reader.getBinaryFile("inner/src.txt"));
        Assert.assertArrayEquals("hello compression".getBytes(), reader.getBinaryFile("inner/src.txt").getData());

        File outDir = new File(workDir, "out_" + type.getExtension());
        reader.decompress(outDir.getAbsolutePath());
        byte[] restored = Files.readAllBytes(new File(outDir, "inner/src.txt").toPath());
        Assert.assertArrayEquals("hello compression".getBytes(), restored);

        reader.extract("inner/src.txt", new File(workDir, "extract_" + type.getExtension()).getAbsolutePath());
        reader.extract("no/such/file", new File(workDir, "extract2").getAbsolutePath());
    }

    @Test
    public void testZip() throws Exception {
        roundTrip(CompressionType.ZIP, ZipLevel.NORMAL);
    }

    @Test
    public void testJar() throws Exception {
        roundTrip(CompressionType.JAR, JarLevel.NORMAL);
    }

    @Test
    public void testTar() throws Exception {
        roundTrip(CompressionType.TAR, ZipLevel.NORMAL);
    }

    @Test
    public void testGz() throws Exception {
        roundTrip(CompressionType.GZ, GzipLevel.NORMAL);
    }

    @Test
    public void testTarGz() throws Exception {
        roundTrip(CompressionType.TAR_GZ, ZipLevel.NORMAL);
    }

    @Test
    public void testTarBz2() throws Exception {
        roundTrip(CompressionType.TAR_BZ2, Bzip2Level.NORMAL);
    }

    @Test
    public void testBz2() throws Exception {
        roundTrip(CompressionType.BZ2, Bzip2Level.NORMAL);
    }

    @Test
    public void testAr() throws Exception {
        roundTrip(CompressionType.AR, ZipLevel.NORMAL);
    }

    @Test
    public void testCpio() throws Exception {
        roundTrip(CompressionType.CPIO, ZipLevel.NORMAL);
    }

    @Test
    public void testXz() throws Exception {
        roundTrip(CompressionType.XZ, ZipLevel.NORMAL);
    }

    @Test
    public void testSevenZip() throws Exception {
        // SEVENZIP 不允许走 compress 校验（validateAllowCompressing 不含），直接用 processor 写文件
        String compressedPath = new File(workDir, "out.7z").getAbsolutePath();
        FileCompressor compressor = new FileCompressor();
        compressor.setCompressedPath(compressedPath);
        compressor.setType(CompressionType.SEVENZIP);
        compressor.addFile(srcFile.getAbsolutePath(), "inner/src.txt");
        new SevenZipProcessor().compressData(compressor);
        Assert.assertTrue(new File(compressedPath).exists());

        FileCompressor reader = FileCompressor.read(compressedPath);
        Assert.assertNotNull(reader);
        Assert.assertNotNull(reader.getBinaryFile("inner/src.txt"));
        Assert.assertArrayEquals("hello compression".getBytes(), reader.getBinaryFile("inner/src.txt").getData());
    }

    @Test
    public void testValidationErrors() throws Exception {
        FileCompressor compressor = new FileCompressor();
        try {
            compressor.compress();
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        compressor.setCompressedPath(new File(workDir, "v.zip").getAbsolutePath());
        try {
            compressor.compress();
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            FileCompressor.validateAllowCompressing(CompressionType.SEVENZIP);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            FileCompressor.validateAllowDecompressing(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        int[] badLevels = {99, -2};
        for (int lv : badLevels) {
            for (CompressionType t : new CompressionType[]{CompressionType.ZIP, CompressionType.JAR,
                    CompressionType.GZ, CompressionType.BZ2}) {
                try {
                    FileCompressor.validateLevel(t, () -> lv);
                    Assert.fail();
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        FileCompressor.validateLevel(CompressionType.TAR, ZipLevel.NORMAL);
        try {
            FileCompressor.validateMapFile(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            FileCompressor.validateMapFile(compressor.getMapBinaryFile());
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            FileCompressor.validateCompressedPath(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            FileCompressor.validateCompressedPath("");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            FileCompressor.validateType(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        FileCompressor.validateType(CompressionType.ZIP);
        try {
            FileCompressor.getCompressProcessor(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        Assert.assertNotNull(FileCompressor.getCompressProcessor(CompressionType.ZIP));
        try {
            FileCompressor.getFileType("file.unknownext");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        Assert.assertEquals(CompressionType.ZIP, FileCompressor.getFileType("x.zip"));
        Assert.assertEquals(CompressionType.TAR_GZ, FileCompressor.getFileType("x.tar.gz"));
    }

    @Test
    public void testReadInvalidFile() throws Exception {
        // 不存在的文件 → 读取异常返回 null
        Assert.assertNull(FileCompressor.read(new File(workDir, "ghost.zip").getAbsolutePath()));
        File zip = new File(workDir, "rd.zip");
        FileCompressor compressor = new FileCompressor();
        compressor.add(srcFile.getAbsolutePath(), "a.txt");
        compressor.setType(CompressionType.ZIP);
        compressor.setLevel(ZipLevel.NORMAL);
        compressor.setCompressedPath(zip.getAbsolutePath());
        compressor.compress();
        FileCompressor readAndDeleted = FileCompressor.readAndDelete(zip.getAbsolutePath());
        Assert.assertNotNull(readAndDeleted);
        Assert.assertFalse(zip.exists());
    }

    @Test
    public void testAddRemove() throws Exception {
        FileCompressor compressor = new FileCompressor();
        compressor.add("/not/exist/path", "x");
        compressor.add(srcFile.getAbsolutePath(), "src.txt");
        File sub = new File(workDir, "sub");
        sub.mkdir();
        Files.write(new File(sub, "b.txt").toPath(), "b".getBytes());
        compressor.add(sub.getAbsolutePath(), "sub");
        Assert.assertNotNull(compressor.getBinaryFile("sub/b.txt"));

        File tempDel = new File(workDir, "del.txt");
        Files.write(tempDel.toPath(), "del".getBytes());
        compressor.addFileAndDelete(tempDel.getAbsolutePath(), "del.txt");
        Assert.assertFalse(tempDel.exists());

        compressor.removeFile("del.txt");
        Assert.assertNull(compressor.getBinaryFile("del.txt"));
        // removeFile 不存在 → logger 记录 null binaryFile（源码行为：抛 NPE）
        try {
            compressor.removeFile("no/such");
        } catch (NullPointerException ignored) {
        }

        BinaryFile bf = compressor.getBinaryFile("src.txt");
        long sizeBefore = compressor.getActualSize();
        compressor.removeBinaryFile(bf);
        Assert.assertNull(compressor.getBinaryFile("src.txt"));
        Assert.assertEquals(sizeBefore - bf.getActualSize(), compressor.getActualSize());

        File del2 = new File(workDir, "del2.txt");
        Files.write(del2.toPath(), "d2".getBytes());
        compressor.addAndDelete(del2.getAbsolutePath(), "d2.txt");
        Assert.assertFalse(del2.exists());
        File delDir = new File(workDir, "deldir");
        delDir.mkdir();
        Files.write(new File(delDir, "x.txt").toPath(), "x".getBytes());
        compressor.addAndDelete(delDir.getAbsolutePath(), "deldir");
        Assert.assertFalse(delDir.exists());
        compressor.addAndDelete("/not/exist", "x");

        // deleteAllSources 会连带删除源文件父目录，使用独立子目录避免影响 workDir
        File ds = new File(workDir, "ds");
        ds.mkdir();
        File dsFile = new File(ds, "ds.txt");
        Files.write(dsFile.toPath(), "ds".getBytes());
        compressor.addFile(dsFile.getAbsolutePath(), "ds.txt");
        compressor.deleteAllSources();
        Assert.assertFalse(ds.exists());

        File zip = new File(workDir, "cad.zip");
        File cadSrc = new File(workDir, "cadsrc.txt");
        Files.write(cadSrc.toPath(), "cad".getBytes());
        FileCompressor c2 = new FileCompressor();
        c2.addFile(cadSrc.getAbsolutePath(), "cad.txt");
        c2.setType(CompressionType.ZIP);
        c2.setLevel(ZipLevel.NORMAL);
        c2.setCompressedPath(zip.getAbsolutePath());
        c2.compressAndDelete();
        Assert.assertFalse(cadSrc.exists());
    }

    @Test
    public void testGetCompressedTypeNameNull() {
        FileCompressor compressor = new FileCompressor();
        Assert.assertEquals("", compressor.getCompressedTypeName());
    }

}
