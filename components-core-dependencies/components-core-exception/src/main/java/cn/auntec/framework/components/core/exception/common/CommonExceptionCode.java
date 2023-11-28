package cn.auntec.framework.components.core.exception.common;

import cn.auntec.framework.components.core.common.response.core.IResult;
import lombok.Getter;

/**
 * 自定义业务异常
 *
 * @author 蒋时华
 * @date 2020-05-02 23:19:21
 */
public enum CommonExceptionCode implements IResult {

    /*** 保留异常码 */
    NOT_SUPPORTED("server.notSupport", "not supported"),
    DE_CODING("server.decoding", "解码异常"),
    EN_CODING("server.encoding", "编码异常"),
    DE_SERIALIZATION("server.deserializable", "反序列化异常"),
    SERIALIZATION("server.serializable", "序列化异常"),
    ILLEGAL_ARGUMENT("server.parameter.illegal", "非法参数异常"),
    ILLEGAL_ACCESS("server.access.illegal", "非法访问异常"),
    HTTP_HANDLE("server.http.handle", "HTTP访问异常"),
    RPC_HANDLE("server.rpc.handle", "RPC访问异常"),
    PARAMETER("server.parameter.error", "参数异常"),
    NULL_POINTER("server.nullPointer", "空指针异常"),
    INDEX_OUT_OF_BOUNDS("server.indexOutOfBounds", "指针越界异常"),
    IO_STREAMING("server.io.streaming", "io流异常"),

    ;

    @Getter
    private final String code;
    @Getter
    private final String message;

    CommonExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public void setCode(String code) {
        throw new IllegalArgumentException("not support this method");
    }

    @Override
    public void setMessage(String message) {
        throw new IllegalArgumentException("not support this method");
    }
}
