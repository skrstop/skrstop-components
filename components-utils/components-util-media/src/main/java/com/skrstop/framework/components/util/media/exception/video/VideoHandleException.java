package com.skrstop.framework.components.util.media.exception.video;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.util.media.exception.CommonExceptionCode;

/**
 * 图片处理异常
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public class VideoHandleException extends SkrstopRuntimeException {

    private static final long serialVersionUID = 2029639321827588277L;

    public VideoHandleException() {
        super(CommonExceptionCode.VIDEO_HANLE);
    }

    public VideoHandleException(Throwable throwable) {
        super(CommonExceptionCode.VIDEO_HANLE, throwable);
    }

    public VideoHandleException(String message) {
        super(CommonExceptionCode.VIDEO_HANLE, message);
    }

    public VideoHandleException(String message, Throwable throwable) {
        super(CommonExceptionCode.VIDEO_HANLE, message, throwable);
    }

}
