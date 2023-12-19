package com.skrstop.framework.components.starter.annotation.handle.function;

import com.skrstop.framework.components.starter.annotation.anno.function.AccessControl;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.exception.aspect.ControllerDeprecatedException;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AccessControlAnnotationInterceptor implements MethodInterceptor {

    private AnnotationProperties annotationProperties;

    public AccessControlAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        AccessControl accessControl = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), AccessControl.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(accessControl)) {
            return invocation.proceed();
        }
        String alias = accessControl.alias();
        if (StrUtil.isBlank(alias) || !CollectionUtil.contains(annotationProperties.getAccessControl().getReleaseAlias(), alias)) {
            String message = accessControl.message();
            throw new ControllerDeprecatedException(message);
        }
        return invocation.proceed();
    }

}