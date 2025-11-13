package com.skrstop.framework.components.starter.annotation.handle.server.processor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2022-03-01 16:32:14
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProcessorContainer implements Serializable {
    @Serial
    private static final long serialVersionUID = -3013768037305729417L;

    private final List<ProcessorEntity> processorEntities;
    private final List<ProcessorEntity> processorEntitiesDefault;
    private final ConcurrentHashMap<String, ProcessorEntity> processorEntitiesKey;

    public ProcessorContainer() {
        this.processorEntities = new ArrayList<>();
        this.processorEntitiesDefault = new ArrayList<>();
        this.processorEntitiesKey = new ConcurrentHashMap<>();
    }

    public void init() {
        // init sort
        processorEntities.sort(Comparator.comparingInt(ProcessorEntity::getOrdered));
        processorEntitiesDefault.sort(Comparator.comparingInt(ProcessorEntity::getOrdered));
    }

}
