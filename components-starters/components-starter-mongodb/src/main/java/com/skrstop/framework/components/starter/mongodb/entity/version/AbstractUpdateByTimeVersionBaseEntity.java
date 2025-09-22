package com.skrstop.framework.components.starter.mongodb.entity.version;

import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyVersion;
import com.skrstop.framework.components.starter.mongodb.entity.AbstractUpdateByTimeBaseEntity;
import dev.morphia.annotations.Version;
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
public abstract class AbstractUpdateByTimeVersionBaseEntity extends AbstractUpdateByTimeBaseEntity {


    private static final long serialVersionUID = -4024689623085796268L;

    @PropertyVersion
    @Version
    private Long version;

}
