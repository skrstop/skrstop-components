package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;

@ProtostuffFeignClient(value = "auto-local-app", autoLocal = true, autoLocalBasePackage = "com.skrstop.framework.components.starter.feign.protostuff.scanapp")
public interface AutoLocalAppClient {

    String hello();
}
