package com.zoe.framework.components.starter.annotation.handle.function;

import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.starter.annotation.anno.function.ServiceLock;
import com.zoe.framework.components.starter.annotation.handle.function.utils.MethodUtil;
import com.zoe.framework.components.util.enums.CharSetEnum;
import com.zoe.framework.components.util.value.data.StrUtil;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步锁 AOP
 *
 * @transaction 中  order 大小的说明
 * https://docs.spring.io/spring/docs/4.3.14.RELEASE/spring-framework-reference/htmlsingle/#transaction-declarative-annotations
 * https://docs.spring.io/spring/docs/4.3.14.RELEASE/javadoc-api/
 */
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties
@Scope
@Aspect
@Order(4)
@Slf4j
public class ServiceLockAspect {

    private ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    /*** Service层切点 */
    @Pointcut("@annotation(com.zoe.framework.components.starter.annotation.anno.function.ServiceLock)")
    public void serviceLockAspect() {
    }

    @Around("serviceLockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 注解所在方法名区分不同的限流策略
        Method method = MethodUtil.getMethod(joinPoint);
        String methodStr = method.toString();
        String functionName = DigestUtils.md5DigestAsHex(methodStr.getBytes(CharSetEnum.UTF8.getCharSet()));

        //获取注解信息
        ServiceLock annotation = method.getAnnotation(ServiceLock.class);
        // 获取注解每秒加入桶中的token
        String lockKey = annotation.lockId();
        boolean fastFail = annotation.fastFail();
        if (StrUtil.isBlank(lockKey)) {
            lockKey = functionName;
        }
        log.debug("function:{}, lockId:{}, 开启同步锁", methodStr, annotation.lockId());
        Object obj;
        Lock lock;
        if (!lockMap.containsKey(lockKey)) {
            lockMap.put(lockKey, new ReentrantLock(true));
        }
        lock = lockMap.get(lockKey);
        if (lock.tryLock(annotation.timeoutMs(), TimeUnit.MILLISECONDS)) {
            try {
                obj = joinPoint.proceed();
            } finally {
                lock.unlock();
            }
            return obj;
        }
        if (fastFail) {
            // 获取锁失败
            throw new ZoeRuntimeException(CommonResultCode.BUSY);
        }
        try {
            // wait
            lock.lock();
            obj = joinPoint.proceed();
        } finally {
            lock.unlock();
        }
        return obj;

    }
}
