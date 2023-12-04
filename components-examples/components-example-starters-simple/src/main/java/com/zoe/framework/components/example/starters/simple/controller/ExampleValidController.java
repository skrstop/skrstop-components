package com.zoe.framework.components.example.starters.simple.controller;

import com.zoe.framework.components.core.common.response.DefaultResult;
import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.example.starters.simple.entity.request.ExampleRequestParam;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.validate.ParameterValidateUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-11-30 17:52:44
 */
@RestController
@Slf4j
@Validated
public class ExampleValidController {


    /**
     * 参数验证样例
     *
     * @param val
     * @return
     */
    @GetMapping("/exampleValid")
    public IResult exampleValid(@NotBlank(message = "val不能为空") String val) {
        return DefaultResult.Builder.success();
    }

    /**
     * 参数验证样例
     *
     * @param requestParam
     * @return
     */
    @GetMapping("/exampleValid1")
    public IResult exampleValid1(ExampleRequestParam requestParam) {
        List<String> errorMsg = ParameterValidateUtil.validateMessage(requestParam);
        if (CollectionUtil.isNotEmpty(errorMsg)) {
            return Result.Builder.error(errorMsg);
        }
        return DefaultResult.Builder.success();
    }

    /**
     * 参数验证样例
     *
     * @param requestParam
     * @return
     */
    @GetMapping("/exampleValidGet")
    public IResult exampleValidGet(@Validated ExampleRequestParam requestParam) {
        return DefaultResult.Builder.success();
    }

    /**
     * 参数验证样例
     *
     * @param requestParam
     * @return
     */
    @PostMapping("/exampleValidPost")
    public IResult exampleValidPost(@RequestBody @Validated ExampleRequestParam requestParam) {
        return DefaultResult.Builder.success();
    }


}
