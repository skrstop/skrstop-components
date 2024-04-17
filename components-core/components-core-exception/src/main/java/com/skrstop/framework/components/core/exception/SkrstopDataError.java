package com.skrstop.framework.components.core.exception;

import com.alibaba.fastjson2.JSON;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.core.SkrstopDataThrowable;
import com.skrstop.framework.components.core.exception.core.data.ThrowableData;
import lombok.Getter;
import lombok.Setter;

/**
 * SkrstopError class
 *
 * @author 蒋时华
 * @date 2019/5/31
 */
@Getter
@Setter
public class SkrstopDataError extends SkrstopError implements SkrstopDataThrowable {

    private ThrowableData throwableData;

    public SkrstopDataError(ThrowableData throwableData) {
        this.throwableData = throwableData;
    }

    public SkrstopDataError(String message, ThrowableData throwableData) {
        super(message);
        this.throwableData = throwableData;
    }

    public SkrstopDataError(Throwable throwable, ThrowableData throwableData) {
        super(throwable);
        this.throwableData = throwableData;
    }

    public SkrstopDataError(String message, Throwable throwable, ThrowableData throwableData) {
        super(message, throwable);
        this.throwableData = throwableData;
    }

    public SkrstopDataError(IResult iResult, ThrowableData throwableData) {
        super(iResult);
        this.throwableData = throwableData;
    }

    public SkrstopDataError(IResult iResult, Throwable throwable, ThrowableData throwableData) {
        super(iResult, throwable);
        this.throwableData = throwableData;
    }

    public SkrstopDataError(IResult iResult, String message, ThrowableData throwableData) {
        super(iResult, message);
        this.throwableData = throwableData;
    }

    public SkrstopDataError(IResult iResult, String message, Throwable throwable, ThrowableData throwableData) {
        super(iResult, message, throwable);
        this.throwableData = throwableData;
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
            printMessage.append("异常数据信息：").append(JSON.toJSON(this.getThrowableData().getData()));
        }
        return printMessage.toString();
    }

}
