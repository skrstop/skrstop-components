package com.zoe.framework.components.example.starters.cloud.feign.controller;

import com.zoe.framework.components.example.starters.cloud.feign.api.RemoteFeign;
import com.zoe.framework.components.example.starters.cloud.feign.api.RemoteFeignOrigin;
import com.zoe.framework.components.util.stress.StressStoreUtil;
import com.zoe.framework.components.util.stress.result.StressResult;
import com.zoe.framework.components.util.value.data.CollectionUtil;
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
    private RemoteFeignOrigin remoteFeignOrigin;

    @GetMapping("/exampleRemote1")
    public String exampleRemote1() {
        System.out.println(remoteFeign.exampleFeign1(new HashMap<String, String>() {{
            put("name", "zoe");
            put("age", "18");
        }}));
        System.out.println(remoteFeign.exampleFeign2(CollectionUtil.newArrayList("zoe", "18")));
        System.out.println(remoteFeign.exampleFeign3("zoe"));
        System.out.println(remoteFeign.exampleFeign4(new HashMap<String, String>() {{
            put("name", "zoe");
            put("age", "18");
        }}));
        return "success";
    }

    @GetMapping("/exampleTest")
    public void exampleTest() {
        // 原始
        StressResult originTest = StressStoreUtil.test(20, 500, () -> {
            remoteFeignOrigin.exampleFeign1(new HashMap<String, String>() {{
                put("name", "zoe");
                put("age", "18");
                put("age1", "18");
                put("age2", "18");
                put("age3", "18");
                put("age4", "18");
                put("age5", "18");
                put("age6", "18");
            }});
            return null;
        }, 10);
        // pb
        StressResult pbTest = StressStoreUtil.test(20, 500, () -> {
            remoteFeign.exampleFeign1(new HashMap<String, String>() {{
                put("name", "zoe");
                put("age", "18");
                put("age1", "18");
                put("age2", "18");
                put("age3", "18");
                put("age4", "18");
                put("age5", "18");
                put("age6", "18");
            }});
            return null;
        }, 10);
        // 控制台输出
        System.out.println(StressStoreUtil.format(originTest));
        System.out.println(StressStoreUtil.format(pbTest));
    }

}
