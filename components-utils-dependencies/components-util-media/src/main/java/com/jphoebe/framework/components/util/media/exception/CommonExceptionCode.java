package com.jphoebe.framework.components.util.media.exception;

import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.core.exception.defined.illegal.NotSupportedException;
import lombok.Getter;

/**
 * 自定义业务异常，外部不可用，异常区域：5000 - 5500
 *
 * @author 蒋时华
 * @date 2020-05-02 23:19:21
 */
public enum CommonExceptionCode implements IResult {

    /*** 媒体处理异常码 */
    IMAGE_HANLE("server.image.error", "图片处理异常"),
    VIDEO_HANLE("server.video.error", "视频处理异常异常"),

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
        throw new NotSupportedException("not support this method");
    }

    @Override
    public void setMessage(String message) {
        throw new NotSupportedException("not support this method");
    }
}
