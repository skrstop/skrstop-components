package com.skrstop.framework.components.starter.annotation.handle.function.serviceLock;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.annotation.anno.function.ServiceLock;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.DigestUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 蒋时华
 * @date 2025-03-13 17:48:03
 * @since 1.0.0
 */
@Slf4j
public class DefaultServiceLockRule implements ServiceLockRule {

    private ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    @Override
    public Object handle(MethodInvocation invocation, ServiceLock serviceLock) throws Throwable {
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
            throw new SkrstopRuntimeException(CommonResultCode.BUSY);
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
