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
 * AuntecError class
 *
 * @author 蒋时华
 * @date 2019/5/31
 */
@Getter
@Setter
public class AuntecError extends Error implements AuntecThrowable, DataAuntecThrowable {

    private static final long serialVersionUID = -1248879421143950687L;
    private Throwable throwable;

    private IResult IResult = new DefaultResult(CommonResultCode.FAIL);

    private String errorMessage;
    private ThrowableData throwableData;

    public AuntecError() {
        /* 默认异常信息 */
        super(CommonResultCode.FAIL.getMessage());
    }

    public AuntecError(String message) {
        super(message);
        this.IResult.setMessage(message);
    }

    public AuntecError(Throwable throwable) {
        super(throwable.getMessage());
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public AuntecError(String message, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult.setMessage(message);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public AuntecError(IResult IResult) {
        super(IResult.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
    }

    public AuntecError(IResult IResult, Throwable throwable) {
        super(throwable.getMessage());
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.throwable = throwable;
        this.errorMessage = throwable.getMessage();
    }

    public AuntecError(IResult IResult, String message) {
        super(message);
        this.IResult = EnumCodeUtil.transferEnumCode(IResult);
        this.IResult.setMessage(message);
    }

    public AuntecError(IResult IResult, String message, Throwable throwable) {
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
