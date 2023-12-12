package com.zoe.framework.components.starter.web.configuration;

import com.zoe.framework.components.starter.web.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * GlobalResponseConfig class
 *
 * @author 蒋时华
 * @date 2019/6/4
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.EXCEPTION_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionProperties {

    /*** 是否开启统一异常处理 */
    private Boolean enable = true;
    /*** 是否有错误页面展示 */
    private Boolean hasHtmlError = false;
    /*** 日志是否打印业务业务异常 */
    private Boolean logBusinessServiceException = false;

}
