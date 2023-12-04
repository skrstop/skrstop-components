package com.zoe.framework.components.example.starters.service;

import com.zoe.framework.components.example.starters.constant.ProcessorContainerNameConst;
import com.zoe.framework.components.starter.annotation.anno.server.SProcessor;
import com.zoe.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert;

/**
 * @author 蒋时华
 * @date 2023-12-04 11:13:50
 */
@SProcessor(containerName = ProcessorContainerNameConst.PROCESSOR_POOL
        , processorName = "processorDefaultClass"
        , assertBeanClass = ExampleProcessorDefaultService.class
        , defaultProcessor = true)
public class ExampleProcessorDefaultService implements ExampleProcessorService, ProcessorAssert<String> {


    @Override
    public boolean isSupported(String currentValue) {
        return true;
    }

    @Override
    public String print() {
        return "processor default print";
    }
}
