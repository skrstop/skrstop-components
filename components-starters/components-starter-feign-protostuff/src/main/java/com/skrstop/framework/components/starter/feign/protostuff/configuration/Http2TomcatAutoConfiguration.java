package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({TomcatServletWebServerFactory.class, Http2Protocol.class})
@EnableConfigurationProperties(GlobalFeignProperties.class)
public class Http2TomcatAutoConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private GlobalFeignProperties globalFeignProperties;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {

        if (!globalFeignProperties.isEnableHttp2()) {
            return;
        }
        factory.addProtocolHandlerCustomizers(s -> s.addUpgradeProtocol(new Http2Protocol()));
    }

}
