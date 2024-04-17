package com.skrstop.framework.components.core.exception;

import com.alibaba.fastjson2.JSON;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import com.skrstop.framework.components.core.exception.core.SkrstopThrowable;
import lombok.Getter;
import lombok.Setter;

/**
 * Skrstop 运行时异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
@Getter
@Setter
public class SkrstopRuntimeException extends RuntimeException implements SkrstopThrowable {

    private static final long serialVersionUID = 4820515985134494290L;
    /*** origin exception  */
    private Throwable throwable;

    private IResult iResult = new DefaultResult(CommonResultCode.FAIL);

    private String exceptionMessage;

    public SkrstopRuntimeException() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public SkrstopRuntimeException(String message) {
        super(message);
        this.iResult.setMessage(message);
    }

    public SkrstopRuntimeException(Throwable throwable) {
        super(throwable.getMessage());
        this.exceptionMessage = throwable.getMessage();
    }

    public SkrstopRuntimeException(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.iResult.setMessage(message);
        this.exceptionMessage = throwable.getMessage();
    }

    public SkrstopRuntimeException(IResult iResult) {
        super(iResult.getMessage());
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
    }

    public SkrstopRuntimeException(IResult iResult, Throwable throwable) {
        super(throwable.getMessage());
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
        this.exceptionMessage = throwable.getMessage();
    }

    public SkrstopRuntimeException(IResult iResult, String message) {
        super(message);
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
        this.iResult.setMessage(message);
    }

    public SkrstopRuntimeException(IResult iResult, String message, Throwable throwable) {
        super(throwable.getMessage());
        this.iResult = EnumCodeUtil.transferEnumCode(iResult);
        this.iResult.setMessage(message);
        this.exceptionMessage = throwable.getMessage();
    }

    @Override
    public String toString() {

        StringBuilder printMessage = new StringBuilder("");
        printMessage.append("错误信息：").append(this.getMessage());
        printMessage.append("\n");
        printMessage.append("自定义错误信息：").append(this.getExceptionMessage());
        printMessage.append("\n");
        printMessage.append("result结果：").append(JSON.toJSON(this.getIResult()));
        return printMessage.toString();
    }
}
