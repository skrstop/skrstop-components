package com.skrstop.framework.components.core.common.response.common;


import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用错误码
 *
 * @author 蒋时华
 * @date 2019/5/30
 */
@AllArgsConstructor
public enum CommonResultCode implements IResult {

    /*** 通用错误码 */
    SUCCESS("server.success", "请求成功"),
    FAIL("server.fail", "请求失败"),
    NOT_FOUND("server.notFound", "无相关资源"),
    FORBIDDEN("server.forbidden", "禁止访问"),
    BUSY("server.busy", "服务器繁忙，请稍后重试"),

    ;

    @Getter
    @Setter
    private Object code;
    @Getter
    @Setter
    private String message;

}
