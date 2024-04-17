package com.skrstop.framework.components.starter.mongodb.entity;

import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractBaseEntity<T> extends SerializableBean {

    private static final long serialVersionUID = -3062937558431776826L;

    /**
     * 获取id 唯一标识
     *
     * @return
     */
    public abstract T getId();

    public abstract void setId(T id);

    @Transient
    public String getIdName() {
        return "_id";
    }

}
