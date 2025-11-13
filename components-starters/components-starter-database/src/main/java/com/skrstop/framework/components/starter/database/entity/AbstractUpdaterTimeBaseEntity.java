package com.skrstop.framework.components.starter.database.entity;

import com.skrstop.framework.components.starter.database.annotation.property.PropertyCreator;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyUpdater;
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
public abstract class AbstractUpdaterTimeBaseEntity extends AbstractUpdateByTimeBaseEntity {

    @Serial
    private static final long serialVersionUID = -4024689623085796268L;

    /**
     * 新增人名字
     */
    @PropertyCreator
    private String creator;

    /**
     * 更新人名字
     */
    @PropertyUpdater
    private String updater;

}
