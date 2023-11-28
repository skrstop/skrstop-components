package cn.auntec.framework.components.starter.web.exception.code;

import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.exception.defined.illegal.NotSupportedException;
import lombok.Getter;

/**
 * 自定义业务异常，外部不可用，异常区域：5500 - 5600
 *
 * @author 蒋时华
 * @date 2020-05-02 23:19:21
 */
public enum WebStarterExceptionCode implements IResult {

    /*** 保留异常码 */
    MATCH_PARAMETER("server.parameter.match", "参数类型错误"),
    NOT_SUPPORT_MEDIA_TYPE("server.media.notSupport", "不支持该媒体类型"),
    NOT_ACCEPTED_MEDIA_TYPE("server.media.notAccept", "不可接受该媒体类型"),
    REQUEST_TIMEOUT("server.timeout", "请求超时"),
    REQUEST_METHOD_NOT_ALLOWED("server.method.notSupport", "不支持该请求方法"),
    MISS_PARAMETER("server.parameter.miss", "缺少参数"),
    FILE_UPLOAD("server.file.upload", "文件上传错误"),
    FILE_NOT_POINT("server.file.notPoint", "未指定上传文件"),


    ;

    @Getter
    private final String code;
    @Getter
    private final String message;

    WebStarterExceptionCode(String code, String message) {
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
