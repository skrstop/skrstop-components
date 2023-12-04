package com.zoe.framework.components.starter.mongodb.entity.version;

import com.zoe.framework.components.starter.mongodb.entity.AbstractBaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractVersionBaseEntity<T> extends AbstractBaseEntity<T> {

    private static final long serialVersionUID = -3062937558431776826L;

    private Long version = 0L;


}
