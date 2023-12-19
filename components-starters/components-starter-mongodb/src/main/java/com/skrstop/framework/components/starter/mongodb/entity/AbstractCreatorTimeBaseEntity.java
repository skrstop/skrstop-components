package com.skrstop.framework.components.starter.mongodb.entity;

import com.skrstop.framework.components.starter.mongodb.entity.expand.CreatorExpand;
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
public abstract class AbstractCreatorTimeBaseEntity<T> extends AbstractCreateTimeBaseEntity<T> implements CreatorExpand {

    private static final long serialVersionUID = -4024689623085796268L;

    /**
     * 更新人名字
     */
    private String creator;

}
