package com.zoe.framework.components.starter.mongodb.constant;

/**
 * @author 蒋时华
 * @date 2020-12-30 11:47:20
 */
public interface MongodbConst {

    String TRANSACTION_NAME_MONGO = "DefaultMongoTransactionManager";
    String TRANSACTION_REACTIVE_NAME_MONGO = "DefaultReactiveMongoTransactionManager";

    /**
     * 驼峰
     */
    String NAME_RULE_CAMEL_CASE = "camelCase";
    /**
     * 原本格式
     */
    String NAME_RULE_IDENTITY = "identity";
    /**
     * 中划线
     */
    String NAME_RULE_KEBAB_CASE = "kebabCase";
    /**
     * 原本格式小写
     */
    String NAME_RULE_LOWER_CASE = "lowerCase";
    /**
     * 下划线
     */
    String NAME_RULE_SNAKE_CASE = "snakeCase";
    /**
     * 全限定
     */
    String NAME_RULE_FQCN = "fqcn";

    String DISCRIMINATOR_RULE_CLASS_NAME = "className";
    String DISCRIMINATOR_RULE_LOWER_CLASS_NAME = "lowerClassName";
    String DISCRIMINATOR_RULE_LOWER_SIMPLE_NAME = "lowerSimpleName";
    String DISCRIMINATOR_RULE_SIMPLE_NAME = "simpleName";
    String DISCRIMINATOR_RULE_FQCN = "fqcn";
    String PROPERTY_DISCOVERY_RULE_FIELDS = "fields";
    String PROPERTY_DISCOVERY_RULE_METHODS = "methods";
    String UUID_UNSPECIFIED = "unspecified";
    String UUID_STANDARD = "standard";
    String UUID_C_SHARP_LEGACY = "c_sharp_legacy";
    String UUID_JAVA_LEGACY = "java_legacy";
    String UUID_PYTHON_LEGACY = "python_legacy";
    String DATE_STORAGE_UTC = "utc";
    String DATE_STORAGE_SYSTEM_DEFAULT = "system_default";

}
