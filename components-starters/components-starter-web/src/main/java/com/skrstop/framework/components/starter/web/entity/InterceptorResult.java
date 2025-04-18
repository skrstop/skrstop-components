package com.skrstop.framework.components.starter.web.entity;

import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author 蒋时华
 * @date 2023-12-12 14:25:26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class InterceptorResult {

    /**
     * 是否继续向下处理
     * 当result != null 返回拦截器的处理结果
     * 当result == null && next = true，会继续向下处理，直到返回结果
     * 当result == null && next = false，返回 DynamicResult.build() 结果，
     */
    private boolean next;

    /**
     * response响应码
     */
    private Integer responseStatus;

    /**
     * 返回结果
     */
    private IResult result;

}
