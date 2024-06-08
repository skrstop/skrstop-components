package com.skrstop.framework.components.starter.annotation.handle.server.processor;

import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.annotation.anno.server.SProcessor;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.DefaultAssert;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2022-03-01 16:32:14
 */
@Slf4j
@SuppressWarnings("all")
public class ProcessorContainerConfiguration implements ApplicationContextAware, BeanPostProcessor, PriorityOrdered {

    public final static String DEFAULT_CONTAINERS_NAME = "defaultProcessorContainer";
    private final ConcurrentHashMap<Class<?>, ProcessorAssert> processorAssertMap = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext = null;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        SProcessor[] sProcessors = beanClass.getAnnotationsByType(SProcessor.class);
        if (ObjectUtil.isNull(sProcessors) || sProcessors.length <= 0) {
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
        ConcurrentHashMap<String, ProcessorContainer> constainers = ProcessorContext.getConstainers();
        for (SProcessor sProcessor : sProcessors) {
            // 容器名
            String containerName = sProcessor.containerName().trim();
            if (StrUtil.isBlank(containerName)) {
                containerName = DEFAULT_CONTAINERS_NAME;
            }
            // 处理器名
            ProcessorContainer container = constainers.get(containerName);
            if (ObjectUtil.isNull(container)) {
                container = new ProcessorContainer();
                constainers.put(containerName, container);
            }
            // 未设置处理器名字，默认使用beanName
            String key = StrUtil.isBlank(sProcessor.processorName()) ? beanName : sProcessor.processorName();
            ProcessorAssert processorAssert = null;
            if (StrUtil.isNotBlank(sProcessor.assertBeanName()) && ObjectUtil.isNotNull(applicationContext)) {
                // 获取容器中的bean
                Object assertBean = null;
                if (beanName.equals(sProcessor.assertBeanName())) {
                    // 处理类中实现了断言类
                    assertBean = bean;
                } else {
                    // 获取容器中的bean
                    assertBean = applicationContext.getBean(sProcessor.assertBeanName());
                }
                if (ObjectUtil.isNull(assertBean)) {
                    throw new SkrstopRuntimeException("容器："
                            + containerName
                            + " assertBeanName："
                            + sProcessor.assertBeanName()
                            + " 不存在！！！");
                }
                if (!ProcessorAssert.class.isAssignableFrom(assertBean.getClass())) {
                    throw new SkrstopRuntimeException("容器："
                            + containerName
                            + " assertBeanName："
                            + sProcessor.assertBeanName()
                            + " 未继承 ProcessorAssert");
                }
                processorAssert = (ProcessorAssert) assertBean;
            } else if (ObjectUtil.isNotNull(sProcessor.assertBeanClass())
                    && ObjectUtil.isNotNull(applicationContext)
                    && !void.class.equals(sProcessor.assertBeanClass())) {
                Object assertBean = null;
                if (bean.getClass().equals(sProcessor.assertBeanClass())) {
                    // 处理类中实现了断言类
                    assertBean = bean;
                } else {
                    // 获取容器中的bean
                    assertBean = applicationContext.getBean(sProcessor.assertBeanClass());
                }
                if (ObjectUtil.isNull(assertBean)) {
                    throw new SkrstopRuntimeException("容器："
                            + containerName
                            + " assertBeanClass："
                            + sProcessor.assertBeanClass().getName()
                            + " 不存在！！！");
                }
                if (!ProcessorAssert.class.isAssignableFrom(assertBean.getClass())) {
                    throw new SkrstopRuntimeException("容器："
                            + containerName
                            + " assertBeanClass："
                            + sProcessor.assertBeanClass().getName()
                            + " 未继承 ProcessorAssert");
                }
                processorAssert = (ProcessorAssert) assertBean;
            } else if (ObjectUtil.isNotNull(sProcessor.assertClass())
                    && !void.class.equals(sProcessor.assertBeanClass())
                    && ProcessorAssert.class.isAssignableFrom(sProcessor.assertClass())) {
                Class<? extends ProcessorAssert> processAssertClass = (Class<? extends ProcessorAssert>) sProcessor.assertClass();
                processorAssert = processorAssertMap.get(processAssertClass);
                if (ObjectUtil.isNull(processorAssert)) {
                    processorAssert = ReflectUtil.newInstance(processAssertClass);
                    processorAssertMap.put(processAssertClass, processorAssert);
                }
            } else if (bean instanceof ProcessorAssert) {
                // bean本身实现了ProcessorAssert接口
                processorAssert = (ProcessorAssert) bean;
            }
            if (ObjectUtil.isNull(processorAssert)) {
                // 没有找到任何断言实现类， 使用默认的断言实现类
                Class<? extends ProcessorAssert> processAssertClass = DefaultAssert.class;
                processorAssert = processorAssertMap.get(processAssertClass);
                if (ObjectUtil.isNull(processorAssert)) {
                    processorAssert = ReflectUtil.newInstance(processAssertClass);
                    processorAssertMap.put(processAssertClass, processorAssert);
                }
                log.error("容器：{} 处理器：{} 未指定断言，请检查！！！当前使用默认断言处理类", containerName, key);
            }
            ProcessorEntity processorEntity = ProcessorEntity.builder()
                    .containerName(containerName)
                    .processor(bean)
                    .processorAssert(processorAssert)
                    .key(key)
                    .description(sProcessor.description())
                    .defaultProcessor(sProcessor.defaultProcessor())
                    .ordered(sProcessor.ordered())
                    .build();
            // 添加到容器中
            if (container.getProcessorEntitiesKey().containsKey(key)) {
                throw new SkrstopRuntimeException("容器：" + containerName + " 中，已存在该处理器：" + key);
            }
            if (processorEntity.isDefaultProcessor()) {
                container.getProcessorEntitiesDefault().add(processorEntity);
            } else {
                container.getProcessorEntities().add(processorEntity);
            }
            container.getProcessorEntitiesKey().put(key, processorEntity);
            // 初始化
            container.init();
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
