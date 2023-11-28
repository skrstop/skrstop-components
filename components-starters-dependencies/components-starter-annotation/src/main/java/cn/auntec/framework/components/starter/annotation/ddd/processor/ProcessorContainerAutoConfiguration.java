package cn.auntec.framework.components.starter.annotation.ddd.processor;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.starter.annotation.ddd.DddProcessor;
import cn.auntec.framework.components.starter.annotation.ddd.processor.asserts.DefaultAssert;
import cn.auntec.framework.components.starter.annotation.ddd.processor.asserts.ProcessorAssert;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2022-03-01 16:32:14
 */
@Configuration
@Slf4j
@SuppressWarnings("all")
public class ProcessorContainerAutoConfiguration implements ApplicationContextAware, BeanPostProcessor, PriorityOrdered {

    public final static String DEFAULT_CONTAINERS_NAME = "defaultProcessorContainer";
    private final ConcurrentHashMap<Class<?>, ProcessorAssert> processorAssertMap = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext = null;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        DddProcessor[] dddProcessors = beanClass.getAnnotationsByType(DddProcessor.class);
        if (ObjectUtil.isNull(dddProcessors) || dddProcessors.length <= 0) {
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
        ConcurrentHashMap<String, ProcessorContainer> constainers = ProcessorContext.getConstainers();
        for (DddProcessor dddProcessor : dddProcessors) {
            String containerName = dddProcessor.containerName().trim();
            if (StrUtil.isBlank(containerName)) {
                containerName = DEFAULT_CONTAINERS_NAME;
            }
            ProcessorContainer container = constainers.get(containerName);
            if (ObjectUtil.isNull(container)) {
                container = new ProcessorContainer();
                constainers.put(containerName, container);
            }
            String key = StrUtil.isBlank(dddProcessor.key()) ? beanName : dddProcessor.key();
            ProcessorAssert processorAssert = null;
            if (StrUtil.isNotBlank(dddProcessor.assertBeanName()) && ObjectUtil.isNotNull(applicationContext)) {
                // 获取容器中的bean
                Object assertBean = applicationContext.getBean(dddProcessor.assertBeanName());
                if (ObjectUtil.isNull(assertBean)) {
                    throw new AuntecRuntimeException("容器："
                            + containerName
                            + " assertBeanName："
                            + dddProcessor.assertBeanName()
                            + " 不存在！！！");
                }
                if (!ProcessorAssert.class.isAssignableFrom(assertBean.getClass())) {
                    throw new AuntecRuntimeException("容器："
                            + containerName
                            + " assertBeanName："
                            + dddProcessor.assertBeanName()
                            + " 未继承 ProcessorAssert");
                }
                processorAssert = (ProcessorAssert) assertBean;
            } else if (ObjectUtil.isNotNull(dddProcessor.assertBeanClass())
                    && ObjectUtil.isNotNull(applicationContext)
                    && !void.class.equals(dddProcessor.assertBeanClass())) {
                // 获取容器中的bean
                Object assertBean = applicationContext.getBean(dddProcessor.assertBeanClass());
                if (ObjectUtil.isNull(assertBean)) {
                    throw new AuntecRuntimeException("容器："
                            + containerName
                            + " assertBeanClass："
                            + dddProcessor.assertBeanClass().getName()
                            + " 不存在！！！");
                }
                if (!ProcessorAssert.class.isAssignableFrom(assertBean.getClass())) {
                    throw new AuntecRuntimeException("容器："
                            + containerName
                            + " assertBeanClass："
                            + dddProcessor.assertBeanClass().getName()
                            + " 未继承 ProcessorAssert");
                }
                processorAssert = (ProcessorAssert) assertBean;
            } else {
                Class<? extends ProcessorAssert> processAssertClass = dddProcessor.assertClass();
                if (ObjectUtil.isNull(processAssertClass)) {
                    processAssertClass = DefaultAssert.class;
//                throw new AuntecRuntimeException("容器：" + containerName + " 处理器：" + key + " 未指定断言，请检查！！！");
                    log.error("容器：{} 处理器：{} 未指定断言，请检查！！！当前使用默认断言处理类", containerName, key);
                }
                processorAssert = processorAssertMap.get(processAssertClass);
                if (ObjectUtil.isNull(processorAssert)) {
                    processorAssert = ReflectUtil.newInstance(processAssertClass);
                    processorAssertMap.put(processAssertClass, processorAssert);
                }
            }
            ProcessorEntity processorEntity = ProcessorEntity.builder()
                    .containerName(containerName)
                    .processor(bean)
                    .processorAssert(processorAssert)
                    .key(key)
                    .description(dddProcessor.description())
                    .defaultProcessor(dddProcessor.defaultProcessor())
                    .ordered(dddProcessor.ordered())
                    .build();
            // 添加到容器中
            if (container.getProcessorEntitiesKey().containsKey(key)) {
                throw new AuntecRuntimeException("容器：" + containerName + " 中，已存在该处理器：" + key);
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
