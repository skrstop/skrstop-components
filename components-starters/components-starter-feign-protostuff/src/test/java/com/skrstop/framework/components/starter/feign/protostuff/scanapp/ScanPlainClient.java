package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;

@ProtostuffFeignClient("svc-plain")
public interface ScanPlainClient {

    String hello();
}
