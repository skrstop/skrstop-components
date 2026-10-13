package com.skrstop.framework.components.starter.objectStorage.credential;

import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import org.junit.Assert;
import org.junit.Test;

public class DynamicBasicCOSCredentialsTest {

    @Test
    public void ok() {
        CosProperties properties = new CosProperties();
        properties.setSecretId("id");
        properties.setSecretKey("key");
        DynamicBasicCOSCredentials credentials = new DynamicBasicCOSCredentials(properties);
        Assert.assertNull(credentials.getCOSAppId());
        Assert.assertEquals("id", credentials.getCOSAccessKeyId());
        Assert.assertEquals("key", credentials.getCOSSecretKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullProperties() {
        new DynamicBasicCOSCredentials(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSecretId() {
        new DynamicBasicCOSCredentials(new CosProperties());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSecretKey() {
        CosProperties properties = new CosProperties();
        properties.setSecretId("id");
        new DynamicBasicCOSCredentials(properties);
    }
}
