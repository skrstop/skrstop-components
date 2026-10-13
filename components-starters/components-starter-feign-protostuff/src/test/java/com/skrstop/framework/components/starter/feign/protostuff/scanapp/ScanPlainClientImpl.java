package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

public class ScanPlainClientImpl implements ScanPlainClient {

    @Override
    public String hello() {
        return "hello";
    }
}
