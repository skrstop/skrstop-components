package com.skrstop.framework.components.example.starters.simple.entity.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-12-01 17:48:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ExampleRequestParam {

    @NotBlank(message = "4##valStr不能为空")
    private String valStr;

    @NotNull(message = "3##valInt不能为空")
    @Min(value = 1, message = "valInt不能小于1")
    private Integer valInt;

    @NotNull(message = "2##valLong不能为空")
    @Max(value = 100, message = "valLong不能大于100")
    private Long valLong;

    @NotNull(message = "1##valBool不能为空")
    private Boolean valBool;

    private LocalDateTime valDateTime;

}
