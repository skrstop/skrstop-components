package com.skrstop.framework.components.starter.mongodb.entity.deletedVersion;

import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyDeleted;
import com.skrstop.framework.components.starter.mongodb.entity.version.AbstractCreateByTimeVersionBaseEntity;
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
public abstract class AbstractCreateByTimeDeletedVersionBaseEntity extends AbstractCreateByTimeVersionBaseEntity {


    private static final long serialVersionUID = -4024689623085796268L;

    /**
     * 软删除标记
     */
    @PropertyDeleted
    public Boolean deleted;

}
