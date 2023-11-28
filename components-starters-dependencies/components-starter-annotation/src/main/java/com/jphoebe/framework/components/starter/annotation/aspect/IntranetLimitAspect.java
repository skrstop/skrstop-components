package com.jphoebe.framework.components.starter.annotation.aspect;

import cn.hutool.core.net.NetUtil;
import com.jphoebe.framework.components.starter.annotation.exception.IntranetLimitException;
import com.jphoebe.framework.components.util.system.net.IPUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 内网访问限制
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties
@Scope
@Aspect
@Order(2)
public class IntranetLimitAspect {

    @Pointcut("@annotation(com.jphoebe.framework.components.starter.annotation.IntranetLimit)")
    public void intranetLimitAspect() {
    }

    /**
     * todo 2019/6/5 服务间调用的问题
     *
     * @param joinPoint
     */
    @Before("intranetLimitAspect()")
    public void before(JoinPoint joinPoint) {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        boolean remoteAddr = NetUtil.isInnerIP(IPUtil.getIpAddress(request));
        if (!remoteAddr) {
            throw new IntranetLimitException();
        }

    }
}
