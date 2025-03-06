package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import com.skrstop.framework.components.starter.feign.protostuff.interceptor.OkHttpResponseLogInterceptor;
import feign.Client;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-05-13 19:00:42
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@ConditionalOnProperty(value = "spring.cloud.openfeign.ok-http.enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({FeignOkHttpProperties.class, GlobalFeignProperties.class})
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class OkHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({Client.class})
    public Client feignClient(okhttp3.OkHttpClient client) {
        return new feign.okhttp.OkHttpClient(client);
    }

    @Bean
    @ConditionalOnMissingBean({ConnectionPool.class})
    public ConnectionPool httpClientConnectionPool(FeignOkHttpProperties okHttpProperties) {
        int maxTotalConnections = okHttpProperties.getMaxConnections();
        long timeToLive = okHttpProperties.getTimeToLive();
        TimeUnit ttlUnit = okHttpProperties.getTimeToLiveUnit();
        return new ConnectionPool(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    @ConditionalOnClass(HttpMessageConverters.class)
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public HttpMessageConverters getMappingJackson2HttpMessageConverter() {
        return new HttpMessageConverters();
    }

    @Bean
    public OkHttpClient client(ConnectionPool connectionPool
            , FeignOkHttpProperties okHttpProperties
            , GlobalFeignProperties globalFeignProperties) {
        boolean followRedirects = okHttpProperties.isFollowRedirects();
        int connectTimeout = okHttpProperties.getConnectionTimeout();
        int readTimeout = okHttpProperties.getReadTimeout();
        int writeTimeout = okHttpProperties.getWriteTimeout();
        boolean disableSslValidation = okHttpProperties.isDisableSslValidation();
        List<Protocol> protocols = new ArrayList<>();
        if (globalFeignProperties.isEnableHttp2()) {
            protocols.add(Protocol.H2_PRIOR_KNOWLEDGE);
        } else {
            protocols.add(Protocol.HTTP_1_1);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (disableSslValidation) {
            try {
                X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
                TrustManager[] trustManagers = new TrustManager[1];
                trustManagers[0] = disabledTrustManager;
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagers, new java.security.SecureRandom());
                SSLSocketFactory disabledSSLSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(disabledSSLSocketFactory, disabledTrustManager);
                builder.hostnameVerifier(new TrustAllHostnames());
            } catch (NoSuchAlgorithmException e) {
                log.warn("Error setting SSLSocketFactory in OKHttpClient", e);
            } catch (KeyManagementException e) {
                log.warn("Error setting SSLSocketFactory in OKHttpClient", e);
            }
        }
        return builder
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .retryOnConnectionFailure(true)
                .connectionPool(connectionPool)
                // 自定义请求日志拦截器
                .addInterceptor(new OkHttpResponseLogInterceptor(okHttpProperties.isLogInfoLevelForRequest()))
                .protocols(protocols)
                .build();

    }

}
