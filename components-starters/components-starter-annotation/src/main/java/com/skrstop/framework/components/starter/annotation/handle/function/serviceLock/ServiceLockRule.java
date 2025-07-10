package com.skrstop.framework.components.starter.annotation.handle.function.serviceLock;

import com.skrstop.framework.components.starter.annotation.anno.function.ServiceLock;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author 蒋时华
 * @date 2024-05-10 20:59:24
 * @since 1.0.0
 */
public interface ServiceLockRule {

    /**
     * @param invocation
     * @return
     */
    Object handle(MethodInvocation invocation, ServiceLock serviceLock) throws Throwable;

}
