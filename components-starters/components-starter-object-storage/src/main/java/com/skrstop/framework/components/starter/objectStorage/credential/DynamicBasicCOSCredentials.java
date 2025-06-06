package com.skrstop.framework.components.starter.objectStorage.credential;

import com.qcloud.cos.auth.COSCredentials;
import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;

public class DynamicBasicCOSCredentials implements COSCredentials {

    private CosProperties cosProperties;

    public DynamicBasicCOSCredentials(CosProperties cosProperties) {
        super();
        if (cosProperties == null) {
            throw new IllegalArgumentException("Access key and Secret cos properties can not be null");
        }
        if (cosProperties.getSecretId() == null) {
            throw new IllegalArgumentException("Access key cannot be null.");
        }
        if (cosProperties.getSecretKey() == null) {
            throw new IllegalArgumentException("Secret key cannot be null.");
        }
        this.cosProperties = cosProperties;
    }


    @Override
    public String getCOSAppId() {
        return null;
    }

    @Override
    public String getCOSAccessKeyId() {
        return cosProperties.getSecretId();
    }

    @Override
    public String getCOSSecretKey() {
        return cosProperties.getSecretKey();
    }

}
