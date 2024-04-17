package com.skrstop.framework.components.example.starters.simple.exception;

import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2023-12-01 17:30:20
 */
public enum CustomErrorCode implements IResult {

    CUSTOM_ERROR("custom_error", "自定义异常code");

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String code;

    CustomErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
