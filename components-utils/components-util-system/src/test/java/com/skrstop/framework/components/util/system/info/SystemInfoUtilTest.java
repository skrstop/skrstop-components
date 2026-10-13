package com.skrstop.framework.components.util.system.info;

import cn.hutool.system.OsInfo;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * @author 蒋时华
 */
public class SystemInfoUtilTest {

    private static final String GPU_SAMPLE = "Mon Jun  1 10:47:16 2020\n" +
            "+-----------------------------------------------------------------------------+\n" +
            "| NVIDIA-SMI 418.87.01    Driver Version: 418.87.01    CUDA Version: 10.1     |\n" +
            "|-------------------------------+----------------------+----------------------+\n" +
            "| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |\n" +
            "| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |\n" +
            "|===============================+======================+======================|\n" +
            "|   0  TITAN V             Off  | 00000000:2D:00.0  On |                  N/A |\n" +
            "| 29%   43C    P8    27W / 250W |   1123MiB / 12035MiB |      0%      Default |\n" +
            "+-------------------------------+----------------------+----------------------+\n" +
            "|   1  GeForce RTX 208...  Off  | 00000000:99:00.0 Off |                  N/A |\n" +
            "|  0%   29C    P0    26W / 260W |    11MiB / 10989MiB |      0%      Default |\n" +
            "+-------------------------------+----------------------+----------------------+\n" +
            "\n" +
            "+-----------------------------------------------------------------------------+\n" +
            "| Processes:                                                       GPU Memory |\n" +
            "|  GPU       PID   Type   Process name                             Usage      |\n" +
            "|=============================================================================|\n" +
            "|    0     16841      C   inference_worker                            1077MiB |\n" +
            "+-----------------------------------------------------------------------------+\n";

    private Process mockProcess(String output) throws IOException {
        Process process = Mockito.mock(Process.class);
        Mockito.when(process.getInputStream()).thenReturn(
                new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(process.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        return process;
    }

    private static File fakeSmi;

    @BeforeClass
    public static void installFakeSmi() throws Exception {
        File dir = new File("/Users/jphoebe/.asdf/shims");
        fakeSmi = new File(dir, "nvidia-smi");
        String script = "#!/bin/sh\n" +
                "cat <<'GPUEOF'\n" + GPU_SAMPLE + "GPUEOF\n";
        Files.write(fakeSmi.toPath(), script.getBytes(StandardCharsets.UTF_8));
        fakeSmi.setExecutable(true);
    }

    @AfterClass
    public static void removeFakeSmi() {
        if (fakeSmi != null && fakeSmi.exists()) {
            fakeSmi.delete();
        }
    }

    @Test
    public void testGetNvidiaGPUInfoStr() throws Exception {
        String info = SystemInfoUtil.getNvidiaGPUInfoStr();
        Assert.assertNotNull(info);
        Assert.assertTrue(info.contains("NVIDIA-SMI"));
    }

    @Test
    public void testGetNvidiaGPUInfo() throws Exception {
        List<NvidiaGPUInfo> infos = SystemInfoUtil.getNvidiaGPUInfo();
        Assert.assertEquals(2, infos.size());
        NvidiaGPUInfo first = infos.get(0);
        Assert.assertEquals(Integer.valueOf(0), first.getNumber());
        Assert.assertEquals(Integer.valueOf(29), first.getFan());
        Assert.assertEquals(Integer.valueOf(43), first.getTemperature());
        Assert.assertEquals("C", first.getTemperatureUnit());
        Assert.assertEquals(Integer.valueOf(8), first.getPerformance());
        Assert.assertEquals("W", first.getPowerUnit());
        Assert.assertEquals(Integer.valueOf(27), first.getPowerUsed());
        Assert.assertEquals(Integer.valueOf(250), first.getPowerTotal());
        Assert.assertEquals(Long.valueOf(1123L), first.getMemoryUsed());
        Assert.assertEquals(Long.valueOf(12035L), first.getMemoryTotal());
        Assert.assertEquals(Integer.valueOf(0), first.getGpuUsageRate());
    }

    @Test
    public void testReadGpuInfo() throws Exception {
        String info = SystemInfoUtil.readGpuInfo(mockProcess(GPU_SAMPLE));
        Assert.assertTrue(info.contains("NVIDIA-SMI"));

        try {
            SystemInfoUtil.readGpuInfo(mockProcess(""));
            Assert.fail("should throw");
        } catch (SkrstopRuntimeException ignored) {
        }
    }

    @Test
    public void testCreateGpuProcessBranches() throws Exception {
        OsInfo osInfo = Mockito.mock(OsInfo.class);

        // linux 分支（伪造 nvidia-smi 已在类级别安装）
        Mockito.when(osInfo.isLinux()).thenReturn(true);
        Process p = SystemInfoUtil.createGpuProcess(osInfo);
        Assert.assertNotNull(p);

        // windows 分支（伪造 nvidia-smi.exe）
        Mockito.when(osInfo.isLinux()).thenReturn(false);
        Mockito.when(osInfo.isWindows()).thenReturn(true);
        File dir = new File("/Users/jphoebe/.asdf/shims");
        File fakeExe = new File(dir, "nvidia-smi.exe");
        Files.write(fakeExe.toPath(), GPU_SAMPLE.getBytes(StandardCharsets.UTF_8));
        fakeExe.setExecutable(true);
        try {
            p = SystemInfoUtil.createGpuProcess(osInfo);
            Assert.assertNotNull(p);
        } finally {
            fakeExe.delete();
        }
        // exe 不存在时 exec 抛 IOException → 包装为 SkrstopRuntimeException
        try {
            SystemInfoUtil.createGpuProcess(osInfo);
            Assert.fail("should throw");
        } catch (SkrstopRuntimeException e) {
            Assert.assertTrue(e.getThrowable() instanceof IOException);
        }

        // 不支持的系统
        Mockito.when(osInfo.isWindows()).thenReturn(false);
        Mockito.when(osInfo.toString()).thenReturn("other");
        try {
            SystemInfoUtil.createGpuProcess(osInfo);
            Assert.fail("should throw");
        } catch (SkrstopRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("不支持该系统"));
        }

        // osInfo 为 null
        try {
            SystemInfoUtil.createGpuProcess(null);
            Assert.fail("should throw");
        } catch (SkrstopRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("无法获取系统平台信息"));
        }

        // isMac() 分支
        Mockito.when(osInfo.toString()).thenReturn("mac");
        Mockito.when(osInfo.isMac()).thenReturn(true);
        Process macProcess = SystemInfoUtil.createGpuProcess(osInfo);
        Assert.assertNotNull(macProcess);
    }

