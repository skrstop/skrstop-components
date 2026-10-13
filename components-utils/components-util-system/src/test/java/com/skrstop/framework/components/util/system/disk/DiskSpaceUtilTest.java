package com.skrstop.framework.components.util.system.disk;

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;

/**
 * @author 蒋时华
 */
public class DiskSpaceUtilTest {

    @Test
    public void testCleanOnNonLinux() throws Exception {
        // 非 linux 系统直接跳过
        DiskSpaceUtil.cleanLinuxDiskCapacity("/tmp", 0L, true);
    }

    @Test
    public void testConstructor() {
        new DiskSpaceUtil();
    }

    @Test
    public void testCleanLinuxWithEnoughSpace() throws Exception {
        File dir = Files.createTempDirectory("disk-test").toFile();
        File f1 = new File(dir, "a.txt");
        Files.write(f1.toPath(), "data".getBytes());

        OsInfo osInfo = Mockito.mock(OsInfo.class);
        Mockito.when(osInfo.isLinux()).thenReturn(true);

        try (MockedStatic<SystemUtil> sysMock = Mockito.mockStatic(SystemUtil.class)) {
            sysMock.when(SystemUtil::getOsInfo).thenReturn(osInfo);
            // 空间阈值设为极小值，可用空间充足 → 直接返回
            DiskSpaceUtil.cleanLinuxDiskCapacity(dir.getAbsolutePath(), 0L, true);
            // 空间阈值极大 → 清理后仍不足 → 抛异常
            try {
                DiskSpaceUtil.cleanLinuxDiskCapacity(dir.getAbsolutePath(), Long.MAX_VALUE / 2, true);
                Assert.fail("should throw");
            } catch (SkrstopRuntimeException ignored) {
            }
        }
    }

    private static void withLinux(MockedStatic<SystemUtil> sysMock, Runnable action) {
        OsInfo osInfo = Mockito.mock(OsInfo.class);
        Mockito.when(osInfo.isLinux()).thenReturn(true);
        sysMock.when(SystemUtil::getOsInfo).thenReturn(osInfo);
        action.run();
    }

    @Test
    public void testCleanLinuxEmptyDir() throws Exception {
        try (MockedStatic<SystemUtil> sysMock = Mockito.mockStatic(SystemUtil.class)) {
            withLinux(sysMock, () -> {
                // 目录不存在 → listFiles 返回 null → 抛异常
                try {
                    DiskSpaceUtil.cleanLinuxDiskCapacity("/tmp/not-exist-dir-" + System.nanoTime(), Long.MAX_VALUE / 2, true);
                    Assert.fail("should throw");
                } catch (SkrstopRuntimeException e) {
                    Assert.assertTrue(e.getMessage().contains("本地缓存文件所占空间为0"));
                }
                // 目录存在但为空 → files.length == 0 → 抛异常
                try {
                    String emptyDir;
                    try {
                        emptyDir = Files.createTempDirectory("disk-empty2").toFile().getAbsolutePath();
                    } catch (java.io.IOException ioe) {
                        throw new AssertionError(ioe);
                    }
                    DiskSpaceUtil.cleanLinuxDiskCapacity(emptyDir, Long.MAX_VALUE / 2, true);
                    Assert.fail("should throw");
                } catch (SkrstopRuntimeException e) {
                    Assert.assertTrue(e.getMessage().contains("本地缓存文件所占空间为0"));
                }
            });
        }
    }

    @Test
    public void testCleanLinuxCleanupFailWithSortAndDelete() throws Exception {
        File dir = Files.createTempDirectory("disk-test-clean").toFile();
        File f1 = new File(dir, "a.txt");
        File f2 = new File(dir, "b.txt");
        Files.write(f1.toPath(), "data".getBytes());
        Files.write(f2.toPath(), "data".getBytes());
        try (MockedStatic<SystemUtil> sysMock = Mockito.mockStatic(SystemUtil.class)) {
            withLinux(sysMock, () -> {
                // 可用空间不会超过极大阈值 → 走完排序/删除循环后抛"清理失败"
                try {
                    DiskSpaceUtil.cleanLinuxDiskCapacity(dir.getAbsolutePath(), Long.MAX_VALUE / 2, true);
                    Assert.fail("should throw");
                } catch (SkrstopRuntimeException e) {
                    Assert.assertTrue(e.getMessage().contains("本地磁盘空间不足，本地缓存文件所占空间为"));
                }
                Assert.assertFalse(f1.exists());
                Assert.assertFalse(f2.exists());
            });
        }
    }

    @Test
    public void testCleanLinuxCleanupSuccess() throws Exception {
        File dir = Files.createTempDirectory("disk-test-success").toFile();
        File f1 = new File(dir, "a.txt");
        Files.write(f1.toPath(), "data".getBytes());
        try (MockedStatic<SystemUtil> sysMock = Mockito.mockStatic(SystemUtil.class)) {
            withLinux(sysMock, () -> {
                // 初始 10MB，删除文件后 200MB，阈值 100MB → 中断循环并清理成功
                long[] usable = {10L, 200L};
                try {
                    DiskSpaceUtil.cleanLinuxDiskCapacity(dir.getAbsolutePath(), 100L, true, () -> {
                        long v = usable[0];
                        usable[0] = usable[1];
                        return v;
                    });
                } catch (SkrstopRuntimeException e) {
                    throw new AssertionError(e);
                }
                Assert.assertFalse(f1.exists());
            });
        }
    }

    @Test
    public void testGetSortTime() throws Exception {
        File dir = Files.createTempDirectory("disk-test-sort").toFile();
        File real = new File(dir, "real.txt");
        Files.write(real.toPath(), "data".getBytes());

        // 正常文件：返回创建时间
        long t = DiskSpaceUtil.getSortTime(real, true);
        Assert.assertTrue(t > 0);
        // 不存在的文件：ignoreDeleteException=true → 返回 -1
        Assert.assertEquals(-1L, DiskSpaceUtil.getSortTime(new File(dir, "not-exist"), true));
        // 不存在的文件：ignoreDeleteException=false → 抛出异常
        try {
            DiskSpaceUtil.getSortTime(new File(dir, "not-exist"), false);
            Assert.fail("should throw");
        } catch (SkrstopRuntimeException e) {
            Assert.assertNotNull(e.getThrowable());
        }
    }

}
