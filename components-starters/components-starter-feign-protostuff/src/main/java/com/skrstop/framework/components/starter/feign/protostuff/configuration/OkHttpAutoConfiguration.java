package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import com.skrstop.framework.components.starter.feign.protostuff.interceptor.OkHttpResponseLogInterceptor;
import feign.Client;
import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-05-13 19:00:42
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureAfter(FeignAutoConfiguration.class)
@ConditionalOnProperty(value = "feign.okhttp.enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({GlobalHttp2Properties.class, FeignOkHttpProperties.class})
@Order(Ordered.LOWEST_PRECEDENCE)
public class OkHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({Client.class})
    public Client feignClient(okhttp3.OkHttpClient client) {
        return new feign.okhttp.OkHttpClient(client);
    }

    @Bean
    @ConditionalOnMissingBean({ConnectionPool.class})
    public ConnectionPool httpClientConnectionPool(FeignOkHttpProperties okHttpProperties
            , OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        Integer maxTotalConnections = okHttpProperties.getMaxConnections();
        Long timeToLive = okHttpProperties.getTimeToLive();
        TimeUnit ttlUnit = okHttpProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    @ConditionalOnClass(HttpMessageConverters.class)
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public HttpMessageConverters getMappingJackson2HttpMessageConverter() {
        return new HttpMessageConverters();
    }

    @Bean
    public OkHttpClient client(OkHttpClientFactory httpClientFactory
            , ConnectionPool connectionPool
            , FeignOkHttpProperties okHttpProperties
            , GlobalHttp2Properties globalHttp2Properties) {
        Boolean followRedirects = okHttpProperties.isFollowRedirects();
        Integer connectTimeout = okHttpProperties.getConnectionTimeout();
        Integer readTimeout = okHttpProperties.getReadTimeout();
        Integer writeTimeout = okHttpProperties.getWriteTimeout();
        Boolean disableSslValidation = okHttpProperties.isDisableSslValidation();
        List<Protocol> protocols = new ArrayList<>();
        if (globalHttp2Properties.isEnable()) {
            protocols.add(Protocol.H2_PRIOR_KNOWLEDGE);
        } else {
            protocols.add(Protocol.HTTP_1_1);
        }
        return httpClientFactory.createBuilder(disableSslValidation)
                .connectTimeout((long) connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout((long) readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout((long) writeTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                // 自定义请求日志拦截器
                .addInterceptor(new OkHttpResponseLogInterceptor(globalHttp2Properties.isLogInfoLevelForRequest()))
                .protocols(protocols)
                .build();

    }

}
