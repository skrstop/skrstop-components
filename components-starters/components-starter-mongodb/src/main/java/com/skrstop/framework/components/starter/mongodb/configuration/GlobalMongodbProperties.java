package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.mongodb.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * mongo Config properties
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.MONGODB_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalMongodbProperties {

    /**
     * 扫描实体类路径
     */
    private String mapPackage;

    /**
     * 自动创建caps
     */
    private boolean applyCaps = false;

    /**
     * 自动创建校验
     */
    private boolean applyDocumentValidations = false;

    /**
     * 自动创建索引
     */
    private boolean applyIndexes = false;

    /**
     * 自动引入实体
     */
    private boolean autoImportModels = false;

    /**
     * 自定义编码器
     */
    private String codecProvider = "";

    /**
     * 集合命名规则
     *
     * @see MongodbConst#NAME_RULE_CAMEL_CASE
     * @see MongodbConst#NAME_RULE_IDENTITY
     * @see MongodbConst#NAME_RULE_KEBAB_CASE
     * @see MongodbConst#NAME_RULE_LOWER_CASE
     * @see MongodbConst#NAME_RULE_SNAKE_CASE
     * @see MongodbConst#NAME_RULE_FQCN
     */
    private String collectionNaming = MongodbConst.NAME_RULE_IDENTITY;

    /**
     * 时区
     *
     * @see MongodbConst#DATE_STORAGE_UTC
     * @see MongodbConst#DATE_STORAGE_SYSTEM_DEFAULT
     */
    private String dateStorage = MongodbConst.DATE_STORAGE_SYSTEM_DEFAULT;

    /**
     * 实体标识value
     *
     * @see MongodbConst#DISCRIMINATOR_RULE_CLASS_NAME
     * @see MongodbConst#DISCRIMINATOR_RULE_LOWER_CLASS_NAME
     * @see MongodbConst#DISCRIMINATOR_RULE_LOWER_SIMPLE_NAME
     * @see MongodbConst#DISCRIMINATOR_RULE_SIMPLE_NAME
     * @see MongodbConst#DISCRIMINATOR_RULE_FQCN
     */
    private String discriminator = MongodbConst.DISCRIMINATOR_RULE_CLASS_NAME;

    /**
     * 实体标识key
     */
    private String discriminatorKey = "_class";

    /**
     * 是否启用多态查询
     */
    private boolean enablePolymorphicQueries = false;

    /**
     * 是否忽略final字段
     */
    private boolean ignoreFinals = false;

    /**
     * 属性发现规则，字段、get/set方法
     *
     * @see MongodbConst#PROPERTY_DISCOVERY_RULE_FIELDS
     * @see MongodbConst#PROPERTY_DISCOVERY_RULE_METHODS
     */
    private String propertyDiscovery = MongodbConst.PROPERTY_DISCOVERY_RULE_FIELDS;

    /**
     * 属性命名规则
     *
     * @see MongodbConst#NAME_RULE_CAMEL_CASE
     * @see MongodbConst#NAME_RULE_IDENTITY
     * @see MongodbConst#NAME_RULE_KEBAB_CASE
     * @see MongodbConst#NAME_RULE_LOWER_CASE
     * @see MongodbConst#NAME_RULE_SNAKE_CASE
     * @see MongodbConst#NAME_RULE_FQCN
     */
    private String propertyNaming = MongodbConst.NAME_RULE_IDENTITY;

    /**
     * 查询工厂
     * dev.morphia.query.DefaultQueryFactor
     */
    private String queryFactory = "dev.morphia.query.DefaultQueryFactory";

    /**
     * 是否存储空集合
     */
    private boolean storeEmpties = true;

    /**
     * 是否存储null值
     */
    private boolean storeNulls = true;

    /**
     * uuid实现规则
     *
     * @see MongodbConst#UUID_UNSPECIFIED
     * @see MongodbConst#UUID_STANDARD
     * @see MongodbConst#UUID_C_SHARP_LEGACY
     * @see MongodbConst#UUID_JAVA_LEGACY
     * @see MongodbConst#UUID_PYTHON_LEGACY
     */
    private String uuidRepresentation = MongodbConst.UUID_JAVA_LEGACY;

}
