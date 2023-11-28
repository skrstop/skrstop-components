package com.jphoebe.framework.components.starter.mongodb.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * DO base entity by deleted
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractDeletedBaseEntity<T> extends AbstractBaseEntity<T> {
    private static final long serialVersionUID = 8349145430304784822L;

    /**
     * 软删除标记
     */
    public Boolean deleted = false;


}
