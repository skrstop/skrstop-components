package com.jphoebe.framework.components.starter.feign.protostuff.condition;

import com.jphoebe.framework.components.core.annotation.source.Snapshot;
import com.jphoebe.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.jphoebe.framework.components.util.value.reflect.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <code>FeignClient use local</code>
 *
 * @author 蒋时华
 * @date 2019-03-22
 */
@Slf4j
@Snapshot(message = "新版本未测试")
public class UseLocalFeignClientCondition implements Condition {

    private static final Map<String, Set<?>> SUB_TYPES_CACHE = new HashMap<>();

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        MergedAnnotation<ProtostuffFeignClient> feignClientMergedAnnotation = metadata.getAnnotations().get(ProtostuffFeignClient.class);
        // 判断是否走本地
        if (feignClientMergedAnnotation.getBoolean("local")) {
            return false;
        }
        // 判断是否自动走本地
        if (feignClientMergedAnnotation.getBoolean("autoLocal")) {
            return isAutoUseLocal(feignClientMergedAnnotation.getString("autoLocalBasePackage"), metadata);
        }
        return true;
    }

    private boolean isAutoUseLocal(String basePackage, AnnotatedTypeMetadata metadata) {
        try {
            // 1、获取使用@FeignClient的interface的Class
            ClassMetadata classMetadata = (ClassMetadata) metadata;
            String interfaceClassName = classMetadata.getClassName();
            Set<?> subTypes = SUB_TYPES_CACHE.get(interfaceClassName);
            if (subTypes == null) {
                Class<?> interfaceClass = Class.forName(interfaceClassName);
                // 2、获取interface对应的所有实现类
                subTypes = ReflectionUtil.getSubTypesOf(basePackage, interfaceClass);
                SUB_TYPES_CACHE.put(interfaceClassName, subTypes);
            }
            // 3、判断是否存在RPC interface的实现类，且实现类上有Controller、RestController注解
            if (subTypes == null || subTypes.isEmpty()) {
                return true;
            }
            // 遍历bean
            for (Object subType : subTypes) {
                Class<?> subTypeClass = (Class<?>) subType;
                boolean isRpcImplementClass = isRpcImplementClass(subTypeClass);
                if (isRpcImplementClass) {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    private boolean isRpcImplementClass(Class<?> clazz) {
        return ((AnnotationUtils.findAnnotation(clazz, Controller.class) != null)
                || (AnnotationUtils.findAnnotation(clazz, RestController.class) != null));
    }

}
