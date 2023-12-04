package com.zoe.framework.components.example.starters.cloud.feign.api;

import com.zoe.framework.components.core.common.response.ListResult;
import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.zoe.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-12-04 17:15:28
 */
// cloud version
//@ProtostuffFeignClient(name = "server discovery name"
//        , path = "/feign"
//        , contextId = "server discovery name" + "-TestFeign"
//        , fallbackFactory = TestFeign.TestFeignClientFallBack.class
//        , configuration = {TestFeign.AccessClientFallBackFactory.class})
// Local version
@ProtostuffFeignClient(url = "http://localhost:8080"
        , path = "/feign"
        , name = "server-discovery-name" + "-TestFeign"
        , contextId = "server-discovery-name" + "-TestFeign"
        , fallbackFactory = RemoteFeign.RemoteFeignClientFallBack.class
        , configuration = {RemoteFeign.AccessClientFallBackFactory.class})
public interface RemoteFeign {

    @GetMapping("/exampleFeign1")
    Result<String> exampleFeign1(@SpringQueryMap HashMap<String, String> params);

    @GetMapping("/exampleFeign2")
    ListResult<String> exampleFeign2(@RequestParam(name = "list", required = false) List<String> list);


    @GetMapping("/exampleFeign3")
    Result<String> exampleFeign3(@RequestParam(name = "id") String id);


    @PostMapping("/exampleFeign4")
    Result<String> exampleFeign4(@RequestBody HashMap<String, String> params);

    @Slf4j
    @SuppressWarnings("unchecked")
    class RemoteFeignClientFallBack implements RemoteFeign {

        @Override
        public Result<String> exampleFeign1(HashMap<String, String> params) {
            return Result.Builder.error();
        }

        @Override
        public ListResult<String> exampleFeign2(List<String> list) {
            return ListResult.Builder.error();
        }

        @Override
        public Result<String> exampleFeign3(String id) {
            return Result.Builder.error();
        }

        @Override
        public Result<String> exampleFeign4(HashMap<String, String> params) {
            return Result.Builder.error();
        }
    }

    @Slf4j
    class AccessClientFallBackFactory implements FallbackFactory<RemoteFeign> {
        @Override
        public RemoteFeign create(Throwable throwable) {
            log.error("TestFeign 服务调用失败: {}", ThrowableStackTraceUtil.getStackTraceStr(throwable));
            return new RemoteFeignClientFallBack();
        }
    }

}
