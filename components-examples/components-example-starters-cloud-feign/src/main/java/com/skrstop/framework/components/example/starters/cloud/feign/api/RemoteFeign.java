package com.skrstop.framework.components.example.starters.cloud.feign.api;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.ListResult;
import com.skrstop.framework.components.core.common.response.PageCollectionResult;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.page.ListSimplePageData;
import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.skrstop.framework.components.example.starters.cloud.feign.entity.DemoInfo;
import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
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
        , name = "server-discovery-name" + "-TestFeign-ctrl"
        , contextId = "server-discovery-name" + "-TestFeign-ctrl"
        , fallbackFactory = RemoteFeign.RemoteFeignClientFallBack.class
        , configuration = {RemoteFeign.AccessClientFallBackFactory.class})
public interface RemoteFeign {

    @GetMapping("/exampleFeign1")
    Result<Integer> exampleFeign1(@SpringQueryMap DemoInfo params);

    @GetMapping("/exampleFeign2")
    ListResult<String> exampleFeign2(@RequestParam(name = "list", required = false) List<String> list);

    @GetMapping("/exampleFeign3")
    Result<DemoInfo> exampleFeign3(@RequestParam(name = "id") String id);

    @PostMapping("/exampleFeign4")
    ListResult<DemoInfo> exampleFeign4(@RequestBody HashMap<String, String> params);

    @PostMapping("/exampleFeign4")
    DemoInfo exampleFeign42(@RequestBody HashMap<String, String> params);

    @PostMapping("/exampleFeign5")
    PageCollectionResult<DemoInfo> exampleFeign5(@RequestParam(name = "list", required = false) List<String> list);

    @PostMapping("/exampleFeign5")
    ListSimplePageData<DemoInfo> exampleFeign51(@RequestParam(name = "list", required = false) List<String> list);

    @PostMapping("/exampleFeign6")
    DefaultResult exampleFeign6();

    @Slf4j
    @SuppressWarnings("unchecked")
    class RemoteFeignClientFallBack implements RemoteFeign {

        @Override
        public Result<Integer> exampleFeign1(DemoInfo params) {
            return Result.Builder.error();
        }

        @Override
        public ListResult<String> exampleFeign2(List<String> list) {
            return ListResult.Builder.error();
        }

        @Override
        public Result<DemoInfo> exampleFeign3(String id) {
            return Result.Builder.error();
        }


        @Override
        public ListResult<DemoInfo> exampleFeign4(HashMap<String, String> params) {
            return ListResult.Builder.error();
        }

        @Override
        public DemoInfo exampleFeign42(HashMap<String, String> params) {
            return null;
        }

        @Override
        public PageCollectionResult<DemoInfo> exampleFeign5(List<String> list) {
            return PageCollectionResult.Builder.error();
        }

        @Override
        public ListSimplePageData<DemoInfo> exampleFeign51(List<String> list) {
            return new ListSimplePageData<>();
        }

        @Override
        public DefaultResult exampleFeign6() {
            return DefaultResult.Builder.error();
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
