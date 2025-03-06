package com.skrstop.framework.components.example.starters.cloud.feign.api;

import com.skrstop.framework.components.core.common.response.ListResult;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.skrstop.framework.components.example.starters.cloud.feign.entity.DemoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
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
@FeignClient(url = "http://localhost:8080"
        , path = "/feign"
        , name = "server-discovery-name" + "-TestFeign"
        , contextId = "server-discovery-name" + "-TestFeign"
        , fallbackFactory = RemoteFeignOrigin.RemoteFeignClientFallBack.class
        , configuration = {RemoteFeignOrigin.AccessClientFallBackFactory.class})
public interface RemoteFeignOrigin {

    @GetMapping("/exampleFeign1")
    Result<String> exampleFeign1(@SpringQueryMap DemoInfo params);

    @GetMapping("/exampleFeign2")
    ListResult<String> exampleFeign2(@RequestParam(name = "list", required = false) List<String> list);

    @GetMapping("/exampleFeign3")
    Result<String> exampleFeign3(@RequestParam(name = "id") String id);

    @PostMapping("/exampleFeign4")
    ListResult<DemoInfo> exampleFeign4(@RequestBody HashMap<String, String> params);

    @Slf4j
    @SuppressWarnings("unchecked")
    class RemoteFeignClientFallBack implements RemoteFeignOrigin {

        @Override
        public Result<String> exampleFeign1(DemoInfo params) {
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
        public ListResult<DemoInfo> exampleFeign4(HashMap<String, String> params) {
            return ListResult.Builder.error();
        }

    }

    @Slf4j
    class AccessClientFallBackFactory implements FallbackFactory<RemoteFeignOrigin> {
        @Override
        public RemoteFeignOrigin create(Throwable throwable) {
            log.error("TestFeign 服务调用失败: {}", ThrowableStackTraceUtil.getStackTraceStr(throwable));
            return new RemoteFeignClientFallBack();
        }
    }

}
