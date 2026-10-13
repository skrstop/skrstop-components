package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;

@ProtostuffFeignClient(value = "svc-q", qualifiers = {"q1"})
public interface ScanQClient {

    String hello();
}
