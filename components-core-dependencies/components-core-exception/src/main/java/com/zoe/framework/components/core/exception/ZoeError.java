package com.zoe.framework.components.core.exception;

import com.alibaba.fastjson2.JSON;
import com.zoe.framework.components.core.common.response.DefaultResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.util.EnumCodeUtil;
import com.zoe.framework.components.core.exception.core.DataZoeThrowable;
import com.zoe.framework.components.core.exception.core.ThrowableData;
import com.zoe.framework.components.core.exception.core.ZoeThrowable;
import lombok.Getter;
import lombok.Setter;

/**
 * ZoeError class
 *
 * @author 蒋时华
 * @date 2019/5/31
 */
@Getter
@Setter
public class ZoeError extends Error implements ZoeThrowable, DataZoeThrowable {

    private static final long serialVersionUID = -1248879421143950687L;
    private Throwable throwable;

    private IResult IResult = new DefaultResult(CommonResultCode.FAIL);

    private String errorMessage;
    private ThrowableData throwableData;

    public ZoeError() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public ZoeError(String message) {
        super(message);
        this.IResult.setMessage(message);
    }

    public ZoeError(Throwable throwable) {
        super(throwable.getMessage());
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public ZoeError(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult.setMessage(message);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public ZoeError(IResult IResult) {
        super(IResult.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
    }

    public ZoeError(IResult IResult, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public ZoeError(IResult IResult, String message) {
        super(message);
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
    }

    public ZoeError(IResult IResult, String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    @Override
    public String toString() {

        StringBuilder printMessage = new StringBuilder("");
        printMessage.append("错误信息：").append(this.getMessage());
        printMessage.append("\n");
        printMessage.append("自定义错误信息：").append(this.getErrorMessage());
        printMessage.append("\n");
        printMessage.append("result结果：").append(JSON.toJSON(this.getIResult()));
        if (this.getThrowableData() != null) {
            printMessage.append("\n");
            printMessage.append("自定义异常详细信息：").append(this.getThrowableData().getDetailMessage());
        }
        return printMessage.toString();
    }

}
