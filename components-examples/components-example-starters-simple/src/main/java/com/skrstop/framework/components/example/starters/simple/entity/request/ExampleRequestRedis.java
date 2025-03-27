package com.skrstop.framework.components.example.starters.simple.entity.request;

import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author 蒋时华
 * @date 2023-12-04 14:26:57
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ExampleRequestRedis extends SerializableBean {

    private String valStr;
    private Integer valInt;
    private Long valLong;
    private Boolean valBol;
    private LocalDateTime valData;
    private LocalDate valData2;
    private LocalTime valData3;
    private BigDecimal valDecimal;

}
