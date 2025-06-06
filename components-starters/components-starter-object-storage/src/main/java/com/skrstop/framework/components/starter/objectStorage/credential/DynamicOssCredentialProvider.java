package com.skrstop.framework.components.starter.objectStorage.credential;

import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.common.auth.InvalidCredentialsException;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;

public class DynamicOssCredentialProvider implements CredentialsProvider {

    private OssProperties ossProperties;

    public DynamicOssCredentialProvider(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @Override
    public synchronized void setCredentials(Credentials creds) {
        throw new NotSupportedException("不支持该操作");
    }

    @Override
    public Credentials getCredentials() {
        if (this.ossProperties == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return new DefaultCredentials(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), null);
    }

}
