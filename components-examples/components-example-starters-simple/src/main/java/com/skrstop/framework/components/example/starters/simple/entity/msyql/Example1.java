package com.skrstop.framework.components.example.starters.simple.entity.msyql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.skrstop.framework.components.starter.database.entity.deleted.AbstractUpdateByTimeDeletedBaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-11-30 19:00:27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Example1 extends AbstractUpdateByTimeDeletedBaseEntity {

    @TableId(type = IdType.AUTO)
    public Long id;

    public String name;

    private Integer age;

    private LocalDateTime birth;

    private Boolean die;

    private Boolean status;


}
