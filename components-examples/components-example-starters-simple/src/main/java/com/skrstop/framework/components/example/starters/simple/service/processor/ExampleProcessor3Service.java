package com.skrstop.framework.components.example.starters.simple.service.processor;

import com.skrstop.framework.components.example.starters.simple.constant.ProcessorContainerNameConst;
import com.skrstop.framework.components.starter.annotation.anno.server.SProcessor;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert;

/**
 * @author 蒋时华
 * @date 2023-12-04 11:13:50
 */
@SProcessor(containerName = ProcessorContainerNameConst.PROCESSOR_POOL
        , processorName = "processor3Class"
        , assertBeanName = "exampleProcessor3Service")
public class ExampleProcessor3Service implements ExampleProcessorService, ProcessorAssert<String> {


    @Override
    public boolean isSupported(String currentValue) {
        return "processor3".equals(currentValue);
    }

    @Override
    public String print() {
        return "processor3 print";
    }
}
