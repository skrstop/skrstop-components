package com.skrstop.framework.components.starter.database.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractBaseEntity implements Serializable {


    private static final long serialVersionUID = -3062937558431776826L;

//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @TableId(type = IdType.ASSIGN_ID)
//    @TableId(type = IdType.AUTO)
//    public Long id;

}
