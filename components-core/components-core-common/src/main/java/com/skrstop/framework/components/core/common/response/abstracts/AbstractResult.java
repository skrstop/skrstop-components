package com.skrstop.framework.components.core.common.response.abstracts;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 14:56:58
 */
@Setter
@Getter
public abstract class AbstractResult extends SerializableBean {

    /*** response code */
    protected String code;
    /*** response simple message */
    protected String message;
    /*** specific description */
    protected String description;
    /*** timestamp */
    protected Long timestamp = System.currentTimeMillis();

    public AbstractResult() {
    }

    public AbstractResult(IResult IResult) {
        this.code = IResult.getCode();
        this.message = IResult.getMessage();
    }

}
