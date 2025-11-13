package com.skrstop.framework.components.core.exception;

import com.alibaba.fastjson2.JSON;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import com.skrstop.framework.components.core.exception.core.SkrstopThrowable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * SkrstopError class
 *
 * @author 蒋时华
 * @date 2019/5/31
 */
@Getter
@Setter
public class SkrstopError extends Error implements SkrstopThrowable {

    @Serial
    private static final long serialVersionUID = -1248879421143950687L;
    private Throwable throwable;

    private IResult iResult = new DefaultResult(CommonResultCode.FAIL);

    private String errorMessage;

    public SkrstopError() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public SkrstopError(String message) {
        super(message);
        this.iResult.setMessage(message);
    }

    public SkrstopError(Throwable throwable) {
        super(throwable.getMessage());
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public SkrstopError(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.iResult.setMessage(message);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public SkrstopError(IResult iResult) {
        super(iResult.getMessage());
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
    }

    public SkrstopError(IResult iResult, Throwable throwable) {
        super(throwable.getMessage());
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public SkrstopError(IResult iResult, String message) {
        super(message);
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
        this.iResult.setMessage(message);
    }

    public SkrstopError(IResult iResult, String message, Throwable throwable) {
        super(throwable.getMessage());
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
        this.iResult.setMessage(message);
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
        return printMessage.toString();
    }

}
