package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import com.skrstop.framework.components.starter.feign.protostuff.constant.GlobalConfigConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.HashSet;

/**
 * @author 蒋时华
 * @date 2020-05-18 12:22:55
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.FEIGN_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalFeignProperties {

    /*** 是否开启http2 */
    private boolean enableHttp2 = false;

    /**
     * feign扫描包路径，多个路径逗号分隔，也可以使用@EnableFeignClients
     */
    private String scanPackage;
    /**
     * 关闭 自适应删除全局相应格式 功能，默认：false
     * 即：会根据FeignClient的returnType自动处理,
     * 如果不使用Result包装, 则只返回response中的data部分，访问失败则返回null
     * 如果使用Result包装, 则只返回有Result包装的对象
     * <p>
     * 注意：如果开启了自适应处理，分页接口则返回 CommonPageData
     * 注意：此参数为客户端配置，服务端配置可在starter-web中关闭 useFeignSupport 可实现服务端不返回包装格式
     * 当服务端关闭userFeignSupport时，客户端需要开启此参数，否则将类型转换错误，导致报错
     *
     * @see com.skrstop.framework.components.core.common.response.page.CommonPageData
     */
    private boolean stopAutoRemoveGlobalResponse = false;

    /**
     * 是否开启全局自适应相应格式的错误日志，默认：true
     */
    private boolean logErrorAutoRemoveGlobalResponse = true;

    /**
     * 是否开启gzip压缩，默认：true
     */
    private Boolean gzipCompress = true;

    /**
     * 是否传递请求头，默认：true
     */
    private Boolean transferHeader = true;

    /**
     * 传递请求头时需要过滤的请求头
     */
    private HashSet<String> transferHeaderIgnore = CollectionUtil.newHashSet(
            "accept",
            "accept-encoding",
            "accept-language",
            "content-length",
            "content-type"
    );

}
