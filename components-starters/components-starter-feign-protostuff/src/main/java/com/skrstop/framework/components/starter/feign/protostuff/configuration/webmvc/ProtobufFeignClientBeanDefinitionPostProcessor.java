package com.skrstop.framework.components.starter.feign.protostuff.configuration.webmvc;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

/**
 * 在feignclient 上 自动配置 FeignMessageConverterAutoConfiguration protostuff序列化支持配置类
 *
 * @author 蒋时华
 * @date 2020-05-14 13:14:34
 */
@Configuration
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ProtobufFeignClientBeanDefinitionPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 扫描有protostuffFeignClient的注解
        String[] beanNamesForAnnotation = ((DefaultListableBeanFactory) registry).getBeanNamesForAnnotation(ProtostuffFeignClient.class);
        List<String> serviceName = CollectionUtil.newArrayList(beanNamesForAnnotation);
        ListIterator<String> iterator = serviceName.listIterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            BeanDefinition beanDefinition = registry.getBeanDefinition(next);
            // 可能是controller继承了feignclient, 需要过滤
            if (!(beanDefinition instanceof GenericBeanDefinition)) {
                iterator.remove();
                continue;
            }
            Supplier<?> instanceSupplier = ((GenericBeanDefinition) beanDefinition).getInstanceSupplier();
            if (ObjectUtil.isNull(instanceSupplier)) {
                iterator.remove();
                continue;
            }
            FeignClientFactoryBean feignClientFactoryBean = (FeignClientFactoryBean) beanDefinition.getAttribute("feignClientsRegistrarFactoryBean");
            if (ObjectUtil.isNull(feignClientFactoryBean)) {
                Object name = beanDefinition.getPropertyValues().get("name");
                Object contextId = beanDefinition.getPropertyValues().get("contextId");
                if (name == null && contextId == null) {
                    throw new RuntimeException("FeignClient attr name or contextId is all null or blank");
                }
                if (StrUtil.isAllBlank(StrUtil.toString(name, StringPoolConst.EMPTY), StrUtil.toString(contextId, StringPoolConst.EMPTY))) {
                    throw new RuntimeException("FeignClient attr name or contextId is all null or blank");
                }
                iterator.set(StrUtil.blankToDefault(contextId.toString(), name.toString()) + ".FeignClientSpecification");
            } else {
                String name = feignClientFactoryBean.getName();
                String contextId = feignClientFactoryBean.getContextId();
                if (StrUtil.isAllBlank(name, contextId)) {
                    throw new RuntimeException("FeignClient attr name or contextId is all null or blank");
                }
                iterator.set(StrUtil.blankToDefault(contextId, name) + ".FeignClientSpecification");
            }
        }

        // 自动配置protostuff 配置类
        serviceName.forEach(beanDefinitionName -> {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            ConstructorArgumentValues.ValueHolder indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues().get(1);
            Object[] configuration = (Object[]) indexedArgumentValues.getValue();
            ArrayList<Object> configurationList = CollectionUtil.toList(configuration);
            configurationList.add(FeignMessageConverterAutoConfiguration.class);
            indexedArgumentValues.setValue(configurationList.toArray());
        });
        log.debug(serviceName.toString());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
