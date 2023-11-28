package com.jphoebe.framework.components.starter.annotation.aspect;

import com.jphoebe.framework.components.starter.annotation.ExecuteTimeLog;
import com.jphoebe.framework.components.util.constant.StringPoolConst;
import com.jphoebe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 执行时间耗时打印切面
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties
@Scope
@Aspect
@Order
@Slf4j
public class ExecuteTimeLogAspect {

    /*** 执行耗时 */
    @Pointcut("@annotation(com.jphoebe.framework.components.starter.annotation.ExecuteTimeLog)")
    public void executeTimeLogAspect() {

    }

    @Around("executeTimeLogAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();
        ExecuteTimeLog executeTimeLog = currentMethod.getAnnotation(ExecuteTimeLog.class);
        String value = executeTimeLog.value();
        // 开始时间
        long startTimeMillis = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        // 执行时间
        long execTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (StrUtil.isNotBlank(value)) {
            value = value + StringPoolConst.SPACE + StringPoolConst.DASH + StringPoolConst.SPACE;
        }
        log.debug("{} 执行方法：{}.{} , 执行耗时：{} ms", value, className, methodName, execTimeMillis);
        return result;
    }
}
