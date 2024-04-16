package com.skrstop.framework.components.example.starters.cloud.feign.controller;

import cn.hutool.json.JSONUtil;
import com.skrstop.framework.components.core.common.response.page.CommonPageData;
import com.skrstop.framework.components.core.common.response.page.SimplePageInfo;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignController;
import com.skrstop.framework.components.example.starters.cloud.feign.entity.DemoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-12-04 17:25:54
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/feign")
public class RemoteServerController implements RemoteFeignController {

    @Override
    @GetMapping("/exampleFeign1")
    public String exampleFeign1(@SpringQueryMap HashMap<String, String> params) {
        System.out.println(JSONUtil.toJsonStr(params));
        return "success 1";
    }

    @Override
    @GetMapping("/exampleFeign2")
    public List<String> exampleFeign2(@RequestParam(name = "list", required = false) List<String> list) {
        System.out.println(JSONUtil.toJsonStr(list));
        return list;
    }

    @Override
    @GetMapping("/exampleFeign3")
    public DemoInfo exampleFeign3(@RequestParam(name = "id") String id) {
        System.out.println(id);
        return new DemoInfo("name");
    }

    @Override
    @PostMapping("/exampleFeign4")
    public List<DemoInfo> exampleFeign4(@RequestBody HashMap<String, String> params) {
        System.out.println(JSONUtil.toJsonStr(params));
        List<DemoInfo> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new DemoInfo("name-" + i));
        }
        return result;
    }

    @Override
    @PostMapping("/exampleFeign5")
    public CommonPageData<DemoInfo> exampleFeign5(@RequestParam(name = "list", required = false) List<String> list) {
        List<DemoInfo> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new DemoInfo("name-" + i));
        }
        return CommonPageData.<DemoInfo>builder()
                .pageInfo(new SimplePageInfo(100L, 100L))
                .data(result)
                .build();
    }

    @Override
    public void exampleFeign6() {
    }

    @Override
    public Void exampleFeign7() {
        return null;
    }

}
