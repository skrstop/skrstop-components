package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;

@ProtostuffFeignClient(value = "auto-local-plain", autoLocal = true, autoLocalBasePackage = "com.skrstop.framework.components.starter.feign.protostuff.scanapp")
public interface AutoLocalPlainClient {

    String hello();
}
