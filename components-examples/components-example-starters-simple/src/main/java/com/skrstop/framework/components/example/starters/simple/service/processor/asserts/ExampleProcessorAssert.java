package com.skrstop.framework.components.example.starters.simple.service.processor.asserts;

import com.skrstop.framework.components.starter.annotation.handle.server.processor.asserts.ProcessorAssert;

/**
 * @author 蒋时华
 * @date 2023-12-04 11:28:44
 */
public class ExampleProcessorAssert implements ProcessorAssert<String> {
    @Override
    public boolean isSupported(String currentValue) {
        return false;
    }
}
