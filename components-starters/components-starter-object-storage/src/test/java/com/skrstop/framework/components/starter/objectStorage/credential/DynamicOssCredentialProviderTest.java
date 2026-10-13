package com.skrstop.framework.components.starter.objectStorage.credential;

import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.InvalidCredentialsException;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;
import org.junit.Assert;
import org.junit.Test;

public class DynamicOssCredentialProviderTest {

    @Test(expected = NotSupportedException.class)
    public void setCredentialsNotSupported() {
        new DynamicOssCredentialProvider(new OssProperties()).setCredentials(null);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void getCredentialsNullProperties() {
        new DynamicOssCredentialProvider(null).getCredentials();
    }

    @Test
    public void getCredentials() {
        OssProperties properties = new OssProperties();
        properties.setAccessKeyId("ak");
        properties.setAccessKeySecret("sk");
        Credentials credentials = new DynamicOssCredentialProvider(properties).getCredentials();
        Assert.assertEquals("ak", credentials.getAccessKeyId());
        Assert.assertEquals("sk", credentials.getSecretAccessKey());
    }
}
