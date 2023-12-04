package com.zoe.framework.components.example.starters.cloud.feign.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class RemoteServerController {

    @GetMapping("/exampleFeign1")
    public String exampleFeign1(@SpringQueryMap HashMap<String, String> params) {
        System.out.println(JSONUtil.toJsonStr(params));
        return "success 1";
    }

    @GetMapping("/exampleFeign2")
    public String exampleFeign2(@RequestParam(name = "list", required = false) List<String> list) {
        System.out.println(JSONUtil.toJsonStr(list));
        return "success 2";
    }


    @GetMapping("/exampleFeign3")
    public String exampleFeign3(@RequestParam(name = "id") String id) {
        System.out.println(id);
        return "success 3";
    }


    @PostMapping("/exampleFeign4")
    public String exampleFeign4(@RequestBody HashMap<String, String> params) {
        System.out.println(JSONUtil.toJsonStr(params));
        return "success 4";
    }

}
