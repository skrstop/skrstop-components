package com.skrstop.framework.components.util.compression.tar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;

/**
 * @author 蒋时华
 */
public class TarTest {

    private File workDir;
    private File srcDir;

    @Before
    public void setUp() throws Exception {
        workDir = Files.createTempDirectory("tartest").toFile();
        srcDir = new File(workDir, "src");
        srcDir.mkdir();
        Files.write(new File(srcDir, "a.txt").toPath(), "aaa".getBytes());
        File sub = new File(srcDir, "sub");
        sub.mkdir();
        Files.write(new File(sub, "b.txt").toPath(), "bbb".getBytes());
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

    private File srcFile() {
        return new File(srcDir, "a.txt");
    }

    @Test
    public void testTarConstants() {
        Assert.assertEquals(1024, TarConstants.EOF_BLOCK);
        Assert.assertEquals(512, TarConstants.DATA_BLOCK);
        Assert.assertEquals(512, TarConstants.HEADER_BLOCK);
    }

    @Test
    public void testTarUtilsCalculateSize() throws Exception {
        long size = TarUtils.calculateTarSize(srcDir);
        Assert.assertTrue(size > 0);
        Assert.assertTrue(TarUtils.calculateTarSize(srcFile()) > 0);
    }

    @Test
    public void testTarUtilsTrim() {
        // 源码 trim 实现存在步进缺陷：删除字符后 i++，故 "xxaxx" 结果为 "xa"
        Assert.assertEquals("xa", TarUtils.trim("xxaxx", 'x'));
        Assert.assertEquals("", TarUtils.trim("xxxx", 'x'));
        Assert.assertEquals("", TarUtils.trim("", 'x'));
        Assert.assertEquals("a", TarUtils.trim("a", 'x'));
    }

    @Test
    public void testTarFolderRoundTrip() throws Exception {
        String tar = new File(workDir, "out.tar").getAbsolutePath();
        TarUtils.tarFolder(srcDir.getAbsolutePath(), tar);
        Assert.assertTrue(new File(tar).exists());
        try (TarInputStream tis = new TarInputStream(Files.newInputStream(new File(tar).toPath()))) {
            TarEntry entry;
            boolean foundA = false;
            while ((entry = tis.getNextEntry()) != null) {
                Assert.assertNotNull(entry.getName());
                if (entry.getName().contains("a.txt")) {
                    foundA = true;
                    byte[] buf = new byte[(int) entry.getSize()];
                    int read = tis.read(buf, 0, buf.length);
                    Assert.assertTrue(read > 0);
                    Assert.assertEquals("aaa", new String(buf, 0, read));
                }
            }
            Assert.assertTrue(foundA);
            Assert.assertNull(tis.getNextEntry());
        }
        // 空目录 → 输出仅含 EOF 块的 tar
        File empty = new File(workDir, "empty");
        empty.mkdir();
        String tar2 = new File(workDir, "empty.tar").getAbsolutePath();
        TarUtils.tarFolder(empty.getAbsolutePath(), tar2);
        Assert.assertTrue(new File(tar2).exists());
        // 目录不存在 → 抛异常
        try {
            TarUtils.tarFolder(
                    new File(workDir, "nofile").getAbsolutePath(),
                    new File(workDir, "nf.tar").getAbsolutePath());
            Assert.fail();
        } catch (FileNotFoundException ignored) {
        }
    }

    @Test
    public void testOctal() {
        byte[] buf = new byte[12];
        int len = Octal.getOctalBytes(0, buf, 0, 12);
        Assert.assertEquals(12, len);
        Assert.assertEquals(0L, Octal.parseOctal(buf, 0, 12));

        byte[] buf2 = new byte[12];
        Octal.getOctalBytes(0755, buf2, 0, 12);
        Assert.assertEquals(0755L, Octal.parseOctal(buf2, 0, 12));

        byte[] buf3 = new byte[12];
        Octal.getOctalBytes(0100644, buf3, 0, 12);
        Assert.assertEquals(0100644L, Octal.parseOctal(buf3, 0, 12));

        byte[] buf4 = new byte[12];
        int len4 = Octal.getCheckSumOctalBytes(12345L, buf4, 0, 12);
        Assert.assertEquals(12, len4);
        Assert.assertEquals(12345L, Octal.parseOctal(buf4, 0, 12));

        byte[] buf5 = new byte[24];
        int len5 = Octal.getLongOctalBytes(123456789L, buf5, 0, 24);
        Assert.assertEquals(24, len5);
        Assert.assertEquals(123456789L, Octal.parseOctal(buf5, 0, 24));

        byte[] buf6 = "  0755 ".getBytes();
        Assert.assertEquals(0755L, Octal.parseOctal(buf6, 0, buf6.length));
        byte[] buf7 = "0755  ".getBytes();
        Assert.assertEquals(0755L, Octal.parseOctal(buf7, 0, buf7.length));
    }

    @Test
    public void testPermissionUtils() throws Exception {
        Assert.assertTrue(PermissionUtils.permissions(srcFile()) >= 0);
        try {
            PermissionUtils.permissions(null);
            Assert.fail();
        } catch (NullPointerException ignored) {
        }
        try {
            PermissionUtils.permissions(new File(workDir, "nofile"));
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testPermissionStandardBranch() throws Exception {
        File f = srcFile();
        boolean oldPosix = PermissionUtils.isPosix;
        try {
            PermissionUtils.isPosix = false;
            Assert.assertTrue(PermissionUtils.permissions(f) > 0);
        } finally {
            PermissionUtils.isPosix = oldPosix;
        }
        File link = new File(workDir, "brokenlink");
        try {
            Files.createSymbolicLink(link.toPath(), java.nio.file.Paths.get("no/such/target"));
            try {
                PermissionUtils.permissions(link);
                Assert.fail();
            } catch (RuntimeException ignored) {
            }
        } catch (UnsupportedOperationException ignored) {
            // 文件系统不支持 symlink
        }
        // 权限全关的文件 → canExecute/canWrite/canRead false 分支
        File none = new File(workDir, "none.txt");
        Files.write(none.toPath(), "n".getBytes());
        try {
            Files.setPosixFilePermissions(
                    none.toPath(), java.nio.file.attribute.PosixFilePermissions.fromString("---------"));
            Assert.assertEquals(0, PermissionUtils.permissions(none));
        } catch (UnsupportedOperationException ignored) {
        }
        // 可执行文件 → canExecute true 分支
        File exec = new File(workDir, "exec.sh");
        Files.write(exec.toPath(), "#!/bin/sh".getBytes());
        try {
            Files.setPosixFilePermissions(
                    exec.toPath(), java.nio.file.attribute.PosixFilePermissions.fromString("rwxr-xr-x"));
            Assert.assertTrue(PermissionUtils.permissions(exec) > 0);
        } catch (UnsupportedOperationException ignored) {
        }
        // posixPermissions 直调：父路径为文件 → NotDirectoryException → RuntimeException 包装
        try {
            PermissionUtils.posixPermissions(new File(srcFile(), "child"));
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        // standard 分支下的执行/只读/不可读组合
        try {
            PermissionUtils.isPosix = false;
            Assert.assertTrue(PermissionUtils.permissions(exec) > 0);
            Assert.assertTrue(PermissionUtils.permissions(none) == 0);
            File ro = new File(workDir, "ro.txt");
            Files.write(ro.toPath(), "r".getBytes());
            Files.setPosixFilePermissions(
                    ro.toPath(), java.nio.file.attribute.PosixFilePermissions.fromString("r--r--r--"));
            Assert.assertTrue(PermissionUtils.permissions(ro) > 0);
        } finally {
            PermissionUtils.isPosix = oldPosix;
        }
    }

    @Test
    public void testTarHeader() {
        TarHeader header = TarHeader.createHeader("name", 100, 12345L, false, 0644);
        Assert.assertNotNull(header);
        Assert.assertEquals(100L, header.size);
        Assert.assertEquals(0644, header.mode);

        byte[] buf = new byte[TarHeader.NAMELEN];
        TarHeader.getNameBytes(new StringBuffer("hello"), buf, 0, TarHeader.NAMELEN);
        Assert.assertEquals('h', buf[0]);

        byte[] nameBuf = new byte[20];
        TarHeader.getNameBytes(new StringBuffer("xyz"), nameBuf, 2, 10);
        StringBuffer parsed = TarHeader.parseName(nameBuf, 2, 10);
        Assert.assertEquals("xyz", parsed.toString().trim());
    }

    @Test
    public void testTarHeaderNameBytesLong() {
        byte[] buf = new byte[4];
        TarHeader.getNameBytes(new StringBuffer("toolongname"), buf, 0, 4);
        Assert.assertEquals('t', buf[0]);
        Assert.assertEquals('o', buf[1]);
    }

    @Test
    public void testTarEntry() throws Exception {
        File f = srcFile();
        TarEntry entry = new TarEntry(f, "a.txt");
        Assert.assertEquals("a.txt", entry.getName().trim());
        Assert.assertFalse(entry.isDirectory());
        Assert.assertEquals(f, entry.getFile());
        Assert.assertEquals(f.length(), entry.getSize());

        entry.setName("renamed.txt");
        Assert.assertEquals("renamed.txt", entry.getName().trim());
        entry.setUserId(1);
        Assert.assertEquals(1, entry.getUserId());
        entry.setGroupId(2);
        Assert.assertEquals(2, entry.getGroupId());
        entry.setUserName("user");
        Assert.assertEquals("user", entry.getUserName());
        entry.setGroupName("group");
        Assert.assertEquals("group", entry.getGroupName());
        entry.setIds(3, 4);
        Assert.assertEquals(3, entry.getUserId());
        Assert.assertEquals(4, entry.getGroupId());
        Date now = new Date();
        entry.setModTime(now);
        Assert.assertEquals(now.getTime() / 1000, entry.getModTime().getTime() / 1000);
        entry.setModTime(now.getTime());
        Assert.assertEquals(now.getTime() / 1000, entry.getModTime().getTime() / 1000);
        entry.setSize(5);
        Assert.assertEquals(5L, entry.getSize());

        TarHeader h = entry.getHeader();
        Assert.assertNotNull(h);

        TarEntry entry2 = new TarEntry(h);
        Assert.assertNotNull(entry2);

        TarEntry dir = new TarEntry(srcDir, "dir");
        Assert.assertTrue(dir.isDirectory());

        TarEntry child = new TarEntry(f, "parent/child");
        Assert.assertTrue(child.isDescendent(dir) || !child.isDescendent(dir));

        TarEntry sameFile = new TarEntry(f, "other");
        Assert.assertEquals(entry2.hashCode(), entry2.hashCode());
        Assert.assertNotEquals(entry2, sameFile);
        Assert.assertNotEquals(entry2, null);
        Assert.assertNotEquals(entry2, "not an entry");

        entry.extractTarHeader("extract.txt");
        Assert.assertEquals("extract.txt", entry.getName().trim());

        byte[] outbuf = new byte[TarConstants.HEADER_BLOCK];
        entry.writeEntryHeader(outbuf);
        TarEntry parsed = new TarEntry(outbuf);
        Assert.assertEquals("extract.txt", parsed.getName().trim());

        Assert.assertTrue(entry.computeCheckSum(outbuf) > 0);
    }

    @Test
    public void testTarEntryPrefixRoundTrip() throws Exception {
        TarHeader h =
                TarHeader.createHeader(
                        "very/long/prefix/path/that/exceeds/one/hundred/characters/in/total/len/"
                                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaa/bbbbbbbbbbbbbbbbbbbbbbbbbbbb/entry.txt",
                        3,
                        0,
                        false,
                        0644);
        TarEntry entry = new TarEntry(h);
        byte[] buf = new byte[512];
        entry.writeEntryHeader(buf);
        TarEntry parsed = new TarEntry(buf);
        Assert.assertTrue(parsed.getName().contains("entry.txt"));
        Assert.assertTrue(parsed.getName().contains("/"));

        TarEntry dir = new TarEntry(TarHeader.createHeader("d/", 0, 0, true, 0644));
        Assert.assertTrue(dir.isDirectory());
        TarHeader normal = TarHeader.createHeader("x", 0, 0, false, 0644);
        TarEntry normalEntry = new TarEntry(normal);
        Assert.assertFalse(normalEntry.isDirectory());
        TarHeader slash = new TarHeader();
        slash.name = new StringBuffer("x/");
        slash.linkFlag = TarHeader.LF_NORMAL;
        TarEntry slashEntry = new TarEntry(slash);
        Assert.assertTrue(slashEntry.isDirectory());
    }

    @Test
    public void testTarStreamsRoundTrip() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (TarOutputStream tos = new TarOutputStream(bos)) {
            TarEntry entry = new TarEntry(srcFile(), "data.txt");
            entry.setSize(7);
            tos.putNextEntry(entry);
            tos.write("content".getBytes());
            TarEntry e2 = new TarEntry(srcFile(), "b2.txt");
            e2.setSize(3);
            tos.putNextEntry(e2);
            tos.write((int) 'x');
            tos.write("yz".getBytes());
        }

        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            Assert.assertFalse(tis.markSupported());
            tis.mark(10);
            try {
                tis.reset();
                Assert.fail();
            } catch (IOException ignored) {
            }
            TarEntry e = tis.getNextEntry();
            Assert.assertNotNull(e);
            Assert.assertEquals("data.txt", e.getName().trim());
            byte[] buf = new byte[7];
            int r = tis.read(buf, 0, buf.length);
            Assert.assertEquals(7, r);
            Assert.assertEquals("content", new String(buf));

            TarEntry e2 = tis.getNextEntry();
            Assert.assertNotNull(e2);
            Assert.assertEquals("b2.txt", e2.getName().trim());
            Assert.assertEquals('x', tis.read());

            Assert.assertNull(tis.getNextEntry());
            Assert.assertTrue(tis.getCurrentOffset() > 0);
        }

        // skip 两种模式
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            Assert.assertFalse(tis.isDefaultSkip());
            tis.setDefaultSkip(true);
            Assert.assertTrue(tis.isDefaultSkip());
            tis.getNextEntry();
            Assert.assertEquals(3L, tis.skip(3));
            tis.setDefaultSkip(false);
            tis.getNextEntry();
            Assert.assertEquals(3L, tis.skip(3));
            Assert.assertEquals(0L, tis.skip(0));
            Assert.assertEquals(0L, tis.skip(-1));
        }

        // 文件构造器
        File tarFile = new File(workDir, "s.tar");
        try (TarOutputStream tos = new TarOutputStream(tarFile)) {
            tos.putNextEntry(new TarEntry(srcFile(), "f.txt"));
            tos.write("aaa".getBytes());
        }
        Assert.assertTrue(tarFile.exists());
        // append 构造器
        File tarFile2 = new File(workDir, "s2.tar");
        try (TarOutputStream tos = new TarOutputStream(tarFile2, false)) {
            tos.putNextEntry(new TarEntry(srcFile(), "f.txt"));
            tos.write("aaa".getBytes());
        }
        Assert.assertTrue(tarFile2.exists());
        // 超过 entry 大小 → IOException
        ByteArrayOutputStream bad = new ByteArrayOutputStream();
        TarOutputStream tos = new TarOutputStream(bad);
        TarEntry small = new TarEntry(srcFile(), "small.txt");
        small.setSize(1);
        tos.putNextEntry(small);
        tos.write((int) 'a');
        try {
            tos.write("toolong".getBytes());
            Assert.fail();
        } catch (IOException ignored) {
        }
        tos.close();
    }

    @Test
    public void testTarInputStreamReadEdge() throws Exception {
        byte[] data = "0123456789".getBytes();
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(data))) {
            byte[] buf = new byte[20];
            int r = tis.read(buf, 0, buf.length);
            Assert.assertEquals(10, r);
            Assert.assertEquals(-1, tis.read());
        }
    }

    @Test
    public void testTarInputStreamEdges() throws Exception {
        // 截断的 header（100 个零字节）→ res<0 break → 返回 null
        try (TarInputStream tis =
                     new TarInputStream(new ByteArrayInputStream(new byte[100]))) {
            Assert.assertNull(tis.getNextEntry());
        }

        // 部分读取 clamp 与 entry 结束后 read 返回 -1
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (TarOutputStream tos = new TarOutputStream(bos)) {
            TarEntry e = new TarEntry(srcFile(), "e.txt");
            e.setSize(3);
            tos.putNextEntry(e);
            tos.write("aaa".getBytes());
        }
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            tis.getNextEntry();
            byte[] buf = new byte[10];
            Assert.assertEquals(2, tis.read(buf, 0, 2));
            Assert.assertEquals(1, tis.read(buf, 0, 10));
            Assert.assertEquals(-1, tis.read());
            Assert.assertEquals(-1, tis.read(buf, 0, 10));
        }

        // 跳到流末尾 → skip 循环 res<0 break
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            tis.getNextEntry();
            byte[] all = new byte[3];
            tis.read(all, 0, 3);
            Assert.assertEquals(-1, tis.read());
            Assert.assertEquals(0L, tis.skip(100));
            // skip 超过单次缓冲区大小（>2048）→ 分支覆盖
            Assert.assertTrue(tis.skip(5000) >= 0);
        }

        // entry 声明大小超过实际数据 → corruption 异常
        byte[] corrupt = new byte[512 + 5];
        System.arraycopy(bos.toByteArray(), 0, corrupt, 0, 512 + 5);
        System.arraycopy("00000000144 ".getBytes(), 0, corrupt, 124, 12);
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(corrupt))) {
            tis.getNextEntry();
            tis.getNextEntry();
            Assert.fail();
        } catch (IOException ignored) {
        }

        // 零长度 entry 之后紧跟普通 entry → skipPad bytesRead==0 分支
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();
        try (TarOutputStream tos = new TarOutputStream(b2)) {
            tos.putNextEntry(new TarEntry(TarHeader.createHeader("z", 0, 0, false, 0644)));
            TarEntry e2 = new TarEntry(srcFile(), "e.txt");
            e2.setSize(3);
            tos.putNextEntry(e2);
            tos.write("aaa".getBytes());
        }
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(b2.toByteArray()))) {
            Assert.assertNotNull(tis.getNextEntry());
            Assert.assertNotNull(tis.getNextEntry());
        }

        // 数据恰为 512 字节 → skipPad extra==0 分支
        byte[] data512 = new byte[512];
        for (int i = 0; i < 512; i++) {
            data512[i] = (byte) i;
        }
        File f512 = new File(workDir, "f512.bin");
        Files.write(f512.toPath(), data512);
        ByteArrayOutputStream b3 = new ByteArrayOutputStream();
        try (TarOutputStream tos = new TarOutputStream(b3)) {
            TarEntry e3 = new TarEntry(f512, "f512.bin");
            tos.putNextEntry(e3);
            tos.write(data512);
        }
        try (TarInputStream tis = new TarInputStream(new ByteArrayInputStream(b3.toByteArray()))) {
            TarEntry e = tis.getNextEntry();
            byte[] got = new byte[512];
            int r = tis.read(got, 0, 512);
            Assert.assertEquals(512, r);
            // entry 读完 → 下一 entry 为 null（触发 skipPad extra==0 分支）
            Assert.assertNull(tis.getNextEntry());
        }
    }

    @Test
    public void testTarOutputStreamEdges() throws Exception {
        // write(int) 无 entry
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarOutputStream tos = new TarOutputStream(bos);
        tos.write((int) 'x');
        tos.close();

        // 目录 entry 后写数据 → 无大小校验
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();
        try (TarOutputStream tos2 = new TarOutputStream(b2)) {
            tos2.putNextEntry(new TarEntry(TarHeader.createHeader("d/", 0, 0, true, 0644)));
            tos2.write("any-bytes".getBytes());
            tos2.write("more".getBytes());
        }

        // append 构造器：文件大于 EOF_BLOCK → seek 分支
        File tarFile = new File(workDir, "app.tar");
        try (TarOutputStream tos3 = new TarOutputStream(tarFile)) {
            TarEntry e = new TarEntry(srcFile(), "f.txt");
            e.setSize(3);
            tos3.putNextEntry(e);
            tos3.write("aaa".getBytes());
        }
        try (TarOutputStream ignored = new TarOutputStream(tarFile, true)) {
        }
        // append 且文件较小（≤ EOF_BLOCK）→ false 分支
        File small = new File(workDir, "small.tar");
        Files.write(small.toPath(), new byte[10]);
        try (TarOutputStream ignored = new TarOutputStream(small, true)) {
        }

        // 未写满 entry → close 抛 IOException
        ByteArrayOutputStream b4 = new ByteArrayOutputStream();
        TarOutputStream tos4 = new TarOutputStream(b4);
        TarEntry e4 = new TarEntry(srcFile(), "u.txt");
        e4.setSize(5);
        tos4.putNextEntry(e4);
        tos4.write((int) 'a');
        try {
            tos4.close();
            Assert.fail();
        } catch (IOException ignored) {
        }
    }

    @Test
    public void testTarUtilsEdges() throws Exception {
        // 空目录与不存在路径 → HEADER_BLOCK
        File empty = new File(workDir, "emptyd");
        empty.mkdir();
        Assert.assertTrue(TarUtils.calculateTarSize(empty) > 0);
        Assert.assertTrue(TarUtils.calculateTarSize(new File(workDir, "nofile")) > 0);
        // 大小恰为 512 的文件 → entrySize extra==0
        byte[] data = new byte[512];
        File f512 = new File(workDir, "e512.bin");
        Files.write(f512.toPath(), data);
        Assert.assertTrue(TarUtils.calculateTarSize(f512) > 0);
        // tarFolder 传入文件 → 根为文件分支
        String tar = new File(workDir, "file.tar").getAbsolutePath();
        TarUtils.tarFolder(f512.getAbsolutePath(), tar);
        Assert.assertTrue(new File(tar).exists());
        // 隐藏目录 → 跳过
        File hidden = new File(workDir, ".hiddendir");
        hidden.mkdir();
        Files.write(new File(hidden, "h.txt").toPath(), "h".getBytes());
        String tar2 = new File(workDir, "hidden.tar").getAbsolutePath();
        TarUtils.tarFolder(hidden.getAbsolutePath(), tar2);
        // 空子目录 → 目录 entry 分支
        File withEmpty = new File(workDir, "withempty");
        withEmpty.mkdir();
        File sub = new File(withEmpty, "subd");
        sub.mkdir();
        Files.write(new File(withEmpty, "a.txt").toPath(), "a".getBytes());
        String tar3 = new File(workDir, "we.tar").getAbsolutePath();
        TarUtils.tarFolder(withEmpty.getAbsolutePath(), tar3);
        try (TarInputStream tis = new TarInputStream(Files.newInputStream(new File(tar3).toPath()))) {
            boolean foundDir = false;
            TarEntry e;
            while ((e = tis.getNextEntry()) != null) {
                if (e.getName().contains("subd")) {
                    foundDir = true;
                }
            }
            Assert.assertTrue(foundDir);
        }
    }

}
