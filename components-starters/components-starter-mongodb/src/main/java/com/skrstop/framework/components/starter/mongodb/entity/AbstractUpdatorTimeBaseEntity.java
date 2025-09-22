package com.skrstop.framework.components.starter.mongodb.entity;

import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyCreator;
import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyUpdater;
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
public abstract class AbstractUpdatorTimeBaseEntity extends AbstractUpdateByTimeBaseEntity {


    private static final long serialVersionUID = -4024689623085796268L;

    /**
     * 更新人名字
     */
    @PropertyCreator
    private String updater;

    /**
     * 更新人名字
     */
    @PropertyUpdater
    private String creator;

}
