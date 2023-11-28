package cn.auntec.framework.components.util.system.info;

import cn.auntec.framework.components.core.exception.AuntecException;
import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.util.value.data.BigDecimalUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
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
public class SystemInfoUtil extends SystemUtil {

    public static List<NvidiaGPUInfo> getNvidiaGPUInfo() throws IOException, AuntecException {
        String gpus = getNvidiaGPUInfoStr();
        // 命令行调用后获取的信息
//        String gpus = "Mon Jun  1 10:47:16 2020       \n" +
//                "+-----------------------------------------------------------------------------+\n" +
//                "| NVIDIA-SMI 418.87.01    Driver Version: 418.87.01    CUDA Version: 10.1     |\n" +
//                "|-------------------------------+----------------------+----------------------+\n" +
//                "| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |\n" +
//                "| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |\n" +
//                "|===============================+======================+======================|\n" +
////                "|   0  TITAN V             Off  | 00000000:2D:00.0  On |                  N/A |\n" +
////                "| 29%   43C    P8    27W / 250W |   1123MiB / 12035MiB |      0%      Default |\n" +
////                "+-------------------------------+----------------------+----------------------+\n" +
////                "|   1  GeForce RTX 208...  Off  | 00000000:99:00.0 Off |                  N/A |\n" +
////                "|  0%   29C    P8    20W / 260W |     11MiB / 10989MiB |      0%      Default |\n" +
//                "|   0  Tesla P100-PCIE...  On   | 00000000:00:08.0 Off |                    0 |\n" +
//                "| N/A   31C    P0    26W / 250W |      0MiB / 16280MiB |      0%      Default |\n" +
//                "+-------------------------------+----------------------+----------------------+\n" +
//                "                                                                               \n" +
//                "+-----------------------------------------------------------------------------+\n" +
//                "| Processes:                                                       GPU Memory |\n" +
//                "|  GPU       PID   Type   Process name                             Usage      |\n" +
//                "|=============================================================================|\n" +
//                "|    0     16841      C   inference_worker                            1077MiB |\n" +
//                "|    0     19996      G   /usr/lib/xorg/Xorg                            33MiB |\n" +
//                "+-----------------------------------------------------------------------------+\n";
        // 分割无用信息信息
        String[] split = gpus.split("\\|===============================\\+======================\\+======================\\|");
        String[] gpusInfoStr = split[1].split("                                                                               ");
        // 分割多个gpu
        String[] gpuInfo = gpusInfoStr[0].split("\\+-------------------------------\\+----------------------\\+----------------------\\+");
        List<NvidiaGPUInfo> gpuInfoList = new ArrayList<>();
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
                    if (j > 1 && j != split1.length) {
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
    public static String getNvidiaGPUInfoStr() throws AuntecException, IOException {
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (ObjectUtil.isNull(osInfo)) {
            throw new AuntecRuntimeException("无法获取系统平台信息");
        }
        Process process;
        try {
            if (osInfo.isWindows()) {
                process = Runtime.getRuntime().exec("nvidia-smi.exe");
            } else if (osInfo.isLinux()
                    || osInfo.isMacOsX()
                    || osInfo.isMac()) {
                String[] shell = {"/bin/bash", "-c", "nvidia-smi"};
                process = Runtime.getRuntime().exec(shell);
            } else {
                throw new AuntecRuntimeException("不支持该系统：" + osInfo.toString());
            }
            process.getOutputStream().close();
        } catch (IOException e) {
            throw new AuntecException("显卡不存在或获取显卡信息失败", e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder tempStr = new StringBuilder();
        String line = "";
        while (null != (line = reader.readLine())) {
            tempStr.append(line)
                    .append("\n");
        }
        String info = tempStr.toString();
        if (StrUtil.isBlank(info)) {
            throw new AuntecException("显卡不存在或获取显卡信息失败");
        }
        return info;
    }

}
