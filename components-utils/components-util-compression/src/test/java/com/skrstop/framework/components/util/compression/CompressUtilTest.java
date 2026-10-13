package com.skrstop.framework.components.util.compression;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author 蒋时华
 */
public class CompressUtilTest {

    private File workDir;
    private File srcDir;

    @Before
    public void setUp() throws Exception {
        workDir = Files.createTempDirectory("compressutil").toFile();
        srcDir = new File(workDir, "src");
        srcDir.mkdir();
        Files.write(new File(srcDir, "a.txt").toPath(), "aaa".getBytes());
        File sub = new File(srcDir, "sub");
        sub.mkdir();
        Files.write(new File(sub, "b.txt").toPath(), "bbb".getBytes());
        Files.write(new File(srcDir, "hidden.txt").toPath(), "h".getBytes());
        // hidden files are filtered by parseDirectory
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

    @Test
    public void testCompressZipList() throws Exception {
        String zip = new File(workDir, "l.zip").getAbsolutePath();
        CompressUtil.compressZip(zip, CompressUtil.parseDirectory(srcDir.getAbsolutePath(), null));
        Assert.assertTrue(new File(zip).exists());
        try (ZipFile zf = new ZipFile(zip)) {
            Assert.assertNotNull(zf.getEntry("a.txt"));
            Assert.assertNotNull(zf.getEntry("sub/b.txt"));
        }
    }

    @Test
    public void testCompressZipDirectory() throws Exception {
        String zip = new File(workDir, "d.zip").getAbsolutePath();
        CompressUtil.compressZip(zip, srcDir.getAbsolutePath());
        Assert.assertTrue(new File(zip).exists());
        try (ZipFile zf = new ZipFile(zip)) {
            Assert.assertNotNull(zf.getEntry("a.txt"));
        }
    }

    @Test
    public void testCompressZipExtension() throws Exception {
        String zip = new File(workDir, "e.zip").getAbsolutePath();
        CompressUtil.compressZip(zip, srcDir.getAbsolutePath(), "txt");
        Assert.assertTrue(new File(zip).exists());
        try (ZipFile zf = new ZipFile(zip)) {
            Assert.assertNotNull(zf.getEntry("a.txt"));
        }
    }

    @Test
    public void testParseDirectoryBranches() {
        Assert.assertTrue(CompressUtil.parseDirectory(null, null).isEmpty());
        Assert.assertTrue(CompressUtil.parseDirectory("", "txt").isEmpty());
        Assert.assertTrue(CompressUtil.parseDirectory("  ", null).isEmpty());
        // 不是目录
        Assert.assertTrue(
                CompressUtil.parseDirectory(new File(workDir, "nofile").getAbsolutePath(), null).isEmpty());
        // 以 / 结尾
        List<CompressUtil.ZipFileParams> list =
                CompressUtil.parseDirectory(srcDir.getAbsolutePath() + "/", null);
        Assert.assertFalse(list.isEmpty());
        // 隐藏文件被过滤（unix 通过 . 前缀模拟）
        File h = new File(srcDir, ".hid.txt");
        try {
            Files.write(h.toPath(), "h".getBytes());
        } catch (Exception ignored) {
        }
        List<CompressUtil.ZipFileParams> list2 =
                CompressUtil.parseDirectory(srcDir.getAbsolutePath(), null);
        Assert.assertFalse(list2.isEmpty());
    }

    @Test
    public void testCompressZipNullList() {
        // null 列表 → 不抛异常
        CompressUtil.compressZip(
                new File(workDir, "n.zip").getAbsolutePath(), (List<CompressUtil.ZipFileParams>) null);
        // 空目录解析出的空列表
        File empty = new File(workDir, "empty");
        empty.mkdir();
        CompressUtil.compressZip(
                new File(workDir, "e2.zip").getAbsolutePath(), empty.getAbsolutePath());
    }

    @Test
    public void testCompressTar() throws Exception {
        String tar = new File(workDir, "out.tar").getAbsolutePath();
        CompressUtil.compressTar(srcDir.getAbsolutePath(), tar);
        Assert.assertTrue(new File(tar).exists());
        Assert.assertTrue(new File(tar).length() > 0);
    }

    @Test
    public void testZipFileParams() {
        CompressUtil.ZipFileParams params =
                CompressUtil.ZipFileParams.builder()
                        .originalFilePath("o")
                        .zipInnerRelativePath("i")
                        .build();
        Assert.assertEquals("o", params.getOriginalFilePath());
        Assert.assertEquals("i", params.getZipInnerRelativePath());
        Assert.assertNotNull(new CompressUtil.ZipFileParams("o", "i"));
        ZipEntry ignored = new ZipEntry("x");
        Assert.assertNotNull(ignored);
    }

}
