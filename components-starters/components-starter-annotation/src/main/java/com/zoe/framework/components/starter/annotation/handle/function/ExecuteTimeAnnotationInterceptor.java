package com.zoe.framework.components.starter.annotation.handle.function;

import com.zoe.framework.components.starter.annotation.anno.function.ExecuteTimeLog;
import com.zoe.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.zoe.framework.components.starter.common.util.AnnoFindUtil;
import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class ExecuteTimeAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;

    public ExecuteTimeAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ExecuteTimeLog executeTimeLog = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), ExecuteTimeLog.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(executeTimeLog)) {
            return invocation.proceed();
        }
        String className = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        String value = executeTimeLog.value();
        // 开始时间
        long startTimeMillis = System.currentTimeMillis();
        Object result = invocation.proceed();
        // 执行时间
        long execTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (StrUtil.isNotBlank(value)) {
            value = value + StringPoolConst.SPACE + StringPoolConst.DASH + StringPoolConst.SPACE;
        }
        if (annotationProperties.getExecuteTimeLog().isLogLevelInfo()) {
            log.info("{} 执行方法：{}.{} , 执行耗时：{} ms", value, className, methodName, execTimeMillis);
            return result;
        }
        log.debug("{} 执行方法：{}.{} , 执行耗时：{} ms", value, className, methodName, execTimeMillis);
        return result;

    }

}