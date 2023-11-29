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
 * Zoe 运行时异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
@Getter
@Setter
public class ZoeRuntimeException extends RuntimeException implements ZoeThrowable, DataZoeThrowable {

    private static final long serialVersionUID = 4820515985134494290L;
    /*** origin exception  */
    private Throwable throwable;

    private IResult IResult = new DefaultResult(CommonResultCode.FAIL);

    private String exceptionMessage;
    private ThrowableData throwableData;

    public ZoeRuntimeException() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public ZoeRuntimeException(String message) {
        super(message);
        this.IResult.setMessage(message);
    }

    public ZoeRuntimeException(Throwable throwable) {
        super(throwable.getMessage());
        this.exceptionMessage = throwable.getMessage();
    }

    public ZoeRuntimeException(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult.setMessage(message);
        this.exceptionMessage = throwable.getMessage();
    }

    public ZoeRuntimeException(IResult IResult) {
        super(IResult.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
    }

    public ZoeRuntimeException(IResult IResult, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.exceptionMessage = throwable.getMessage();
    }

    public ZoeRuntimeException(IResult IResult, String message) {
        super(message);
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
    }

    public ZoeRuntimeException(IResult IResult, String message, Throwable throwable) {
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
