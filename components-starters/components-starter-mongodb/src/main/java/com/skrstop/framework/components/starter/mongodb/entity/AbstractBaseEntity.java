package com.skrstop.framework.components.starter.mongodb.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = -3062937558431776826L;

//    @Id
//    public T id;

}
