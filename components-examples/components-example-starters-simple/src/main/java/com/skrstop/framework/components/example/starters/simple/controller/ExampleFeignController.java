package com.skrstop.framework.components.example.starters.simple.controller;

import com.skrstop.framework.components.example.starters.simple.controller.feign.TestFeign;
import com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2023-11-30 17:52:44
 */
@RestController
@Slf4j
@Validated
public class ExampleFeignController {

    @Autowired
    private TestFeign testFeign;

    /**
     * mysql 插入样例
     */
//    @DisableGlobalResponse
    @GetMapping("/testFeignRemote")
    public Map<String, String> testFeignRemote() {
        return new LinkedHashMap() {{
            put("test", "aaabbb");
        }};
    }

    @DisableTransResultTypeResponse
    @GetMapping("/testFeign")
    public Object testFeign() {
        return testFeign.testFeignRemote();
    }


}
