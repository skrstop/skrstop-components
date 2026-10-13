package com.skrstop.framework.components.starter.feign.protostuff.scanapp;

public class AutoLocalPlainClientImpl implements AutoLocalPlainClient {

    @Override
    public String hello() {
        return "hello";
    }
}
