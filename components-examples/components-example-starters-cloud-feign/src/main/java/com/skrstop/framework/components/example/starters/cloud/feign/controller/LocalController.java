package com.skrstop.framework.components.example.starters.cloud.feign.controller;

import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeign;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignController;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignOrigin;
import com.skrstop.framework.components.util.stress.StressStoreUtil;
import com.skrstop.framework.components.util.stress.result.StressResult;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author 蒋时华
 * @date 2023-12-04 17:25:54
 */
@RestController
@Validated
@Slf4j
@EnableFeignClients
public class LocalController {

    @Autowired
    private RemoteFeign remoteFeign;
    @Autowired
    private RemoteFeignController remoteFeignController;
    @Autowired
    private RemoteFeignOrigin remoteFeignOrigin;

    @GetMapping("/exampleRemote1")
    public String exampleRemote1() {

        System.out.println("测试打印：" + remoteFeign.exampleFeign51(CollectionUtil.newArrayList("skrstop", "18")));
//        System.out.println("测试打印：" + remoteFeign.exampleFeign5(CollectionUtil.newArrayList("skrstop", "18")));
//
//        System.out.println("测试打印：" + remoteFeign.exampleFeign1(DemoInfo.builder().name("aaaaa").build()));
//        System.out.println("测试打印：" + remoteFeign.exampleFeign2(CollectionUtil.newArrayList("skrstop", "18")));
//        System.out.println("测试打印：" + remoteFeign.exampleFeign3("skrstop"));
//        ListResult<DemoInfo> listResult = remoteFeign.exampleFeign4(new HashMap<String, String>() {{
//            put("name", "skrstop");
//            put("age", "18");
//        }});
//        System.out.println("测试打印：" + listResult);
//        System.out.println("测试打印：" + remoteFeign.exampleFeign5(CollectionUtil.newArrayList("skrstop", "18")));
//        System.out.println("测试打印：" + remoteFeign.exampleFeign6());
//
//        System.out.println("测试打印：without result");
//        Integer s = remoteFeignController.exampleFeign1(DemoInfo.builder().name("aaaaa").build());
//        System.out.println("测试打印：" + s);
//        System.out.println("测试打印：" + remoteFeignController.exampleFeign2(CollectionUtil.newArrayList("skrstop", "18")));
//        System.out.println("测试打印：" + remoteFeignController.exampleFeign3("skrstop"));
//        System.out.println("测试打印：" + remoteFeignController.exampleFeign4(new HashMap<String, String>() {{
//            put("name", "skrstop");
//            put("age", "18");
//        }}));
////
//        System.out.println("测试打印：" + remoteFeignController.exampleFeign5(CollectionUtil.newArrayList("skrstop", "18")));
//        System.out.println("测试打印：6");
        return "success";
    }

    @GetMapping("/exampleTest")
    public void exampleTest() {
        // 原始
        StressResult originTest = StressStoreUtil.test(4, 5000, () -> {
            remoteFeignOrigin.exampleFeign4(new HashMap<String, String>() {{
                put("name", "skrstop");
                put("age", "18");
            }});
        }, 10);
        // pb
        StressResult pbTest = StressStoreUtil.test(4, 5000, () -> {
            remoteFeignController.exampleFeign4(new HashMap<String, String>() {{
                put("name", "skrstop");
                put("age", "18");
            }});
        }, 10);
        // 控制台输出
        System.out.println("原始压测\n" + StressStoreUtil.format(originTest));
        System.out.println("pb压测\n" + StressStoreUtil.format(pbTest));
    }

}
