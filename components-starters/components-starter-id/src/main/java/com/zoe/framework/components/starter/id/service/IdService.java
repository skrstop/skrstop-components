package com.zoe.framework.components.starter.id.service;

import cn.hutool.core.util.IdUtil;

/**
 * @author 蒋时华
 * @date 2020-05-11 18:37:21
 */
public interface IdService {

    /**
     * 获取id
     *
     * @return
     */
    long getId();

    default String getUuid() {
        return IdUtil.fastUUID();
    }

    default String getUuidWithoutDash() {
        return IdUtil.fastSimpleUUID();
    }

    default String getObjectId() {
        return IdUtil.objectId();
    }

    default String getNanoId() {
        return IdUtil.nanoId();
    }

    default String getNanoId(int site) {
        return IdUtil.nanoId(site);
    }

}
