package com.skrstop.framework.components.starter.annotation.handle.function;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfo;
import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfoValue;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.util.system.net.IPUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.security.SensitiveDataUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class PrivacyInfoAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;

    public PrivacyInfoAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        PrivacyInfo privacyInfo = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), PrivacyInfo.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(privacyInfo)) {
            return invocation.proceed();
        }
        Object returnVal = invocation.proceed();
        // 是否限制内网访问
        boolean limitIntranet = privacyInfo.limitIntranet();
        this.setInfo(returnVal, limitIntranet);
        return returnVal;
    }

    private void setInfo(Object returnVal, boolean limitIntranet) {
        if (ObjectUtil.isNull(returnVal)) {
            return;
        }
        PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(returnVal.getClass());
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String name = descriptor.getName();
            if ("class".equals(name)) {
                continue;
            }
            if (!this.isPrimitive(descriptor)) {
                if (returnVal instanceof Collection) {
                    Collection collection = (Collection) returnVal;
                    collection.forEach(item -> {
                        try {
                            this.setInfo(item, limitIntranet);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });
                } else if (returnVal instanceof Map) {
                    Map map = (Map) returnVal;
                    map.keySet().forEach(item -> {
                        try {
                            this.setInfo(map.get(item), limitIntranet);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });
                } else {
                    Object property = BeanUtil.getProperty(returnVal, descriptor.getName());
                    this.setInfo(property, limitIntranet);
                }
            } else {
                // 将隐私信息置null
                Field field = ReflectUtil.getField(returnVal.getClass(), descriptor.getName());
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
                    BeanUtil.setProperty(returnVal, descriptor.getName(), null);
                    continue;
                }
                if (!field.getType().equals(String.class)) {
                    continue;
                }
                // 字符串处理
                String oldValue = BeanUtil.getProperty(returnVal, descriptor.getName());
                if (StrUtil.isBlank(oldValue)) {
                    continue;
                }
                switch (privacyInfoValue.type()) {
                    case DEFAULT:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.defaultHide(oldValue));
                        break;
                    case BANK_CARD:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.bankCardNoHide(oldValue));
                        break;
                    case ID_CARD:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.idCardNoHide(oldValue));
                        break;
                    case PHONE:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.phoneOrTelNoHide(oldValue));
                        break;
                    case EMAIL:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.emailHide(oldValue));
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
        if (ObjectUtil.isNull(ra)) {
            return true;
        }
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        if (ObjectUtil.isNull(request)) {
            return true;
        }
        return NetUtil.isInnerIP(IPUtil.getIpAddress(request));
    }

}