package com.zoe.framework.components.example.starters.controller;

import com.zoe.framework.components.core.common.response.MapResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.example.starters.service.Example1MysqlService;
import com.zoe.framework.components.starter.id.service.IdService;
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
    @GetMapping("/exampleReturn")
    public Object exampleReturn() {
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
    @GetMapping("/exampleReturnMap")
    public IResult exampleReturnMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("1", "1");
        result.put("2", "2");
        result.put("3", "3");
        return MapResult.Builder.success(result);
    }

}
