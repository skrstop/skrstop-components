package com.skrstop.framework.components.starter.mongodb.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.starter.mongodb.annotation.property.*;
import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import com.skrstop.framework.components.starter.mongodb.repository.SuperRepository;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Version;
import dev.morphia.query.updates.UpdateOperator;
import dev.morphia.query.updates.UpdateOperators;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2025-09-22 20:40:59
 * @since 1.0.0
 */
public class EntityPropertiesUtil {

    private static final Pair<String, Class<?>> DEFAULT_EMPTY = Pair.of(null, null);

    public static Object getFieldValue(Object entity, String fieldName) {
        return ReflectUtil.getFieldValue(entity, fieldName);
    }

    public static void setFieldValue(SuperRepository superRepository
            , Object entity
            , Set<String> fieldNames
            , Object value) {
        if (CollectionUtil.isEmpty(fieldNames)) {
            return;
        }
        for (String fieldName : fieldNames) {
            if (superRepository.onlySetCreateInfoWhenNull()) {
                Object fieldValue = getFieldValue(entity, fieldName);
                if (ObjectUtil.isNull(fieldValue)) {
                    ReflectUtil.setFieldValue(entity, fieldName, value);
                }
            } else if (!superRepository.onlySetCreateInfoWhenNull()) {
                ReflectUtil.setFieldValue(entity, fieldName, value);
            }
        }
    }

    public static void setFieldValue(SuperRepository superRepository
            , List<UpdateOperator> updates
            , Set<String> fieldCollect
            , Set<String> fieldNames
            , Object value) {
        if (CollectionUtil.isEmpty(fieldNames)) {
            return;
        }
        for (String fieldName : fieldNames) {
            if (superRepository.onlySetUpdateInfoWhenNull()
                    && !fieldCollect.contains(fieldName)) {
                updates.add(UpdateOperators.set(fieldName, value));
            } else if (!superRepository.onlySetUpdateInfoWhenNull()) {
                updates.add(UpdateOperators.set(fieldName, value));
            }
        }
    }

    public static Set<String> getColumnPropertyNames(Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache, Class<?> propertyType) {
        return Optional.ofNullable(propertyFieldCache.get(propertyType))
                .orElse(Collections.emptyMap())
                .keySet();
    }

