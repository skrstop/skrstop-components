package com.skrstop.framework.components.starter.mongodb.entity.version;

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
public abstract class AbstractCreatorTimeVersionBaseEntity<T> extends AbstractCreateTimeVersionBaseEntity<T> implements CreatorExpand {

    private static final long serialVersionUID = -4024689623085796268L;

//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @TableId(type = IdType.ASSIGN_ID)
//    @TableId(type = IdType.AUTO)
//    public Long id;

    /**
     * 更新人名字
     */
    private String creator;

}
