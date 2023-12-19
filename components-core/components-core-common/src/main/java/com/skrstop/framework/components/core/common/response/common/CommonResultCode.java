package com.skrstop.framework.components.core.common.response.common;


import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.Getter;

/**
 * 通用错误码
 *
 * @author 蒋时华
 * @date 2019/5/30
 */
public enum CommonResultCode implements IResult {

    /*** 通用错误码 */
    SUCCESS("server.success", "请求成功"),
    FAIL("server.fail", "请求失败"),
    NOT_FOUND("server.notFound", "无相关资源"),
    FORBIDDEN("server.forbidden", "禁止访问"),
    BUSY("server.busy", "服务器繁忙，请稍后重试"),

    ;

    @Getter
    private final String code;
    @Getter
    private final String message;

    CommonResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public void setCode(String code) throws IllegalArgumentException {
        throw new IllegalArgumentException("not support this method");
    }

    @Override
    public void setMessage(String message) {
        throw new IllegalArgumentException("not support this method");
    }
}
