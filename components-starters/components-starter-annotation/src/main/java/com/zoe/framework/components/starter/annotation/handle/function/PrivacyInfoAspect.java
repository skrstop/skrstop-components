package com.zoe.framework.components.starter.annotation.handle.function;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ReflectUtil;
import com.zoe.framework.components.starter.annotation.anno.function.PrivacyInfo;
import com.zoe.framework.components.starter.annotation.anno.function.PrivacyInfoValue;
import com.zoe.framework.components.starter.annotation.constant.PrivacyInfoType;
import com.zoe.framework.components.util.system.net.IPUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import com.zoe.framework.components.util.value.security.SensitiveDataUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

/**
 * 隐私信息限制
 *
 * @author 蒋时华
 * @date 2019/4/3
 */
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties
@Scope
@Aspect
@Order(2)
@Slf4j
public class PrivacyInfoAspect {

    @Pointcut("@annotation(com.zoe.framework.components.starter.annotation.anno.function.PrivacyInfo)")
    public void privacyInfo() {
    }

    @AfterReturning(pointcut = "privacyInfo()", returning = "returnValue")
    public void after(JoinPoint joinPoint, Object returnValue) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        PrivacyInfo privacyInfo = currentMethod.getAnnotation(PrivacyInfo.class);
        if (ObjectUtil.isNull(privacyInfo)) {
            return;
        }
        // 是否限制内网访问
        boolean limitIntranet = privacyInfo.limitIntranet();
        this.setInfo(returnValue, limitIntranet);
    }

    @SuppressWarnings("unchecked")
    private void setInfo(Object bean, boolean limitIntranet) {
        if (ObjectUtil.isNull(bean)) {
            return;
        }
        PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(bean.getClass());
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String name = descriptor.getName();
            if ("class".equals(name)) {
                continue;
            }
            if (!this.isPrimitive(descriptor)) {
                if (bean instanceof Collection) {
                    Collection collection = (Collection) bean;
                    collection.forEach(item -> {
                        try {
                            this.setInfo(item, limitIntranet);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });
                } else if (bean instanceof Map) {
                    Map map = (Map) bean;
                    map.keySet().forEach(item -> {
                        try {
                            this.setInfo(map.get(item), limitIntranet);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });
                } else {
                    Object property = BeanUtil.getProperty(bean, descriptor.getName());
                    this.setInfo(property, limitIntranet);
                }
            } else {
                // 将隐私信息置null
                Field field = ReflectUtil.getField(bean.getClass(), descriptor.getName());
                if (ObjectUtil.isNull(field)) {
                    continue;
                }
                PrivacyInfoValue privacyInfoValue = field.getAnnotation(PrivacyInfoValue.class);
                if (ObjectUtil.isNull(privacyInfoValue) || ObjectUtil.isNull(privacyInfoValue.type())) {
                    continue;
                }
                boolean innerIp = this.innerIp();
                boolean limit = limitIntranet || privacyInfoValue.limitIntranet();
                if (innerIp && !limit) {
                    continue;
                }
                if (privacyInfoValue.type() == PrivacyInfoType.SET_NULL) {
                    // 属性设置为null
                    BeanUtil.setProperty(bean, descriptor.getName(), null);
                    continue;
                }
                if (!field.getType().equals(String.class)) {
                    continue;
                }
                // 字符串处理
                String oldValue = BeanUtil.getProperty(bean, descriptor.getName());
                if (StrUtil.isBlank(oldValue)) {
                    continue;
                }
                switch (privacyInfoValue.type()) {
                    case DEFAULT:
                        BeanUtil.setProperty(bean, descriptor.getName(), SensitiveDataUtil.defaultHide(oldValue));
                        break;
                    case BANK_CARD:
                        BeanUtil.setProperty(bean, descriptor.getName(), SensitiveDataUtil.bankCardNoHide(oldValue));
                        break;
                    case ID_CARD:
                        BeanUtil.setProperty(bean, descriptor.getName(), SensitiveDataUtil.idCardNoHide(oldValue));
                        break;
                    case PHONE:
                        BeanUtil.setProperty(bean, descriptor.getName(), SensitiveDataUtil.phoneOrTelNoHide(oldValue));
                        break;
                    case EMAIL:
                        BeanUtil.setProperty(bean, descriptor.getName(), SensitiveDataUtil.emailHide(oldValue));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean isPrimitive(PropertyDescriptor field) {
        Class<?> type = field.getPropertyType();
        boolean primitive = type.isPrimitive();
        if (!primitive) {
            return type.getName().equals(Integer.class.getName()) ||
                    type.getName().equals(Byte.class.getName()) ||
                    type.getName().equals(Long.class.getName()) ||
                    type.getName().equals(Double.class.getName()) ||
                    type.getName().equals(Float.class.getName()) ||
                    type.getName().equals(Character.class.getName()) ||
                    type.getName().equals(String.class.getName()) ||
                    type.getName().equals(Short.class.getName()) ||
                    type.getName().equals(Timestamp.class.getName()) ||
                    type.getName().equals(Time.class.getName()) ||
                    type.getName().equals(BigDecimal.class.getName()) ||
                    type.getName().equals(Boolean.class.getName());
        }
        return true;
    }

    private boolean innerIp() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        return NetUtil.isInnerIP(IPUtil.getIpAddress(request));
    }
}
