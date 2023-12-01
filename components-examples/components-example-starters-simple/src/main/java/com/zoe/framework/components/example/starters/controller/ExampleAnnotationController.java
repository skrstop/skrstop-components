package com.zoe.framework.components.example.starters.controller;

import com.zoe.framework.components.starter.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2023-11-30 17:52:44
 */
@RestController
@Slf4j
@Validated
public class ExampleAnnotationController {

    /**
     * 动态接口访问开关注解
     */
    @AccessControl(alias = "exampleAnnotationAccessControl", message = "自定义异常")
    @GetMapping("/exampleAnnotationAccessControl")
    public void exampleAnnotationAccessControl() {

    }

    /**
     * 限流注解
     */
    @AccessLimit(limit = 1)
    @GetMapping("/exampleAnnotationAccessLimit")
    public void exampleAnnotationAccessLimit() {

    }

    /**
     * 执行耗时日志注解
     *
     * @throws InterruptedException
     */
    @ExecuteTimeLog
    @GetMapping("/exampleAnnotationExecuteTimeLog")
    public void exampleAnnotationExecuteTimeLog() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    /**
     * 内网访问限制注解
     * 模拟：请求头增加 X-Real-IP:111.111.111.111
     *
     * @throws InterruptedException
     */
    @IntranetLimit
    @GetMapping("/exampleAnnotationIntranetLimit")
    public void exampleAnnotationIntranetLimit() {

    }

    /**
     * 同步锁注解
     */
    @ServiceLock(lockId = "serviceLock")
    @GetMapping("/exampleAnnotationServiceLock")
    public void exampleAnnotationServiceLock() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 同步锁注解2
     */
    @ServiceLock(lockId = "serviceLock", fastFail = true, timeoutMs = 11000)
    @GetMapping("/exampleAnnotationServiceLock2")
    public void exampleAnnotationServiceLock2() {
        System.out.println("aaa");
    }


}
