package cn.auntec.framework.components.starter.annotation.aspect;

import cn.auntec.framework.components.starter.annotation.AccessControl;
import cn.auntec.framework.components.starter.annotation.config.AccessControlConfig;
import cn.auntec.framework.components.starter.annotation.exception.ControllerDeprecatedException;
import cn.auntec.framework.components.util.value.data.CollectionUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.lang.reflect.Method;

/**
 * controller接口失效
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties(AccessControlConfig.class)
@Scope
@Aspect
@Order(1)
@ControllerAdvice
public class AccessControlAspect {

    @Pointcut("@annotation(cn.auntec.framework.components.starter.annotation.AccessControl)")
    public void accessControlAspect() {
    }

    @Autowired
    private AccessControlConfig accessControlConfig;

    /**
     * @param joinPoint
     * @throws NoSuchMethodException
     */
    @Before("accessControlAspect()")
    public void around(JoinPoint joinPoint) throws NoSuchMethodException {
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        AccessControl annotation = currentMethod.getAnnotation(AccessControl.class);
        String alias = annotation.alias();
        if (StrUtil.isBlank(alias) || !CollectionUtil.contains(accessControlConfig.getReleaseAlias(), alias)) {
            String message = annotation.message();
            throw new ControllerDeprecatedException(message);
        }
    }
}
