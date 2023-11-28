package cn.auntec.framework.components.core.exception;

import cn.auntec.framework.components.core.common.response.DefaultResult;
import cn.auntec.framework.components.core.common.response.common.CommonResultCode;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.common.util.EnumCodeUtil;
import cn.auntec.framework.components.core.exception.core.AuntecThrowable;
import cn.auntec.framework.components.core.exception.core.DataAuntecThrowable;
import cn.auntec.framework.components.core.exception.core.ThrowableData;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * Auntec 运行时异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
@Getter
@Setter
public class AuntecRuntimeException extends RuntimeException implements AuntecThrowable, DataAuntecThrowable {

    private static final long serialVersionUID = 4820515985134494290L;
    /*** origin exception  */
    private Throwable throwable;

    private IResult IResult = new DefaultResult(CommonResultCode.FAIL);

    private String exceptionMessage;
    private ThrowableData throwableData;

    public AuntecRuntimeException() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public AuntecRuntimeException(String message) {
        super(message);
        this.IResult.setMessage(message);
    }

    public AuntecRuntimeException(Throwable throwable) {
        super(throwable.getMessage());
        this.exceptionMessage = throwable.getMessage();
    }

    public AuntecRuntimeException(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult.setMessage(message);
        this.exceptionMessage = throwable.getMessage();
    }

    public AuntecRuntimeException(IResult IResult) {
        super(IResult.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
    }

    public AuntecRuntimeException(IResult IResult, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.exceptionMessage = throwable.getMessage();
    }

    public AuntecRuntimeException(IResult IResult, String message) {
        super(message);
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
    }

    public AuntecRuntimeException(IResult IResult, String message, Throwable throwable) {
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
