package com.skrstop.framework.components.example.starters.simple.controller;

import com.skrstop.framework.components.example.starters.simple.configuration.AccessLimitConfiguration;
import com.skrstop.framework.components.example.starters.simple.constant.ProcessorContainerNameConst;
import com.skrstop.framework.components.example.starters.simple.entity.response.ExamplePrivacyInfo;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessorService;
import com.skrstop.framework.components.starter.annotation.anno.function.*;
import com.skrstop.framework.components.starter.annotation.handle.server.processor.ProcessorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
     * 限流注解 -- 自定义
     */
//    @AccessLimit(limit = 1, message = "自定义限流异常信息", beanName = "accessLimitRule1")
//    @AccessLimit(limit = 1, message = "自定义限流异常信息", beanClass = AccessLimitConfiguration.CustomAccessLimitRule.class)
    @AccessLimit(limit = 1, message = "自定义限流异常信息", defaultRule = AccessLimitConfiguration.CustomAccessLimitRule.class)
    @GetMapping("/exampleAnnotationAccessLimitCustom")
    public void exampleAnnotationAccessLimitCustom() {

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
    @ServiceLock(lockId = "serviceLock", fastFail = true, timeoutMs = 1000)
    @GetMapping("/exampleAnnotationServiceLock2")
    public void exampleAnnotationServiceLock2() {
        System.out.println("aaa");
    }

    /**
     * 数据脱敏注解
     */
    @PrivacyInfo
    @GetMapping("/exampleAnnotationPrivacyInfo")
    public ExamplePrivacyInfo exampleAnnotationPrivacyInfo() {
        return ExamplePrivacyInfo.builder()
                .valueDefault("默认处理方式")
                .valueSetNull("设置为null")
                .valueBankCard("6225145784512365")
                .valueEmail("123456789@gmail.com")
                .valueIdCard("321123199911112222")
                .valuePhone("18844446666")
                .custom("test")
                .other1(100)
                .other2(100L)
                .other3(false)
                .other4(LocalDateTime.now())
                .build();
    }

    /**
     * {@link @SProcessor} 注解使用， 用来尽量简化简单的策略模式
     *
     * @return
     */
    @GetMapping("/exampleAnnotationProcessor")
    public void exampleAnnotationProcessor() {
        ExampleProcessorService processor1 = ProcessorContext.getProcessorOne(ProcessorContainerNameConst.PROCESSOR_POOL, "processor2Class", ExampleProcessorService.class);
        System.out.println(processor1.print());

        ExampleProcessorService processor = ProcessorContext.getProcessorOneWithoutDefault(ProcessorContainerNameConst.PROCESSOR_POOL, "processor1", ExampleProcessorService.class);
        System.out.println(processor.print());

        ExampleProcessorService defaultProcessor = ProcessorContext.getProcessorOneDefault(ProcessorContainerNameConst.PROCESSOR_POOL, "use default", ExampleProcessorService.class);
        System.out.println(defaultProcessor.print());
    }

}
