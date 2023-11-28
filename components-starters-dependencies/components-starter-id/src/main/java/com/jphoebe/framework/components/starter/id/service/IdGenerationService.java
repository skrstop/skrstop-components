package com.jphoebe.framework.components.starter.id.service;

/**
 * @author 蒋时华
 * @date 2020-05-11 18:37:21
 */
public interface IdGenerationService {

    /**
     * 获取id
     *
     * @return
     */
    long getId();

    String getUuid();

    String getUuidWithoutDash();

    String getObjectId();

}
