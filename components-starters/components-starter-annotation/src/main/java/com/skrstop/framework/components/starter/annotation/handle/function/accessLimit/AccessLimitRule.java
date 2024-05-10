package com.skrstop.framework.components.starter.annotation.handle.function.accessLimit;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author 蒋时华
 * @date 2024-05-10 20:59:24
 * @since 1.0.0
 */
public interface AccessLimitRule {

    /**
     * 是否可以继续执行
     *
     * @param invocation
     * @return
     */
    boolean canContinue(MethodInvocation invocation, double limitNum);

}
