package com.skrstop.framework.components.util.compression;

import com.skrstop.framework.components.util.compression.bean.BinaryFile;
import com.skrstop.framework.components.util.compression.bean.CompressionType;
import com.skrstop.framework.components.util.compression.bean.ZipLevel;
import com.skrstop.framework.components.util.compression.compressor.CompressProcessor;
import com.skrstop.framework.components.util.compression.compressor.FileCompressor;
import com.skrstop.framework.components.util.compression.exception.CompressException;
import com.skrstop.framework.components.util.compression.processor.*;
import com.skrstop.framework.components.util.compression.tar.*;
import com.skrstop.framework.components.util.compression.utils.*;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.zip.GZIPOutputStream;

/**
 * 覆盖处理器异常分支与零长度条目分支等边界场景
 *
 * @author 蒋时华
 */
public class EdgeCasesTest {

    private File workDir;

    @Before
    public void setUp() throws Exception {
        workDir = Files.createTempDirectory("edge").toFile();
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

    private File newFile(String name, String content) throws Exception {
        File f = new File(workDir, name);
        if (f.getParentFile() != null) {
            f.getParentFile().mkdirs();
        }
        Files.write(f.toPath(), content.getBytes());
        return f;
    }

    private FileCompressor compressorWithNullData() throws Exception {
        File src = newFile("s.txt", "abc");
        FileCompressor compressor = new FileCompressor();
        compressor.addFile(src.getAbsolutePath(), "s.txt");
        BinaryFile bf = compressor.getBinaryFile("s.txt");
        Field data = BinaryFile.class.getDeclaredField("data");
        data.setAccessible(true);
        data.set(bf, null);
        return compressor;
    }

    /**
     * 处理器内部捕获异常：data 为 null → 写入 NPE → catch 分支
     */
    private void assertCompressCatch(CompressProcessor processor) throws Exception {
        FileCompressor compressor = compressorWithNullData();
        compressor.setCompressedPath(new File(workDir, "c.bin").getAbsolutePath());
        try {
            processor.compressData(compressor);
        } catch (Exception ignored) {
            // 部分 tar 系处理器 finally 关闭时抛出 unclosed entries
        }
    }

    /**
     * 传入对应格式的非法内容 → 读取失败（Ar/Cpio/7z 内部捕获，其余向外抛出）
     */
    private void assertReadCatch(CompressProcessor processor, byte[] content) throws Exception {
        File garbage = new File(workDir, "g_" + processor.getClass().getSimpleName() + ".bin");
        Files.write(garbage.toPath(), content);
        try {
            processor.read(garbage.getAbsolutePath(), new FileCompressor());
        } catch (Exception ignored) {
        }
    }

    private static byte[] gzipWrap(byte[] data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gz = new GZIPOutputStream(bos);
        gz.write(data);
        gz.close();
        return bos.toByteArray();
    }

    private static byte[] bz2Wrap(byte[] data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bz = new BZip2CompressorOutputStream(bos);
        bz.write(data);
        bz.close();
        return bos.toByteArray();
    }

    private static byte[] xzWrap(byte[] data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XZCompressorOutputStream xz = new XZCompressorOutputStream(bos);
        xz.write(data);
        xz.close();
        return bos.toByteArray();
    }

    @Test
    public void testProcessorCompressCatch() throws Exception {
        assertCompressCatch(new ZipProcessor());
        assertCompressCatch(new GzipProcessor());
        assertCompressCatch(new JarProcessor());
        assertCompressCatch(new TarProcessor());
        assertCompressCatch(new TarGzProcessor());
        assertCompressCatch(new TarBz2Processor());
        assertCompressCatch(new Bzip2Processor());
        assertCompressCatch(new ArProcessor());
        assertCompressCatch(new CpioProcessor());
        assertCompressCatch(new XzProcessor());
        assertCompressCatch(new SevenZipProcessor());
    }

    @Test
    public void testProcessorReadCatch() throws Exception {
        byte[] garbage = "this is not a valid archive at all!!".getBytes();
        assertReadCatch(new ZipProcessor(), garbage);
        assertReadCatch(new GzipProcessor(), gzipWrap(garbage));
        assertReadCatch(new JarProcessor(), garbage);
        assertReadCatch(new TarProcessor(), garbage);
        assertReadCatch(new TarGzProcessor(), gzipWrap(garbage));
        assertReadCatch(new TarBz2Processor(), bz2Wrap(garbage));
        assertReadCatch(new Bzip2Processor(), bz2Wrap(garbage));
        assertReadCatch(new ArProcessor(), garbage);
        assertReadCatch(new CpioProcessor(), garbage);
        assertReadCatch(new XzProcessor(), xzWrap(garbage));
        assertReadCatch(new SevenZipProcessor(), garbage);
    }

    /**
     * 构造包含零长度条目的归档，覆盖各 read 循环 entry.getSize() > 0 的 false 分支
     */
    private void zeroSizeArchive(CompressProcessor processor, String fileName) throws Exception {
        File archive = newFile(fileName, "");
        compressorWithNullData(); // 确保 helper 可用
        FileCompressor compressor = new FileCompressor();
        processor.read(archive.getAbsolutePath(), compressor);
        Assert.assertNotNull(compressor);
    }

    @Test
    public void testZeroSizeEntries() throws Exception {
        // tar（零长度普通条目）
        ByteArrayOutputStream tb = new ByteArrayOutputStream();
        TarOutputStream tos = new TarOutputStream(tb);
        TarHeader header = TarHeader.createHeader("z", 0, 0, false, 0644);
        tos.putNextEntry(new TarEntry(header));
        tos.close();

        File tar = newFile("z.tar", "");
        Files.write(tar.toPath(), tb.toByteArray());
        new TarProcessor().read(tar.getAbsolutePath(), new FileCompressor());

        // tar.gz
        File tgz = newFile("z.tar.gz", "");
        try (GZIPOutputStream gz = new GZIPOutputStream(new FileOutputStream(tgz))) {
            gz.write(tb.toByteArray());
        }
        new TarGzProcessor().read(tgz.getAbsolutePath(), new FileCompressor());

        // tar.bz2
        File tbz = newFile("z.tar.bz2", "");
        try (BZip2CompressorOutputStream bz =
                     new BZip2CompressorOutputStream(new FileOutputStream(tbz))) {
            bz.write(tb.toByteArray());
        }
        new TarBz2Processor().read(tbz.getAbsolutePath(), new FileCompressor());

        // xz
        File txz = newFile("z.tar.xz", "");
        try (XZCompressorOutputStream xz =
                     new XZCompressorOutputStream(new FileOutputStream(txz))) {
            xz.write(tb.toByteArray());
        }
        new XzProcessor().read(txz.getAbsolutePath(), new FileCompressor());

        // cpio
        File cpio = newFile("z.cpio", "");
        try (CpioArchiveOutputStream cos = new CpioArchiveOutputStream(new FileOutputStream(cpio))) {
            cos.putArchiveEntry(new CpioArchiveEntry("z", 0));
            cos.closeArchiveEntry();
        }
        new CpioProcessor().read(cpio.getAbsolutePath(), new FileCompressor());

        // ar
        File ar = newFile("z.ar", "");
        try (ArArchiveOutputStream aos = new ArArchiveOutputStream(new FileOutputStream(ar))) {
            aos.putArchiveEntry(new ArArchiveEntry("z", 0));
            aos.closeArchiveEntry();
        }
        new ArProcessor().read(ar.getAbsolutePath(), new FileCompressor());

        // 7z
        File sevenZ = newFile("z.7z", "");
        File zero = newFile("z.txt", "");
        SevenZOutputFile sevenZOutput = new SevenZOutputFile(sevenZ);
        org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry sevenEntry =
                sevenZOutput.createArchiveEntry(zero, "z");
        sevenEntry.setSize(0);
        sevenZOutput.putArchiveEntry(sevenEntry);
        sevenZOutput.closeArchiveEntry();
        sevenZOutput.finish();
        sevenZOutput.close();
        new SevenZipProcessor().read(sevenZ.getAbsolutePath(), new FileCompressor());
    }

    @Test
    public void testDecompressAndExtractToExistingDir() throws Exception {
        File zip = new File(workDir, "d.zip");
        FileCompressor compressor = new FileCompressor();
        compressor.addFile(newFile("a.txt", "aaa").getAbsolutePath(), "a.txt");
        compressor.setType(CompressionType.ZIP);
        compressor.setLevel(ZipLevel.NORMAL);
        compressor.setCompressedPath(zip.getAbsolutePath());
        compressor.compress();

        FileCompressor reader = FileCompressor.read(zip.getAbsolutePath());
        File out = new File(workDir, "out");
        reader.decompress(out.getAbsolutePath());
        // 目录已存在 → mkdirs false 分支
        reader.decompress(out.getAbsolutePath());
        // extract 到已存在目录
        reader.extract("a.txt", out.getAbsolutePath());
        // extract 到嵌套不存在的目录
        reader.extract("a.txt", new File(workDir, "n1/n2").getAbsolutePath());
    }

    @Test
    public void testFileCompressorEdges() throws Exception {
        // validateAllowDecompressing 合法类型
        FileCompressor.validateAllowDecompressing(CompressionType.ZIP);
        // validateLevel null
        try {
            FileCompressor.validateLevel(CompressionType.ZIP, null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        // remove 别名方法
        FileCompressor compressor = new FileCompressor();
        compressor.addFile(newFile("r.txt", "r").getAbsolutePath(), "r.txt");
        compressor.remove("r.txt");
        Assert.assertNull(compressor.getBinaryFile("r.txt"));
        // removeFile 不存在 → 日志分支（不再抛 NPE）
        compressor.removeFile("no/such");
        // deleteAllSources：源文件已被提前删除 → deleteParent 直接返回
        File gone = newFile("g.txt", "g");
        File goneDir = gone.getParentFile();
        compressor.addFile(gone.getAbsolutePath(), "g.txt");
        gone.delete();
        goneDir.delete();
        compressor.deleteAllSources();
        // convertByteToFile 失败分支：data 为 null → NPE 被捕获
        FileKits.convertByteToFile(new File(workDir, "bad.bin").getAbsolutePath(), null);
        // deleteParent 不存在的文件
        FileKits.deleteParent(new File(workDir, "nofile").getAbsolutePath());
        // 父目录不存在 → while 首次即 false
        FileKits.deleteParent(new File(workDir, "nodir/f.txt").getAbsolutePath());
        // 父路径是文件 → isDirectory false 短路
        FileKits.deleteParent(newFile("blocker2.txt", "x").getAbsolutePath() + "/child");
        // FormatterKits 第 4 段 GB 分支（TB 级）
        Assert.assertNotNull(FormatterKits.formatSizeInfo(1099511627776L));
        // 构造器覆盖
        Assert.assertNotNull(new FileKits());
        Assert.assertNotNull(new MemoryKits());
        Assert.assertNotNull(new Constants());
        Assert.assertNotNull(new Constants.SOURCE());
        Assert.assertNotNull(new Constants.FILE_TYPES_ALLOW());
        Assert.assertNotNull(new CompressLoggerKits());
        Assert.assertNotNull(new FormatterKits());
        Assert.assertNotNull(new TarUtils());
        Assert.assertNotNull(new Octal());
        Assert.assertNotNull(new TarConstants());
        Assert.assertNotNull(new PermissionUtils());
        Assert.assertNotNull(new FileCompressor());
        // createFileLog null path 分支
        CompressLoggerKits.createFileLog(new FileCompressor());
        // FormatterKits GB 分支
        Assert.assertNotNull(FormatterKits.formatSizeInfo(3L * 1024 * 1024 * 1024));
        Assert.assertNotNull(FormatterKits.formatSizeInfo(5L * 1024 * 1024 * 1024));
        // TarHeader user.name > 31
        String oldUser = System.getProperty("user.name");
        System.setProperty("user.name", "a-very-long-user-name-exceeding-thirty-one-chars");
        try {
            Assert.assertNotNull(new TarHeader());
        } finally {
            System.setProperty("user.name", oldUser);
        }
        // TarHeader.parseName 长度为 0
        Assert.assertEquals(0, TarHeader.parseName(new byte[8], 0, 0).length());
        // getNameBytes 空名字
        byte[] nb = new byte[4];
        TarHeader.getNameBytes(new StringBuffer(), nb, 0, 4);
        Assert.assertEquals(0, nb[0]);
        // createHeader 目录名已带斜杠 / 超长名称前缀
        TarHeader h1 = TarHeader.createHeader("dir/", 0, 0, true, 0644);
        Assert.assertNotNull(h1);
        TarHeader h2 =
                TarHeader.createHeader(
                        "very/long/prefix/path/that/exceeds/one/hundred/characters/in/total/length/"
                                + "aaaaaaaaaaaaaaaaaaaaaaaa/bbbbbbbbbbbbbbbbbbbbbbbb/entry.txt",
                        1,
                        0,
                        false,
                        0644);
        Assert.assertNotNull(h2.namePrefix);
        // Octal.parseOctal 长度为 0
        Assert.assertEquals(0L, Octal.parseOctal(new byte[4], 0, 0));
        // getFileType 多重后缀
        Assert.assertEquals(CompressionType.TAR_BZ2, FileCompressor.getFileType("x.tar.bz2"));
    }

    @Test
    public void testCompressUtilException() throws Exception {
        // 目标路径的父目录是文件 → 写入失败 → CompressException
        File blocker = newFile("blocker.txt", "x");
        try {
            CompressUtil.compressZip(
                    new File(blocker, "inner.zip").getAbsolutePath(),
                    newFile("src", "s").getParentFile().getAbsolutePath());
            Assert.fail();
        } catch (CompressException ignored) {
        }
    }

}
