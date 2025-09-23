package com.skrstop.framework.components.starter.database.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.skrstop.framework.components.starter.database.annotation.property.*;
import com.skrstop.framework.components.starter.database.repository.SuperRepository;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author 蒋时华
 * @date 2025-09-22 20:40:59
 * @since 1.0.0
 */
public class EntityPropertiesUtil {

    public static void setFieldValue(SuperRepository superRepository
            , TableInfo tableInfo
            , Object entity
            , Set<String> fieldNames
            , Object value) {
        if (CollectionUtil.isEmpty(fieldNames)) {
            return;
        }
        for (String key : fieldNames) {
            if (superRepository.onlySetUpdateInfoWhenNull()) {
                Object propertyValue = tableInfo.getPropertyValue(entity, key);
                if (ObjectUtil.isNull(propertyValue)) {
                    tableInfo.setPropertyValue(entity, key, value);
                }
            } else if (!superRepository.onlySetUpdateInfoWhenNull()) {
                tableInfo.setPropertyValue(entity, key, value);
            }
        }
    }

    public static void setFieldValue(SuperRepository superRepository
            , UpdateWrapper updateWrapper
            , Map<String, Object> paramNameValueMap
            , Map<String, String> paramMap
            , Set<String> fieldNames
            , Object value) {
        if (CollectionUtil.isEmpty(fieldNames)) {
            return;
        }
        fieldNames.forEach(property -> {
            if (superRepository.onlySetUpdateInfoWhenNull()
                    && (!paramMap.containsKey(property) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(property))))) {
                updateWrapper.set(property, value);
            }
            // 无论如何都覆盖则直接修改
            if (!superRepository.onlySetUpdateInfoWhenNull()) {
                updateWrapper.set(property, value);
            }
        });
    }

    public static void setFieldValue(SuperRepository superRepository
            , LambdaUpdateWrapper lambdaUpdateWrapper
            , Map<String, Object> paramNameValueMap
            , Map<String, String> paramMap
            , Set<String> fieldNames
            , Object value) {
        if (CollectionUtil.isEmpty(fieldNames)) {
            return;
        }
        fieldNames.forEach(property -> {
            if (superRepository.onlySetUpdateInfoWhenNull()
                    && (!paramMap.containsKey(property) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(property))))) {
                if (ObjectUtil.isNotNull(paramMap.get(property))) {
                    paramNameValueMap.put(paramMap.get(property), value);
                } else {
                    lambdaUpdateWrapper.setSql(property + StringPoolConst.EQUALS + value);
                }
            }
            if (!superRepository.onlySetUpdateInfoWhenNull()) {
                if (ObjectUtil.isNotNull(paramMap.get(property))) {
                    paramNameValueMap.put(paramMap.get(property), value);
                } else {
                    lambdaUpdateWrapper.setSql(property + StringPoolConst.EQUALS + value);
                }
            }
        });
    }

    public static Set<String> getColumnNames(Map<Class<?>, Map<String, Class<?>>> propertyFieldCache, Class<?> propertyClass) {
        return Optional.of(propertyFieldCache.get(propertyClass))
                .orElse(Collections.emptyMap())
                .keySet();
    }

    public static String getColumnNameId(Map<Class<?>, Map<String, Class<?>>> propertyFieldCache) {
        return Optional.of(propertyFieldCache.get(PropertyId.class))
                .orElse(Collections.emptyMap())
                .keySet()
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 表实体字段解析
     *
     * @param entityClass
     * @return
     */
    public static Map<Class<?>, Map<String, Class<?>>> tableProperties(Class<?> entityClass) {
        Map<Class<?>, Map<String, Class<?>>> propertyFieldCache = new HashMap<>();
        // 通用字段信息
        Field[] allFields = ReflectUtil.getFields(entityClass);
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // id
            PropertyId propertyId = AnnotationUtil.getAnnotation(field, PropertyId.class);
            TableId id = AnnotationUtil.getAnnotation(field, TableId.class);
            if (ObjectUtil.isNotNull(propertyId) || ObjectUtil.isNotNull(id)) {
                if (ObjectUtil.isNotNull(id) && StrUtil.isNotBlank(id.value())) {
                    propertyFieldCache.computeIfAbsent(PropertyId.class, k -> new HashMap<>())
                            .put(id.value(), field.getType());
                    continue;
                }
                propertyFieldCache.computeIfAbsent(PropertyId.class, k -> new HashMap<>())
                        .put(field.getName(), field.getType());
                continue;
            }
            String targetFieldName = field.getName();
            if (StrUtil.isBlank(targetFieldName)) {
                continue;
            }
            // 创建人
            PropertyCreateBy propertyCreateBy = AnnotationUtil.getAnnotation(field, PropertyCreateBy.class);
            if (ObjectUtil.isNotNull(propertyCreateBy)) {
                propertyFieldCache.computeIfAbsent(PropertyCreateBy.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 创建时间
            PropertyCreateTime propertyCreateTime = AnnotationUtil.getAnnotation(field, PropertyCreateTime.class);
            if (ObjectUtil.isNotNull(propertyCreateTime)) {
                propertyFieldCache.computeIfAbsent(PropertyCreateTime.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 创建人名
            PropertyCreator propertyCreator = AnnotationUtil.getAnnotation(field, PropertyCreator.class);
            if (ObjectUtil.isNotNull(propertyCreator)) {
                propertyFieldCache.computeIfAbsent(PropertyCreator.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 更新人
            PropertyUpdateBy propertyUpdateBy = AnnotationUtil.getAnnotation(field, PropertyUpdateBy.class);
            if (ObjectUtil.isNotNull(propertyUpdateBy)) {
                propertyFieldCache.computeIfAbsent(PropertyUpdateBy.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 更新时间
            PropertyUpdateTime propertyUpdateTime = AnnotationUtil.getAnnotation(field, PropertyUpdateTime.class);
            if (ObjectUtil.isNotNull(propertyUpdateTime)) {
                propertyFieldCache.computeIfAbsent(PropertyUpdateTime.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 更新人名
            PropertyUpdater propertyUpdater = AnnotationUtil.getAnnotation(field, PropertyUpdater.class);
            if (ObjectUtil.isNotNull(propertyUpdater)) {
                propertyFieldCache.computeIfAbsent(PropertyUpdater.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 版本
            PropertyVersion propertyVersion = AnnotationUtil.getAnnotation(field, PropertyVersion.class);
            Version tableVersion = AnnotationUtil.getAnnotation(field, Version.class);
            if (ObjectUtil.isNotNull(propertyVersion) || ObjectUtil.isNotNull(tableVersion)) {
                propertyFieldCache.computeIfAbsent(PropertyVersion.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
                continue;
            }
            // 软删除
            PropertyDeleted propertyDeleted = AnnotationUtil.getAnnotation(field, PropertyDeleted.class);
            TableLogic tableLogic = AnnotationUtil.getAnnotation(field, TableLogic.class);
            if (ObjectUtil.isNotNull(propertyDeleted) || ObjectUtil.isNotNull(tableLogic)) {
                propertyFieldCache.computeIfAbsent(PropertyDeleted.class, k -> new HashMap<>()).put(targetFieldName, field.getType());
            }
        }
        return propertyFieldCache;
    }

}
