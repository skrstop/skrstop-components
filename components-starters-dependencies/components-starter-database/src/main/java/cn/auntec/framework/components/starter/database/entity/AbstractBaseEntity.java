package cn.auntec.framework.components.starter.database.entity;

import cn.auntec.framework.components.core.common.serializable.SerializableBean;
import cn.auntec.framework.components.util.value.lambda.LambdaUtil;
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
public abstract class AbstractBaseEntity extends SerializableBean {

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
