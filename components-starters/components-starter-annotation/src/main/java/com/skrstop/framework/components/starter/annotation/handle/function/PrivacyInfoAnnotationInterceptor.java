package com.skrstop.framework.components.starter.annotation.handle.function;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfo;
import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfoValue;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType;
import com.skrstop.framework.components.starter.annotation.handle.function.privacyInfo.PrivacyInfoTypeRule;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.util.system.net.IPUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@SuppressWarnings("all")
public class PrivacyInfoAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;
    private final Map<String, PrivacyInfoTypeRule> privacyInfoTypeRuleMap = new ConcurrentHashMap<>();

    public PrivacyInfoAnnotationInterceptor(AnnotationProperties annotationProperties, List<PrivacyInfoTypeRule> privacyInfoTypeRuleList) {
        this.annotationProperties = annotationProperties;
        if (CollectionUtil.isEmpty(privacyInfoTypeRuleList)) {
            return;
        }
        privacyInfoTypeRuleList.forEach(rule -> {
            if (CollectionUtil.isEmpty(rule.supportType())) {
                return;
            }
            rule.supportType().forEach(type -> {
                this.privacyInfoTypeRuleMap.put(type, rule);
            });
        });
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        PrivacyInfo privacyInfo = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), PrivacyInfo.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(privacyInfo)) {
            return invocation.proceed();
        }
        Object returnVal = invocation.proceed();
        boolean innerIp = this.innerIp();
        this.setInfo(returnVal, innerIp);
        return returnVal;
    }

    private void setInfo(Object returnVal, boolean innerIp) {
        if (returnVal instanceof Collection) {
            Collection collection = (Collection) returnVal;
            if (collection.isEmpty()) {
                return;
            }
            for (Object item : collection) {
                this.setInfo(item, innerIp);
            }
        } else if (returnVal instanceof Map) {
            Map map = (Map) returnVal;
            if (map.isEmpty()) {
                return;
            }
            map.keySet().forEach(item -> {
                try {
                    this.setInfo(map.get(item), innerIp);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        } else {
            // 是个对象
            this.setObjectInfo(returnVal, innerIp);
        }
    }

    private void setObjectInfo(Object returnVal, boolean innerIp) {
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
                Object property = BeanUtil.getProperty(returnVal, descriptor.getName());
                this.setInfo(property, innerIp);
            } else {
                // 将隐私信息置null
                Field field = ReflectUtil.getField(returnVal.getClass(), descriptor.getName());
                if (ObjectUtil.isNull(field)) {
                    continue;
                }
                if (!field.getType().equals(String.class)) {
                    continue;
                }
                PrivacyInfoValue privacyInfoValue = field.getAnnotation(PrivacyInfoValue.class);
                if (ObjectUtil.isNull(privacyInfoValue) || ObjectUtil.isNull(privacyInfoValue.type())) {
                    continue;
                }
                boolean limit = privacyInfoValue.limitIntranet();
                if (innerIp && !limit) {
                    continue;
                }
                if (PrivacyInfoType.SET_NULL.equals(privacyInfoValue.type())) {
                    // 属性设置为null
                    BeanUtil.setProperty(returnVal, descriptor.getName(), null);
                    continue;
                }
                // 字符串处理
                String oldValue = BeanUtil.getProperty(returnVal, descriptor.getName());
                if (StrUtil.isBlank(oldValue)) {
                    continue;
                }
                switch (privacyInfoValue.type()) {
                    case PrivacyInfoType.DEFAULT:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.defaultHide(oldValue));
                        break;
                    case PrivacyInfoType.DEFAULT_BANK_CARD:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.bankCardNoHide(oldValue));
                        break;
                    case PrivacyInfoType.DEFAULT_ID_CARD:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.idCardNoHide(oldValue));
                        break;
                    case PrivacyInfoType.DEFAULT_PHONE:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.phoneOrTelNoHide(oldValue));
                        break;
                    case PrivacyInfoType.DEFAULT_EMAIL:
                        BeanUtil.setProperty(returnVal, descriptor.getName(), SensitiveDataUtil.emailHide(oldValue));
                        break;
                    default:
                        // 自定义
                        PrivacyInfoTypeRule rule = privacyInfoTypeRuleMap.get(privacyInfoValue.type());
                        if (ObjectUtil.isNull(rule)) {
                            break;
                        }
                        BeanUtil.setProperty(returnVal, descriptor.getName(), rule.handle(oldValue));
                        break;
                }
            }
        }
    }

    private boolean isPrimitive(PropertyDescriptor field) {
        Class<?> type = field.getPropertyType();
        boolean primitive = ClassUtil.isBasicType(type);
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
                    type.getName().equals(Date.class.getName()) ||
                    type.getName().equals(LocalDateTime.class.getName()) ||
                    type.getName().equals(LocalDate.class.getName()) ||
                    type.getName().equals(LocalTime.class.getName()) ||
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
        try {
            return NetUtil.isInnerIP(IPUtil.getIpAddress(request));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}