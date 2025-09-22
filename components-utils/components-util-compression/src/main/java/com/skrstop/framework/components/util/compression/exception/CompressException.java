package com.skrstop.framework.components.util.compression.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import lombok.Getter;

import java.io.Serial;

/**
 * @author 蒋时华
 */
public class CompressException extends SkrstopRuntimeException {

    @Serial
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

    enum CompressExceptionCode implements IResult {
        /*** 异常码 */
        COMPRESSION("server.compress.error", "解压缩异常");;

        @Getter
        private final String code;
        @Getter
        private final String message;

        CompressExceptionCode(String code, String message) {
            this.code = code;
            this.message = message;
        }


        @Override
        public void setCode(String code) {
            throw new NotSupportedException("not support this method");
        }

        @Override
        public void setMessage(String message) {
            throw new NotSupportedException("not support this method");
        }
    }

}
