package com.skrstop.framework.components.example.starters.simple.controller.feign;

import com.skrstop.framework.components.core.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author 蒋时华
 * @date 2025-02-12 16:45:56
 * @since 1.0.0
 */
@FeignClient(name = "TestFeign", url = "http://127.0.0.1:8081")
public interface TestFeign {

    @GetMapping("/testFeignRemote")
    Map<String, String> testFeignRemote();

    @GetMapping("/testFeignRemote")
    Result<Map<String, String>> testFeignRemote2();

}
