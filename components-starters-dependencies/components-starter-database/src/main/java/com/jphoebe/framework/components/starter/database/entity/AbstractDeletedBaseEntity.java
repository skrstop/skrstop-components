package com.jphoebe.framework.components.starter.database.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jphoebe.framework.components.util.constant.StringPoolConst;
import lombok.Getter;
import lombok.Setter;

/**
 * DO base entity by deleted
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractDeletedBaseEntity extends AbstractBaseEntity {
    private static final long serialVersionUID = 8349145430304784822L;

    /**
     * 软删除标记
     */
    @TableLogic(value = StringPoolConst.ZERO, delval = StringPoolConst.ONE)
    public Boolean deleted;


}