    @Test
    public void testParseGpuInfo() {
        List<NvidiaGPUInfo> infos = SystemInfoUtil.parseGpuInfo(GPU_SAMPLE);
        Assert.assertEquals(2, infos.size());
        NvidiaGPUInfo first = infos.get(0);
        Assert.assertEquals(Integer.valueOf(0), first.getNumber());
        Assert.assertEquals(Integer.valueOf(29), first.getFan());
        Assert.assertEquals(Integer.valueOf(43), first.getTemperature());
        Assert.assertEquals("C", first.getTemperatureUnit());
        Assert.assertEquals(Integer.valueOf(8), first.getPerformance());
        Assert.assertEquals("W", first.getPowerUnit());
        Assert.assertEquals(Integer.valueOf(27), first.getPowerUsed());
        Assert.assertEquals(Integer.valueOf(250), first.getPowerTotal());
        Assert.assertEquals(Long.valueOf(1123L), first.getMemoryUsed());
        Assert.assertEquals(Long.valueOf(12035L), first.getMemoryTotal());
        Assert.assertEquals(Integer.valueOf(0), first.getGpuUsageRate());

        // 坏数据走 catch 分支
        List<NvidiaGPUInfo> bad = SystemInfoUtil.parseGpuInfo("no-table-format");
        Assert.assertNotNull(bad);
        Assert.assertTrue(bad.isEmpty());

        // null / 空串
        Assert.assertTrue(SystemInfoUtil.parseGpuInfo(null).isEmpty());
        Assert.assertTrue(SystemInfoUtil.parseGpuInfo("").isEmpty());

        // 表头有但数据行残缺 → 所有解析 catch 分支
        String sep1 = "|===============================+======================+======================|";
        String sep2 = "+-------------------------------+----------------------+----------------------+";
        String broken = sep1 + "\n" + sep2 + "\n\n\n";
        List<NvidiaGPUInfo> brokenList = SystemInfoUtil.parseGpuInfo(broken);
        Assert.assertEquals(1, brokenList.size());
        Assert.assertNull(brokenList.get(0).getName());

        // 空 GPU 列表：只有表头没有数据行
        List<NvidiaGPUInfo> empty = SystemInfoUtil.parseGpuInfo("header |===============================+======================+======================|");
        Assert.assertEquals(0, empty.size());
    }

    @Test
    public void testNvidiaGPUInfoModel() {
        NvidiaGPUInfo info = NvidiaGPUInfo.builder()
                .number(0)
                .name("TITAN V")
                .fan(29)
                .temperature(43)
                .temperatureUnit("C")
                .performance(8)
                .powerUsageRate(10)
                .powerUsableRate(90)
                .powerTotal(250)
                .powerUsed(27)
                .powerUsable(223)
                .powerUnit("W")
                .memoryUsageRate(9)
                .memoryUsableRate(91)
                .memoryTotal(12035L)
                .memoryUsed(1123L)
                .memoryUsable(10912L)
                .memoryUnit("MiB")
                .gpuUsageRate(0)
                .gpuUsableRate(100)
                .build();
        Assert.assertEquals("TITAN V", info.getName());
        Assert.assertEquals(Long.valueOf(10912L), info.getMemoryUsable());
        Assert.assertEquals(Integer.valueOf(90), info.getPowerUsableRate());

        NvidiaGPUInfo allArgs = new NvidiaGPUInfo(1, "n", 1, 1, "C", 1, 1, 1, 1, 1, 1, "W", 1, 1, 1L, 1L, 1L, "MiB", 1, 1);
        Assert.assertEquals("n", allArgs.getName());

        NvidiaGPUInfo empty = new NvidiaGPUInfo();
        Assert.assertNull(empty.getName());
        empty.setName("x");
        Assert.assertEquals("x", empty.getName());
    }

}
