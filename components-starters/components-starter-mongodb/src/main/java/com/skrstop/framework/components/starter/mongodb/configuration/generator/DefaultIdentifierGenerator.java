package com.skrstop.framework.components.starter.mongodb.configuration.generator;

import cn.hutool.core.util.IdUtil;

/**
 * @author 蒋时华
 * @date 2025-09-22 22:34:33
 * @since 1.0.0
 */
public class DefaultIdentifierGenerator implements IdentifierGenerator {
    @Override
    public Number nextId() {
        return IdUtil.getSnowflakeNextId();
    }

    @Override
    public String nextUUID() {
        return IdUtil.fastSimpleUUID();
    }
}
