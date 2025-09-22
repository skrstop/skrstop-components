package com.skrstop.framework.components.starter.database.entity.deletedVersion;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyDeleted;
import com.skrstop.framework.components.starter.database.entity.version.AbstractVersionBaseEntity;
import com.skrstop.framework.components.util.constant.StringPoolConst;
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
public abstract class AbstractDeletedVersionBaseEntity extends AbstractVersionBaseEntity {


    private static final long serialVersionUID = -3062937558431776826L;

    /**
     * 软删除标记
     */
    @PropertyDeleted
    @TableLogic(value = StringPoolConst.ZERO, delval = StringPoolConst.ONE)
    public Boolean deleted;

}