    public static Set<String> getColumnDbNames(Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache, Class<?> propertyType) {
        return Optional.ofNullable(propertyFieldCache.get(propertyType))
                .orElse(Collections.emptyMap())
                .values()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toSet());
    }

    public static String getColumnPropertyNameId(Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache) {
        return Optional.ofNullable(propertyFieldCache.get(PropertyId.class))
                .orElse(Collections.emptyMap())
                .keySet()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public static String getColumnDbNameId(Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache) {
        return Optional.ofNullable(propertyFieldCache.get(PropertyId.class))
                .orElse(Collections.emptyMap())
                .values()
                .stream()
                .findFirst()
                .orElse(DEFAULT_EMPTY)
                .getKey();
    }

    public static Class<?> getColumnTypeId(Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache) {
        Map<String, Pair<String, Class<?>>> idColumnMap = propertyFieldCache.get(PropertyId.class);
        if (CollectionUtil.isEmpty(idColumnMap)) {
            return null;
        }
        if (idColumnMap.size() > 1) {
            throw new RuntimeException("There are multiple fields with @PropertyId annotation");
        }
        return idColumnMap.values().stream()
                .findFirst()
                .orElse(DEFAULT_EMPTY)
                .getValue();
    }

    /**
     * 表实体字段解析
     *
     * @param propertyNaming
     * @param entityClass
     * @return Map<Class<?>, Map<String, Pair<String, Class<?>>>>
     * key: Property注解类型
     * value: Map<String, Pair<String, Class<?>>>
     *      key: java字段名
     *      value: Pair<String, Class<?>>
     *          key: db字段名
     *          value: 字段类型
     */
    public static Map<Class<?>, Map<String, Pair<String, Class<?>>>> tableProperties(String propertyNaming, Class<?> entityClass) {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache = new HashMap<>();
        // 通用字段信息
        Field[] allFields = ReflectUtil.getFields(entityClass);
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            Property mongoProperty = AnnotationUtil.getAnnotation(field, Property.class);
            String tagetColumnName = null;
            if (ObjectUtil.isNotNull(mongoProperty)) {
                tagetColumnName = mongoProperty.value();
            } else {
                tagetColumnName = field.getName();
            }
            switch (propertyNaming) {
                case MongodbConst.NAME_RULE_IDENTITY:
                case MongodbConst.NAME_RULE_CAMEL_CASE:
                    break;
                case MongodbConst.NAME_RULE_SNAKE_CASE:
                    tagetColumnName = StrUtil.toUnderlineCase(tagetColumnName);
                    break;
                case MongodbConst.NAME_RULE_LOWER_CASE:
                    tagetColumnName = tagetColumnName.toLowerCase();
                    break;
                case MongodbConst.NAME_RULE_KEBAB_CASE:
                    tagetColumnName = StrUtil.toSymbolCase(tagetColumnName, '-');
                    break;
                default:
                    break;
            }
            // id
            PropertyId propertyId = AnnotationUtil.getAnnotation(field, PropertyId.class);
            Id id = AnnotationUtil.getAnnotation(field, Id.class);
            if (ObjectUtil.isNotNull(propertyId) || ObjectUtil.isNotNull(id)) {
                if (ObjectUtil.isNotNull(id)) {
                    propertyFieldCache.computeIfAbsent(PropertyId.class, k -> new HashMap<>())
                            .put(field.getName(), Pair.of(MongodbConst.DEFAULT_ID_NAME, field.getType()));
                } else {
                    propertyFieldCache.computeIfAbsent(PropertyId.class, k -> new HashMap<>())
                            .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                }
                continue;
            }
            // 创建人
            PropertyCreateBy propertyCreateBy = AnnotationUtil.getAnnotation(field, PropertyCreateBy.class);
            if (ObjectUtil.isNotNull(propertyCreateBy)) {
                propertyFieldCache.computeIfAbsent(PropertyCreateBy.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 创建时间
            PropertyCreateTime propertyCreateTime = AnnotationUtil.getAnnotation(field, PropertyCreateTime.class);
            if (ObjectUtil.isNotNull(propertyCreateTime)) {
                propertyFieldCache.computeIfAbsent(PropertyCreateTime.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 创建人名
            PropertyCreator propertyCreator = AnnotationUtil.getAnnotation(field, PropertyCreator.class);
            if (ObjectUtil.isNotNull(propertyCreator)) {
                propertyFieldCache.computeIfAbsent(PropertyCreator.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 更新人
            PropertyUpdateBy propertyUpdateBy = AnnotationUtil.getAnnotation(field, PropertyUpdateBy.class);
            if (ObjectUtil.isNotNull(propertyUpdateBy)) {
                propertyFieldCache.computeIfAbsent(PropertyUpdateBy.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 更新时间
            PropertyUpdateTime propertyUpdateTime = AnnotationUtil.getAnnotation(field, PropertyUpdateTime.class);
            if (ObjectUtil.isNotNull(propertyUpdateTime)) {
                propertyFieldCache.computeIfAbsent(PropertyUpdateTime.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 更新人名
            PropertyUpdater propertyUpdater = AnnotationUtil.getAnnotation(field, PropertyUpdater.class);
            if (ObjectUtil.isNotNull(propertyUpdater)) {
                propertyFieldCache.computeIfAbsent(PropertyUpdater.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 版本
            PropertyVersion propertyVersion = AnnotationUtil.getAnnotation(field, PropertyVersion.class);
            Version tableVersion = AnnotationUtil.getAnnotation(field, Version.class);
            if (ObjectUtil.isNotNull(propertyVersion) || ObjectUtil.isNotNull(tableVersion)) {
                propertyFieldCache.computeIfAbsent(PropertyVersion.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
                continue;
            }
            // 软删除
            PropertyDeleted propertyDeleted = AnnotationUtil.getAnnotation(field, PropertyDeleted.class);
            if (ObjectUtil.isNotNull(propertyDeleted)) {
                propertyFieldCache.computeIfAbsent(PropertyDeleted.class, k -> new HashMap<>())
                        .put(field.getName(), Pair.of(tagetColumnName, field.getType()));
            }
        }
        return propertyFieldCache;
    }
}
