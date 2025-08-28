package com.skrstop.framework.components.example.starters.simple.entity.mongo;

import dev.morphia.annotations.Entity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Entity(useDiscriminator = false)
public class ExampleMongoChild implements Serializable {

    private String valStr;
    private Integer valInt;
    private Long valLong;
    private Boolean valBol;
    private LocalDateTime valData;
}
