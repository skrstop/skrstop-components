package com.skrstop.framework.components.util.media.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义业务异常，外部不可用，异常区域：5000 - 5500
 *
 * @author 蒋时华
 * @date 2020-05-02 23:19:21
 */
@AllArgsConstructor
public enum CommonExceptionCode implements IResult {

    /*** 媒体处理异常码 */
    IMAGE_HANLE("server.image.error", "图片处理异常"),
    VIDEO_HANLE("server.video.error", "视频处理异常异常"),

    ;

    @Getter
    @Setter
    private Object code;
    @Getter
    @Setter
    private String message;

}
