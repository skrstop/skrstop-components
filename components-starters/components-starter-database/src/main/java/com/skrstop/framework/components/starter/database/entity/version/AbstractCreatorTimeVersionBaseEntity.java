package com.skrstop.framework.components.starter.database.entity.version;

import com.baomidou.mybatisplus.annotation.Version;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyVersion;
import com.skrstop.framework.components.starter.database.entity.AbstractCreatorTimeBaseEntity;
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
public abstract class AbstractCreatorTimeVersionBaseEntity extends AbstractCreatorTimeBaseEntity {

    @Serial
    private static final long serialVersionUID = -4024689623085796268L;

    @PropertyVersion
    @Version
    private Long version;

}
