package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScanAppClientController implements ScanAppClient {

    @Override
    public String hello() {
        return "hello";
    }
}
