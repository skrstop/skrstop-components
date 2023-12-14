package com.zoe.framework.components.starter.annotation.handle.function;

import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.starter.annotation.anno.function.ServiceLock;
import com.zoe.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.zoe.framework.components.starter.common.util.AnnoFindUtil;
import com.zoe.framework.components.util.enums.CharSetEnum;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.DigestUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ServiceLockAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;
    private ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    public ServiceLockAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ServiceLock serviceLock = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), ServiceLock.class, this.annotationProperties.getAllowedPublicOnly());
        if (ObjectUtil.isNull(serviceLock)) {
            return invocation.proceed();
        }
        String methodStr = invocation.getMethod().toString();
        String functionName = DigestUtils.md5DigestAsHex(methodStr.getBytes(CharSetEnum.UTF8.getCharSet()));
        //获取注解信息
        // 获取注解每秒加入桶中的token
        String lockKey = serviceLock.lockId();
        boolean fastFail = serviceLock.fastFail();
        if (StrUtil.isBlank(lockKey)) {
            lockKey = functionName;
        }
        log.debug("function:{}, lockId:{}, 开启同步锁", methodStr, serviceLock.lockId());
        Object obj;
        Lock lock;
        if (!lockMap.containsKey(lockKey)) {
            lockMap.put(lockKey, new ReentrantLock(true));
        }
        lock = lockMap.get(lockKey);
        if (lock.tryLock(serviceLock.timeoutMs(), TimeUnit.MILLISECONDS)) {
            try {
                obj = invocation.proceed();
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
            obj = invocation.proceed();
        } finally {
            lock.unlock();
        }
        return obj;
    }

}