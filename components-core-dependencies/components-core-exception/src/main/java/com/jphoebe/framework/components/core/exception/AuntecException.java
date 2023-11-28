package com.jphoebe.framework.components.core.exception;

import com.alibaba.fastjson.JSON;
import com.jphoebe.framework.components.core.common.response.DefaultResult;
import com.jphoebe.framework.components.core.common.response.common.CommonResultCode;
import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.core.common.util.EnumCodeUtil;
import com.jphoebe.framework.components.core.exception.core.AuntecThrowable;
import com.jphoebe.framework.components.core.exception.core.DataAuntecThrowable;
import com.jphoebe.framework.components.core.exception.core.ThrowableData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2019/1/29
 */
@Getter
@Setter
public class AuntecException extends Exception implements AuntecThrowable, DataAuntecThrowable {

    private static final long serialVersionUID = 7496223413975961515L;
    private Throwable throwable;

    private IResult IResult = new DefaultResult(CommonResultCode.FAIL);

    private String exceptionMessage;
    private ThrowableData throwableData;

    public AuntecException() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public AuntecException(String message) {
        super(message);
        this.IResult.setMessage(message);
    }

    public AuntecException(Throwable throwable) {
        super(throwable.getMessage());
        this.exceptionMessage = throwable.getMessage();
    }

    public AuntecException(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult.setMessage(message);
        this.exceptionMessage = throwable.getMessage();
    }

    public AuntecException(IResult IResult) {
        super(IResult.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
    }

    public AuntecException(IResult IResult, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.exceptionMessage = throwable.getMessage();
    }

    public AuntecException(IResult IResult, String message) {
        super(message);
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
    }

    public AuntecException(IResult IResult, String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
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
        if (this.getThrowableData() != null) {
            printMessage.append("\n");
            printMessage.append("自定义异常详细信息：").append(this.getThrowableData().getDetailMessage());
        }
        return printMessage.toString();
    }
}
