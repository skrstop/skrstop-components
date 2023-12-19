package com.skrstop.framework.components.util.system.info;

import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author 蒋时华
 * @date 2021-08-11 15:00:14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class NvidiaGPUInfo extends SerializableBean {
    private static final long serialVersionUID = -8328058147701190635L;

    private Integer number;
    private String name;

    // 风扇转速
    private Integer fan;

    // 温度
    private Integer temperature;
    private String temperatureUnit;

    // 性能 0 ~ 12
    private Integer performance;

    // 功率使用率
    private Integer powerUsageRate;
    // 功率可使用率
    private Integer powerUsableRate;
    // 总功率
    private Integer powerTotal;
    // 已使用功率
    private Integer powerUsed;
    // 可使用功率
    private Integer powerUsable;
    // 功率单位
    private String powerUnit;

    // 显存使用率
    private Integer memoryUsageRate;
    // 显存可使用率
    private Integer memoryUsableRate;
    // 总显存
    private Long memoryTotal;
    // 已使用显存
    private Long memoryUsed;
    // 可使用显存
    private Long memoryUsable;
    // 显存单位
    private String memoryUnit;

    // gpu使用率
    private Integer gpuUsageRate;
    // gpu可使用率
    private Integer gpuUsableRate;

}
