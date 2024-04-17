package com.skrstop.framework.components.example.starters.cloud.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 蒋时华
 * @date 2020-05-12 16:20:04
 */
@SpringBootApplication
//@EnableFeignClients("com.skrstop.framework.components.example.starters.cloud.feign.api")
public class ExampleStartersCloudFeignApplicationBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ExampleStartersCloudFeignApplicationBootstrap.class, args);
    }

}
