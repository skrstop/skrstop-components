package com.skrstop.framework.components.example.starters.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 蒋时华
 * @date 2020-05-12 16:20:04
 */
@EnableFeignClients("com.skrstop.framework.components.example.starters.simple.controller.feign")
@SpringBootApplication
public class ExampleStartersSimpleApplicationBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ExampleStartersSimpleApplicationBootstrap.class, args);
    }

}
