package com.skrstop.framework.components.example.starters.simple.controller;

import com.skrstop.framework.components.core.common.response.MapResult;
import com.skrstop.framework.components.core.common.response.PageLinkedSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.example.starters.simple.service.Example1MysqlService;
import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.starter.web.response.DisableGlobalResponse;
import com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author 蒋时华
 * @date 2023-11-30 17:52:44
 */
@RestController
@Slf4j
@Validated
public class ExampleReturnController {

    @Autowired
    private Example1MysqlService example1MysqlService;
    @Autowired
    private IdService idService;

    /**
     * 返回值样例
     *
     * @return
     */
    @GetMapping("/exampleReturn1")
    public Object exampleReturn1() {
        HashMap<String, String> result = new HashMap<>();
        result.put("1", "1");
        result.put("2", "2");
        result.put("3", "3");
        return result;
    }

    @DisableGlobalResponse
    @GetMapping("/exampleReturn2")
    public Object exampleReturn2() {
        HashMap<String, String> result = new HashMap<>();
        result.put("1", "1");
        result.put("2", "2");
        result.put("3", "3");
        return result;
    }

    /**
     * 返回值样例
     *
     * @return
     */
    @GetMapping("/exampleReturnMap1")
    public IResult exampleReturnMap1() {
        HashMap<String, String> result = new HashMap<>();
        result.put("1", "1");
        result.put("2", "2");
        result.put("3", "3");
        return MapResult.Builder.success(result);
    }

    @DisableTransResultTypeResponse
    @GetMapping("/exampleReturnMap2")
    public IResult exampleReturnMap2() {
        HashMap<String, String> result = new HashMap<>();
        result.put("1", "1");
        result.put("2", "2");
        result.put("3", "3");
        return MapResult.Builder.success(result);
    }

    /**
     * page样例
     *
     * @return
     */
    @GetMapping("/exampleReturnPage")
    public IPageResult exampleReturnPage() {
        return PageLinkedSetResult.Builder.success(10, 100);
    }

}
