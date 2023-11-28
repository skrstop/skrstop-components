package cn.auntec.framework.components.starter.annotation.aspect;

import cn.auntec.framework.components.starter.annotation.PrivacyInfo;
import cn.auntec.framework.components.starter.annotation.PrivacyInfoValue;
import cn.auntec.framework.components.util.system.net.IPUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ReflectUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class PrivacyInfoAspect {

    @Pointcut("@annotation(cn.auntec.framework.components.starter.annotation.PrivacyInfo)")
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
        if (privacyInfo != null) {
            // 是否限制内网访问
            boolean limitIntranet = privacyInfo.limitIntranet();

            this.setInfo(returnValue, limitIntranet);
        }
    }

    @SuppressWarnings("unchecked")
    private void setInfo(Object value, boolean limitIntranet) {

        if (value != null) {
            PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(value.getClass());
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String name = descriptor.getName();
                if (!"class".equals(name)) {
                    if (!this.isPrimitive(descriptor)) {
                        if (value instanceof Collection || value instanceof Set) {
                            List list = (List) value;
                            list.forEach(item -> {
                                try {
                                    this.setInfo(item, limitIntranet);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } else if (value instanceof Map) {
                            Map map = (Map) value;
                            map.keySet().forEach(item -> {
                                try {
                                    this.setInfo(map.get(item), limitIntranet);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            Object property = BeanUtil.getProperty(value, descriptor.getName());
                            this.setInfo(property, limitIntranet);
                        }
                    } else {
                        // 将隐私信息置null
                        Field field = ReflectUtil.getField(value.getClass(), descriptor.getName());
                        if (field != null) {
                            PrivacyInfoValue privacyInfoValue = field.getAnnotation(PrivacyInfoValue.class);
                            if (privacyInfoValue != null) {
                                boolean innerIp = this.innerIp();
                                boolean limit = limitIntranet || privacyInfoValue.limitIntranet();
                                if (innerIp && !limit) {
                                    continue;
                                }
                                BeanUtil.setProperty(value, descriptor.getName(), null);
                            }
                        }
                    }
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
