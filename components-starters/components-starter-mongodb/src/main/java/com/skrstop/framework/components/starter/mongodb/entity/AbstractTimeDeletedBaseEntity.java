package com.skrstop.framework.components.starter.mongodb.entity;

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
public abstract class AbstractTimeDeletedBaseEntity<T> extends AbstractTimeBaseEntity<T> {

    private static final long serialVersionUID = 3937442618809011749L;
    /**
     * 软删除标记
     */
    public Boolean deleted = false;

}
