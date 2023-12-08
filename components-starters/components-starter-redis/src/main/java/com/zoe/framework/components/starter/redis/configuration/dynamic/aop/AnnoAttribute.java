package com.zoe.framework.components.starter.redis.configuration.dynamic.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnnoAttribute<T> {
    /**
     * attr
     */
    private T attr;
}