package com.skrstop.framework.components.util.system.info;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.util.value.data.BigDecimalUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2021-08-11 14:43:20
 */
@Slf4j
@UtilityClass
public class SystemInfoUtil extends SystemUtil {

    public static List<NvidiaGPUInfo> getNvidiaGPUInfo() throws IOException, SkrstopRuntimeException {
        String gpus = getNvidiaGPUInfoStr();
        return parseGpuInfo(gpus);
    }

    static List<NvidiaGPUInfo> parseGpuInfo(String gpus) {
        List<NvidiaGPUInfo> gpuInfoList = new ArrayList<>();
        if (gpus == null || gpus.isEmpty()) {
            return gpuInfoList;
        }
        // 分割无用信息信息
        String[] split = gpus.split("\\|===============================\\+======================\\+======================\\|");
        if (split.length < 2) {
            // 非 nvidia-smi 标准输出格式
            return gpuInfoList;
        }
        String[] gpusInfoStr = split[1].split("                                                                               ");
        // 分割多个gpu
        String[] gpuInfo = gpusInfoStr[0].split("\\+-------------------------------\\+----------------------\\+----------------------\\+");
        for (int i = 0; i < gpuInfo.length - 1; i++) {
            NvidiaGPUInfo info = new NvidiaGPUInfo();
            String[] nameAndInfo = gpuInfo[i].split("\n");
            try {
                // 基础信息
                String[] split1 = nameAndInfo[1]
                        // 0  TITAN V             Off
                        .split("\\|")[1]
                        // 去空格
                        .split("\\s+");
                info.setNumber(Integer.parseInt(split1[1]));
                StringBuffer name = new StringBuffer();
                for (int j = 0; j < split1.length - 1; j++) {
                    if (j > 1) {
                        name.append(split1[j] + " ");
                    }
                }
                info.setName(name.toString());
            } catch (Exception e) {
                log.error("获取gpu基础信息错误", e);
                // 获取信息错误
            }

            try {
                String[] temp = nameAndInfo[2].split("\\|")[1].split("\\s+");
                // 风扇
                info.setFan(Integer.parseInt(temp[1].split("%")[0]));
            } catch (Exception e) {
                log.error("获取gpu风扇信息错误", e);
                // 获取信息错误
            }
            try {
                String[] temp = nameAndInfo[2].split("\\|")[1].split("\\s+");
                // 温度
                info.setTemperature(Integer.parseInt(temp[2].split("C")[0]));
                info.setTemperatureUnit("C");
            } catch (Exception e) {
                log.error("获取gpu温度信息错误", e);
                // 获取信息错误
            }
            try {
                String[] temp = nameAndInfo[2].split("\\|")[1].split("\\s+");
                // 性能
                info.setPerformance(Integer.parseInt(temp[3].split("P")[1]));
            } catch (Exception e) {
                log.error("获取gpu性能信息错误", e);
                // 获取信息错误
            }
            try {
                String[] temp = nameAndInfo[2].split("\\|")[1].split("\\s+");
                // 功率
                info.setPowerUnit("W");
                info.setPowerUsed(Integer.parseInt(temp[4].split("W")[0]));
                info.setPowerTotal(Integer.parseInt(temp[6].split("W")[0]));
                info.setPowerUsable(info.getPowerTotal() - info.getPowerUsed());
                info.setPowerUsageRate((int) (Float.parseFloat(BigDecimalUtil.divideScaleHalfUp(info.getPowerUsed().toString(), info.getPowerTotal().toString())) * 100));
                info.setPowerUsableRate(100 - info.getPowerUsageRate());
            } catch (Exception e) {
                log.error("获取gpu基础信息错误", e);
                // 获取信息错误
            }
            try {
                // 显存信息
                String[] memoryInfo = nameAndInfo[2].split("\\|")[2].split("\\s+");
                String unit = "MiB";
                info.setMemoryUnit(unit);
                info.setMemoryTotal(Long.parseLong(memoryInfo[3].split(unit)[0]));
                info.setMemoryUsed(Long.parseLong(memoryInfo[1].split(unit)[0]));
                info.setMemoryUsable(info.getMemoryTotal() - info.getMemoryUsed());
                info.setMemoryUsageRate((int) (Float.parseFloat(BigDecimalUtil.divideScaleHalfUp(info.getMemoryUsed().toString(), info.getMemoryTotal().toString())) * 100));
                info.setMemoryUsableRate(100 - info.getMemoryUsageRate());
            } catch (Exception e) {
                log.error("获取gpu显存信息错误", e);
                // 获取信息错误
            }
            try {
                // gpu
                String[] gpuUseInfo = nameAndInfo[2].split("\\|")[3].split("\\s+");
                info.setGpuUsageRate(Integer.parseInt(gpuUseInfo[1].split("%")[0]));
                info.setGpuUsableRate(100 - info.getGpuUsageRate());
            } catch (Exception e) {
                log.error("获取gpu基础信息错误", e);
                // 获取信息错误
            }
            gpuInfoList.add(info);
        }
        return gpuInfoList;
    }

    /**
     * 获取英伟达显卡信息
     *
     * @return
     * @throws IOException
     */
    public static String getNvidiaGPUInfoStr() throws SkrstopRuntimeException, IOException {
        return readGpuInfo(createGpuProcess(SystemUtil.getOsInfo()));
    }

    static Process createGpuProcess(OsInfo osInfo) throws IOException, SkrstopRuntimeException {
        if (ObjectUtil.isNull(osInfo)) {
            throw new SkrstopRuntimeException("无法获取系统平台信息");
        }
        try {
            if (osInfo.isWindows()) {
                Process windowsProcess = Runtime.getRuntime().exec("nvidia-smi.exe");
                windowsProcess.getOutputStream().close();
                return windowsProcess;
            } else if (osInfo.isLinux()
                    || osInfo.isMacOsX()
                    || osInfo.isMac()) {
                String[] shell = {"/bin/bash", "-c", "nvidia-smi"};
                Process process = Runtime.getRuntime().exec(shell);
                process.getOutputStream().close();
                return process;
            } else {
                throw new SkrstopRuntimeException("不支持该系统：" + osInfo.toString());
            }
        } catch (IOException e) {
            throw new SkrstopRuntimeException("显卡不存在或获取显卡信息失败", e);
        }
    }

    static String readGpuInfo(Process process) throws SkrstopRuntimeException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder tempStr = new StringBuilder();
        String line = "";
        while (null != (line = reader.readLine())) {
            tempStr.append(line)
                    .append("\n");
        }
        String info = tempStr.toString();
        if (StrUtil.isBlank(info)) {
            throw new SkrstopRuntimeException("显卡不存在或获取显卡信息失败");
        }
        return info;
    }

}
