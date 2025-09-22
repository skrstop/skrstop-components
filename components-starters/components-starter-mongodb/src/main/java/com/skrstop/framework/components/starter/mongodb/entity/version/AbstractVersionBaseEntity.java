package com.skrstop.framework.components.starter.mongodb.entity.version;

import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyVersion;
import com.skrstop.framework.components.starter.mongodb.entity.AbstractBaseEntity;
import dev.morphia.annotations.Version;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractVersionBaseEntity extends AbstractBaseEntity {

    @Serial
    private static final long serialVersionUID = -3062937558431776826L;

    @PropertyVersion
    @Version
    private Long version;

}
