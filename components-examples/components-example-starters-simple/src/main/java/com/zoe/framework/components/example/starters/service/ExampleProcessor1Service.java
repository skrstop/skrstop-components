package com.zoe.framework.components.example.starters.service;

import com.zoe.framework.components.example.starters.constant.ProcessorContainerNameConst;
import com.zoe.framework.components.starter.annotation.anno.server.SProcessor;
import com.zoe.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert;

/**
 * @author 蒋时华
 * @date 2023-12-04 11:13:50
 */
@SProcessor(containerName = ProcessorContainerNameConst.PROCESSOR_POOL
        , processorName = "processor1Class"
        , assertClass = ExampleProcessor1Service.AssertProcessor.class)
public class ExampleProcessor1Service implements ExampleProcessorService {

    /**
     * 处理器断言类
     */
    public static class AssertProcessor implements ProcessorAssert<String> {

        @Override
        public boolean isSupported(String currentValue) {
            return "processor1".equals(currentValue);
        }
    }


    @Override
    public String print() {
        return "processor1 print";
    }
}
