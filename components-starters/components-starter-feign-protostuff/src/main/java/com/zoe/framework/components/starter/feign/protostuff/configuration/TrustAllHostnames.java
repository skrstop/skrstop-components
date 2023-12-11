package com.zoe.framework.components.starter.feign.protostuff.configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author 蒋时华
 * @date 2023-12-05 10:11:45
 */
public class TrustAllHostnames implements HostnameVerifier {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }

}
