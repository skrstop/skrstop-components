package com.zoe.framework.components.example.starters.entity.mongo;

import com.zoe.framework.components.starter.mongodb.entity.version.AbstractTimeDeletedVersionBaseEntity;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.*;

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
public class Example2Mongo extends AbstractTimeDeletedVersionBaseEntity<Long> {
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
