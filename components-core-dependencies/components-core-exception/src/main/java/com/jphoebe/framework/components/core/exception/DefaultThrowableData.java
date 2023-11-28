package com.jphoebe.framework.components.core.exception;

import com.jphoebe.framework.components.core.common.serializable.SerializableBean;
import com.jphoebe.framework.components.core.exception.core.ThrowableData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 23:05:07
 */
@Getter
@Setter
public class DefaultThrowableData extends SerializableBean implements ThrowableData {

    private static final long serialVersionUID = 1372837245824421920L;

    private String detailMessage;

    private SerializableBean dataMessage;
}
