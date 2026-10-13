package com.skrstop.framework.components.starter.feign.protostuff.condition;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <code>FeignClient use local</code>
 *
 * @author 蒋时华
 * @date 2019-03-22
 */

public class UseLocalFeignClientCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MergedAnnotation<ProtostuffFeignClient> feignClientMergedAnnotation = metadata.getAnnotations().get(ProtostuffFeignClient.class);
        // 判断是否走本地
        if (feignClientMergedAnnotation.getBoolean("local")) {
            return false;
        }
        // 判断是否自动走本地(autoLocal):
        // ReflectionUtil.getSubTypesOf 基于 Reflections.collect + JavaCodeSerializer, 其元数据 read 不受支持,
        // 恒返回空集合(内部捕获 UnsupportedOperationException), 无法扫描到实现类,
        // 因此原扫包遍历实现类逻辑为不可达死代码, autoLocal 恒等价于"使用 feign"(true), 等价删除
        return true;
    }

}
