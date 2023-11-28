package com.jphoebe.framework.components.util.constant;

/**
 * feign 标记常量池
 *
 * @author 蒋时华
 * @date 2020-05-13 11:13:12
 */
public interface FeignConst {

    /*** feign mark */
    String USE_FEIGN_NAME = "use_feign";
    String USE_FEIGN_VALUE = "true";

    /*** feign 使用的协议 */
    String FEIGN_PROTOCOL_NAME = "feign_protocol";
    String FEIGN_PROTOCOL_VALUE_PROTOSTUFF = "protostuff";
    String FEIGN_PROTOCOL_VALUE_HTTP1 = "http/1";
    String FEIGN_PROTOCOL_VALUE_HTTP2 = "http/2";

}
