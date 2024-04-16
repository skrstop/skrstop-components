package com.skrstop.framework.components.example.starters.cloud.feign.api;

import com.skrstop.framework.components.core.common.response.page.CommonPageData;
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

import java.util.ArrayList;
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
        , fallbackFactory = RemoteFeignController.RemoteFeignClientFallBack.class
        , configuration = {RemoteFeignController.AccessClientFallBackFactory.class})
public interface RemoteFeignController {

    @GetMapping("/exampleFeign1")
    String exampleFeign1(@SpringQueryMap HashMap<String, String> params);

    @GetMapping("/exampleFeign2")
    List<String> exampleFeign2(@RequestParam(name = "list", required = false) List<String> list);

    @GetMapping("/exampleFeign3")
    DemoInfo exampleFeign3(@RequestParam(name = "id") String id);

    @PostMapping("/exampleFeign4")
    List<DemoInfo> exampleFeign4(@RequestBody HashMap<String, String> params);

    @PostMapping("/exampleFeign5")
    CommonPageData<DemoInfo> exampleFeign5(@RequestParam(name = "list", required = false) List<String> list);

    @PostMapping("/exampleFeign6")
    void exampleFeign6();

    @PostMapping("/exampleFeign7")
    Void exampleFeign7();

    @Slf4j
    @SuppressWarnings("unchecked")
    class RemoteFeignClientFallBack implements RemoteFeignController {

        @Override
        public String exampleFeign1(HashMap<String, String> params) {
            return null;
        }

        @Override
        public List<String> exampleFeign2(List<String> list) {
            return null;
        }

        @Override
        public DemoInfo exampleFeign3(String id) {
            return null;
        }

        @Override
        public List<DemoInfo> exampleFeign4(HashMap<String, String> params) {
            return null;
        }

        @Override
        public CommonPageData<DemoInfo> exampleFeign5(List<String> list) {
            return CommonPageData.<DemoInfo>builder().data(new ArrayList<>()).build();
        }

        @Override
        public void exampleFeign6() {
        }

        @Override
        public Void exampleFeign7() {
            return null;
        }
    }

    @Slf4j
    class AccessClientFallBackFactory implements FallbackFactory<RemoteFeignController> {
        @Override
        public RemoteFeignController create(Throwable throwable) {
            log.error("TestFeign 服务调用失败: {}", ThrowableStackTraceUtil.getStackTraceStr(throwable));
            return new RemoteFeignClientFallBack();
        }
    }

}
