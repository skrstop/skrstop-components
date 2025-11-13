package com.skrstop.framework.components.example.starters.simple.entity.mongo;

import com.skrstop.framework.components.starter.mongodb.entity.deletedVersion.AbstractUpdateByTimeDeletedVersionBaseEntity;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-12-04 14:54:20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(useDiscriminator = false)
//@Indexes({
//        @Index(fields = @Field(value = "val_int", type = IndexType.DESC)),
//})
public class Example2Mongo extends AbstractUpdateByTimeDeletedVersionBaseEntity {
    @Serial
    private static final long serialVersionUID = 2219449530545654221L;


    @Id
    private Long id;

    private String valStr;
    private Integer valInt;
    private Long valLong;
    private Boolean valBol;
    private LocalDateTime valData;
    private ExampleMongoChild child;

}
