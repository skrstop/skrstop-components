package com.skrstop.framework.components.starter.mongodb.configuration.generator;


/**
 * Id生成器接口
 *
 * @since 2019-10-15
 */
public interface IdentifierGenerator {

    /**
     * 生成Id
     *
     * @return id
     */
    Number nextId();

    /**
     * 生成uuid
     *
     * @return uuid
     */
    String nextUUID();


}
