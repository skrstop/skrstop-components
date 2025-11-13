package com.skrstop.framework.components.starter.annotation.exception.code;

import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义业务异常，外部不可用，异常区域：5600 - 5700
 *
 * @author 蒋时华
 * @date 2020-05-02 23:19:21
 */
@AllArgsConstructor
public enum WebAnnotationExceptionCode implements IResult {

    /*** 保留异常码 */
    ACCESS_LIMIT("server.limit.access", "访问过于频繁，请稍后重试"),
    CONTROLLER_DEPRECATED("server.deprecated.api", "该接口已失效"),
    INTRANET_LIMIT("server.limit.intranet", "内网访问限制"),
    PRIVACY_INFO("server.limit.privacyInfo", "隐私信息，不可访问"),
    NO_PROCESSOR("server.no.processor", "未找到相关处理器"),

    ;

    @Getter
    @Setter
    private Object code;
    @Getter
    @Setter
    private String message;

}
