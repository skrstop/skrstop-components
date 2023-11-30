package com.zoe.framework.components.starter.annotation.utils;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author 蒋时华
 * @date 2020-05-19 14:01:33
 */
@UtilityClass
public class MethodUtil {

    public static Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

}
