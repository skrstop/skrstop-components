package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.net.ssl.SSLSession;

public class TrustAllTest {

    @Test
    public void trustAllHostnames() {
        Assert.assertTrue(new TrustAllHostnames().verify("any", null));
        SSLSession session = Mockito.mock(SSLSession.class);
        Assert.assertTrue(new TrustAllHostnames().verify("any", session));
    }

    @Test
    public void disableValidationTrustManager() throws java.security.cert.CertificateException {
        DisableValidationTrustManager trustManager = new DisableValidationTrustManager();
        trustManager.checkClientTrusted(new java.security.cert.X509Certificate[0], "RSA");
        trustManager.checkServerTrusted(new java.security.cert.X509Certificate[0], "RSA");
        Assert.assertEquals(0, trustManager.getAcceptedIssuers().length);
    }
}
