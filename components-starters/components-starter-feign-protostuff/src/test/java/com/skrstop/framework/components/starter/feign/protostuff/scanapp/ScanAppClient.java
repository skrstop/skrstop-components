package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;

@ProtostuffFeignClient("svc-app")
public interface ScanAppClient {

    String hello();
}
