package com.skrstop.framework.components.starter.annotation.handle.function;

import com.google.common.util.concurrent.RateLimiter;
import com.skrstop.framework.components.starter.annotation.anno.function.AccessLimit;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.exception.aspect.AccessLimitException;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.DigestUtils;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AccessLimitAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;
    /**
     * 用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
     * 每秒只发出 limit 个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
     */
    private final ConcurrentHashMap<String, RateLimiter> limitMap = new ConcurrentHashMap<>();

    public AccessLimitAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        AccessLimit accessLimit = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), AccessLimit.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(accessLimit)) {
            return invocation.proceed();
        }

        String methodStr = invocation.getMethod().toString();
        String functionName = DigestUtils.md5DigestAsHex(methodStr.getBytes(CharSetEnum.UTF8.getCharSet()));
        // 获取注解每秒加入桶中的token
        double limitNum = accessLimit.limit();
        log.debug("function:{}, limit:{}, 开启限流", methodStr, limitNum);
        RateLimiter rateLimiter;
        // 获取rateLimiter
        if (limitMap.containsKey(functionName)) {
            rateLimiter = limitMap.get(functionName);
            double rate = rateLimiter.getRate();
            if (rate != limitNum) {
                limitMap.put(functionName, RateLimiter.create(limitNum));
                rateLimiter = limitMap.get(functionName);
            }
        } else {
            limitMap.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = limitMap.get(functionName);
        }
        boolean tryAcquire = rateLimiter.tryAcquire();
        if (tryAcquire) {
            //执行方法
            return invocation.proceed();
        }
        //拒绝请求
        throw new AccessLimitException();
    }

}