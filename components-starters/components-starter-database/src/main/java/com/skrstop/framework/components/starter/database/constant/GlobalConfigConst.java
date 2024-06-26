package com.skrstop.framework.components.starter.database.constant;

/**
 * GlobalConfigConst class
 *
 * @author 蒋时华
 * @date 2019/6/6
 */
public interface GlobalConfigConst {

    String DATABASE_PREFIX = "skrstop.database.config";
    String DATABASE_DYNAMIC = "skrstop.database.dynamic";

    String DATABASE_CONFIG_CHECK_DRUID_USE_SELECT1 = DATABASE_PREFIX + ".checkDruidUseSelect1";
    String DATABASE_CONFIG_CLASS_CLASS_LOCATION = DATABASE_PREFIX + ".mapper-class-location";

    String DEFAULT_MAPPER_XML_LOCATION = "classpath*:mapper/*.xml";

    String DS_DEFAULT = "default";
    String DS_MASTER = "master";
    String DS_SLAVE = "slave";
}
