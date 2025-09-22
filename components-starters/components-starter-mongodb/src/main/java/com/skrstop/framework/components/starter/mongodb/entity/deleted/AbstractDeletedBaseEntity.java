package com.skrstop.framework.components.starter.mongodb.entity.deleted;

import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyDeleted;
import com.skrstop.framework.components.starter.mongodb.entity.AbstractBaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * DO base entity by deleted
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractDeletedBaseEntity extends AbstractBaseEntity {
    @Serial
    private static final long serialVersionUID = 8349145430304784822L;

    /**
     * 软删除标记
     */
    @PropertyDeleted
    public Boolean deleted;


}
