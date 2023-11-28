package cn.auntec.framework.components.starter.id.service.strategy;

import cn.auntec.framework.components.starter.id.config.GlobalIdConfig;
import cn.auntec.framework.components.starter.id.service.IdGenerationService;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author 蒋时华
 * @date 2020-05-11 18:47:26
 */
public class LocalIdGenerationServiceImpl implements IdGenerationService {

    private final Snowflake snowflake;

    public LocalIdGenerationServiceImpl(GlobalIdConfig globalIdConfig) {
        snowflake = IdUtil.createSnowflake(globalIdConfig.getWorkerId(), globalIdConfig.getDataCenterId());
    }

    @Override
    public long getId() {
        return this.snowflake.nextId();
    }

    @Override
    public String getUuid() {
        return IdUtil.fastUUID();
    }

    @Override
    public String getUuidWithoutDash() {
        return IdUtil.fastSimpleUUID();
    }

    @Override
    public String getObjectId() {
        return IdUtil.objectId();
    }

}
