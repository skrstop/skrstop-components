package com.skrstop.framework.components.starter.common.proxy;

import com.skrstop.framework.components.util.value.data.CollectionUtil;
import lombok.NonNull;
import org.aopalliance.intercept.MethodInterceptor;

public class DynamicServiceAdvisor extends DynamicMultiServiceAdvisor {

    /**
     * 构造方法
     *
     * @param advice  切面
     * @param service service
     */
    public DynamicServiceAdvisor(@NonNull MethodInterceptor advice,
                                 @NonNull Class<?> service) {
        super(advice, CollectionUtil.newArrayList(service));
    }
}