package com.zoe.framework.components.starter.annotation.handle.function;

import cn.hutool.core.net.NetUtil;
import com.zoe.framework.components.starter.annotation.anno.function.IntranetLimit;
import com.zoe.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.zoe.framework.components.starter.annotation.exception.aspect.IntranetLimitException;
import com.zoe.framework.components.starter.common.util.AnnoFindUtil;
import com.zoe.framework.components.util.system.net.IPUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class IntranetLimitAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;

    public IntranetLimitAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        IntranetLimit intranetLimit = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), IntranetLimit.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(intranetLimit)) {
            return invocation.proceed();
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(ra)) {
            return invocation.proceed();
        }
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        if (ObjectUtil.isNull(request)) {
            return invocation.proceed();
        }
        boolean remoteAddr = NetUtil.isInnerIP(IPUtil.getIpAddress(request));
        if (!remoteAddr) {
            throw new IntranetLimitException();
        }
        return invocation.proceed();
    }

}