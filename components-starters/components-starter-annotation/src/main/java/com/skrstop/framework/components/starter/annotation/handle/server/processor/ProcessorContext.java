package com.skrstop.framework.components.starter.annotation.handle.server.processor;

import com.skrstop.framework.components.starter.annotation.exception.aspect.ProcessorException;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2022-03-01 16:27:09
 */
@Configuration
@SuppressWarnings("all")
@Slf4j
public class ProcessorContext {

    public final static String DEFAULT_CONTAINERS_NAME = "defaultProcessorContainer";

    @Getter
    private final static ConcurrentHashMap<String, ProcessorContainer> constainers = new ConcurrentHashMap<>();

    /**
     * 返回指定类型的处理器, 不包含默认处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getProcessorsWithoutDefault(String containerName, Object assertParam, Class<T> cls) {
        return (List<T>) getProcessors(containerName, assertParam, false);
    }

    public static <T> List<T> getProcessorsWithoutDefault(Object assertParam, Class<T> cls) {
        return (List<T>) getProcessorsWithoutDefault(null, assertParam, cls);
    }

    /**
     * 返回指定类型的处理器, 包含默认处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getProcessors(String containerName, Object assertParam, Class<T> cls) {
        return (List<T>) getProcessors(containerName, assertParam, true);
    }

    public static <T> List<T> getProcessors(Object assertParam, Class<T> cls) {
        return (List<T>) getProcessors(null, assertParam, cls);
    }

    /**
     * 获取相关处理器
     *
     * @param containerName
     * @param assertParam
     * @return
     */
    public static List<Object> getProcessors(String containerName, Object assertParam, boolean containDefault) {
        if (StrUtil.isBlank(containerName)) {
            containerName = ProcessorContext.DEFAULT_CONTAINERS_NAME;
        }
        ProcessorContainer processorContainer = constainers.get(containerName);
        if (ObjectUtil.isNull(processorContainer)) {
            throw new ProcessorException();
        }
        List<ProcessorEntity> result = new ArrayList<>();
        for (ProcessorEntity processorEntity : processorContainer.getProcessorEntities()) {
            boolean supported = processorEntity.getProcessorAssert().isSupported(assertParam);
            if (supported) {
                result.add(processorEntity);
            }
        }
        if (CollectionUtil.isNotEmpty(result) && !containDefault) {
            return result.stream().map(ProcessorEntity::getProcessor).collect(Collectors.toList());
        }
        // 寻找默认处理器
        for (ProcessorEntity processorEntity : processorContainer.getProcessorEntitiesDefault()) {
            boolean supported = processorEntity.getProcessorAssert().isSupported(assertParam);
            if (supported) {
                result.add(processorEntity);
            }
        }
        if (CollectionUtil.isEmpty(result)) {
            throw new ProcessorException();
        }
        return result.stream().map(ProcessorEntity::getProcessor).collect(Collectors.toList());
    }

    public static List<Object> getProcessors(Object assertParam, boolean containDefault) {
        return getProcessors(null, assertParam, containDefault);
    }

    /**
     * 获取第一顺序的处理器，不包含默认处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @return
     */
    public static <T> T getProcessorOneWithoutDefault(String containerName, Object assertParam, Class<T> cls) {
        return (T) getProcessorOne(containerName, assertParam, false);
    }

    public static <T> T getProcessorOneWithoutDefault(Object assertParam, Class<T> cls) {
        return (T) getProcessorOneWithoutDefault(null, assertParam, cls);
    }

    /**
     * 获取第一顺序的处理器，包含默认处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @return
     */
    public static <T> T getProcessorOne(String containerName, Object assertParam, Class<T> cls) {
        return (T) getProcessorOne(containerName, assertParam, true);
    }

    public static <T> T getProcessorOne(Object assertParam, Class<T> cls) {
        return (T) getProcessorOne(null, assertParam, cls);
    }

    /**
     * 获取单个处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @return
     */
    public static Object getProcessorOne(String containerName, Object assertParam, boolean containDefault) {
        if (StrUtil.isBlank(containerName)) {
            containerName = ProcessorContext.DEFAULT_CONTAINERS_NAME;
        }
        ProcessorContainer processorContainer = constainers.get(containerName);
        if (ObjectUtil.isNull(processorContainer)) {
            throw new ProcessorException();
        }
        ProcessorEntity result = null;
        for (ProcessorEntity processorEntity : processorContainer.getProcessorEntities()) {
            boolean supported = processorEntity.getProcessorAssert().isSupported(assertParam);
            if (supported) {
                result = processorEntity;
                break;
            }
        }
        if (ObjectUtil.isNotNull(result) && !containDefault) {
            return result.getProcessor();
        }
        // 寻找默认处理器
        for (ProcessorEntity processorEntity : processorContainer.getProcessorEntitiesDefault()) {
            boolean supported = processorEntity.getProcessorAssert().isSupported(assertParam);
            if (supported) {
                result = processorEntity;
                break;
            }
        }
        if (ObjectUtil.isNull(result)) {
            throw new ProcessorException();
        }
        return result.getProcessor();
    }

