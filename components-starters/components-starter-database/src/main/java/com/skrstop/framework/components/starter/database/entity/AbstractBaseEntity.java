package com.skrstop.framework.components.starter.database.entity;

import com.skrstop.framework.components.util.value.lambda.LambdaUtil;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.io.Serializable;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = -3062937558431776826L;

    /**
     * 获取id 唯一标识
     *
     * @return
     */
    public abstract Long getId();

    @Transient
    public String getIdName() {
        return LambdaUtil.convertToFieldName(AbstractBaseEntity::getId);
    }

}
