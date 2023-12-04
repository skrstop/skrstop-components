package com.zoe.framework.components.starter.annotation.handle.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.zoe.framework.components.starter.annotation.anno.aspect.AccessLimit;
import com.zoe.framework.components.starter.annotation.exception.aspect.AccessLimitException;
import com.zoe.framework.components.starter.annotation.handle.aspect.utils.MethodUtil;
import com.zoe.framework.components.util.enums.CharSetEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口限流注解
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties
@Scope
@Aspect
@Order(3)
@Slf4j
public class AccessLimitAspect {
    /**
     * 用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
     * 每秒只发出 limit 个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
     */
    private ConcurrentHashMap<String, RateLimiter> limitMap = new ConcurrentHashMap<>();

    /*** 限流 */
    @Pointcut("@annotation(com.zoe.framework.components.starter.annotation.anno.aspect.AccessLimit)")
    public void accessLimitAspect() {
    }

    @Around("accessLimitAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = MethodUtil.getMethod(joinPoint);
        String methodStr = method.toString();
        String functionName = DigestUtils.md5DigestAsHex(methodStr.getBytes(CharSetEnum.UTF8.getCharSet()));

        //获取注解信息
        AccessLimit annotation = method.getAnnotation(AccessLimit.class);
        // 获取注解每秒加入桶中的token
        double limitNum = annotation.limit();

        log.debug("function:{}, limit:{}, 开启限流", methodStr, limitNum);

        Object obj = null;
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
            obj = joinPoint.proceed();
        } else {
            //拒绝请求
            throw new AccessLimitException();
        }
        return obj;

    }
}
