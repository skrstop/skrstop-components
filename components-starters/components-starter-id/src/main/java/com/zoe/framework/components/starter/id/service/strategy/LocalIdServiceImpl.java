package com.zoe.framework.components.starter.id.service.strategy;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.zoe.framework.components.starter.id.config.GlobalIdProperties;
import com.zoe.framework.components.starter.id.service.IdService;

/**
 * @author 蒋时华
 * @date 2020-05-11 18:47:26
 */
public class LocalIdServiceImpl implements IdService {

    private final Snowflake snowflake;

    public LocalIdServiceImpl(GlobalIdProperties globalIdProperties) {
        snowflake = IdUtil.getSnowflake(globalIdProperties.getWorkerId(), globalIdProperties.getDataCenterId());
    }

    @Override
    public long getId() {
        return this.snowflake.nextId();
    }

}
