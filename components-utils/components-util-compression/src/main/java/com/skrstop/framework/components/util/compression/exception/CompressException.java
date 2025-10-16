package com.skrstop.framework.components.util.compression.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 */
public class CompressException extends SkrstopRuntimeException {

    private static final long serialVersionUID = -1025089131707609566L;

    public CompressException() {
        super(CompressExceptionCode.COMPRESSION);
    }

    public CompressException(Throwable throwable) {
        super(CompressExceptionCode.COMPRESSION, throwable);
    }

    public CompressException(String message) {
        super(CompressExceptionCode.COMPRESSION, message);
    }

    public CompressException(String message, Throwable throwable) {
        super(CommonExceptionCode.NOT_SUPPORTED, message, throwable);
    }

    @AllArgsConstructor
    enum CompressExceptionCode implements IResult {
        /*** 异常码 */
        COMPRESSION("server.compress.error", "解压缩异常");;

        @Getter
        @Setter
        private Object code;
        @Getter
        @Setter
        private String message;

    }

}