    public static Object getProcessorOne(Object assertParam, boolean containDefault) {
        return getProcessorOne(null, assertParam, containDefault);
    }

    /**
     * 返回指定类型的处理器
     *
     * @param containerName
     * @param name
     * @param cls
     * @return
     */
    public static <T> T getProcessorOneByName(String containerName, String name, Class<T> cls) {
        return (T) getProcessorOneByName(containerName, name);
    }

    public static <T> T getProcessorOneByName(String name, Class<T> cls) {
        return (T) getProcessorOneByName(null, name, cls);
    }

    /**
     * 获取相关处理器
     *
     * @param containerName
     * @param name
     * @return
     */
    public static Object getProcessorOneByName(String containerName, String name) {
        if (StrUtil.isBlank(containerName)) {
            containerName = ProcessorContext.DEFAULT_CONTAINERS_NAME;
        }
        ProcessorContainer processorContainer = constainers.get(containerName);
        if (ObjectUtil.isNull(processorContainer)) {
            throw new ProcessorException();
        }
        ProcessorEntity processorEntity = processorContainer.getProcessorEntitiesKey().get(name);
        if (ObjectUtil.isNull(processorEntity)) {
            throw new ProcessorException();
        }
        return processorEntity.getProcessor();
    }

    public static Object getProcessorOneByName(String name) {
        return getProcessorOneByName(null, name);
    }


    /**
     * 获取指定类型的第一顺序默认处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @return
     */
    public static <T> T getProcessorOneDefault(String containerName, Object assertParam, Class<T> cls) {
        return (T) getProcessorOneDefault(containerName, assertParam);
    }

    public static <T> T getProcessorOneDefault(Object assertParam, Class<T> cls) {
        return (T) getProcessorOneDefault(null, assertParam, cls);
    }

    /**
     * 获取单个默认处理器
     *
     * @param containerName
     * @param assertParam
     * @param containDefault
     * @return
     */
    public static Object getProcessorOneDefault(String containerName, Object assertParam) {
        if (StrUtil.isBlank(containerName)) {
            containerName = ProcessorContext.DEFAULT_CONTAINERS_NAME;
        }
        ProcessorContainer processorContainer = constainers.get(containerName);
        if (ObjectUtil.isNull(processorContainer)) {
            throw new ProcessorException();
        }
        ProcessorEntity result = null;
        // 寻找默认处理器
        for (ProcessorEntity processorEntity : processorContainer.getProcessorEntitiesDefault()) {
            boolean supported = processorEntity.getProcessorAssert().isSupported(assertParam);
            if (supported) {
                result = processorEntity;
                break;
            }
        }
        if (ObjectUtil.isNull(result)) {
            throw new ProcessorException();
        }
        return result.getProcessor();
    }

    public static Object getProcessorOneDefault(Object assertParam) {
        return getProcessorOneDefault(null, assertParam);
    }

    /**
     * 获取指定类型的默认处理器
     *
     * @param containerName
     * @param assertParam
     * @return
     */
    public static <T> List<T> getProcessorsDefault(String containerName, Object assertParam, Class<T> cls) {
        return (List<T>) getProcessorsDefault(containerName, assertParam);
    }

    public static <T> List<T> getProcessorsDefault(Object assertParam, Class<T> cls) {
        return (List<T>) getProcessorsDefault(null, assertParam, cls);
    }

    /**
     * 获取相关默认处理器
     *
     * @param containerName
     * @param assertParam
     * @return
     */
    public static List<Object> getProcessorsDefault(String containerName, Object assertParam) {
        if (StrUtil.isBlank(containerName)) {
            containerName = ProcessorContext.DEFAULT_CONTAINERS_NAME;
        }
        ProcessorContainer processorContainer = constainers.get(containerName);
        if (ObjectUtil.isNull(processorContainer)) {
            throw new ProcessorException();
        }
        List<ProcessorEntity> result = new ArrayList<>();
        // 寻找默认处理器
        for (ProcessorEntity processorEntity : processorContainer.getProcessorEntitiesDefault()) {
            boolean supported = processorEntity.getProcessorAssert().isSupported(assertParam);
            if (supported) {
                result.add(processorEntity);
            }
        }
        if (CollectionUtil.isEmpty(result)) {
            throw new ProcessorException();
        }
        return result.stream().map(ProcessorEntity::getProcessor).collect(Collectors.toList());
    }

    public static List<Object> getProcessorsDefault(Object assertParam) {
        return getProcessorsDefault(null, assertParam);
    }

}
