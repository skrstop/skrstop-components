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

    /**
     * 返回指定类型的处理器
     *
     * @param containerName
     * @param key
     * @param cls
     * @return
     */
    public static <T> T getProcessorOne(String containerName, String key, Class<T> cls) {
        return (T) getProcessorOne(containerName, key);
    }

    /**
     * 获取相关处理器
     *
     * @param containerName
     * @param key
     * @return
     */
    public static Object getProcessorOne(String containerName, String key) {
        if (StrUtil.isBlank(containerName)) {
            containerName = ProcessorContext.DEFAULT_CONTAINERS_NAME;
        }
        ProcessorContainer processorContainer = constainers.get(containerName);
        if (ObjectUtil.isNull(processorContainer)) {
            throw new ProcessorException();
        }
        ProcessorEntity processorEntity = processorContainer.getProcessorEntitiesKey().get(key);
        if (ObjectUtil.isNull(processorEntity)) {
            throw new ProcessorException();
        }
        return processorEntity.getProcessor();
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

}
