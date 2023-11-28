package cn.auntec.framework.components.starter.port.shift.config;

import cn.auntec.framework.components.starter.port.shift.constant.GlobalConfigConst;
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
@ConfigurationProperties(GlobalConfigConst.HTTP_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalPortConfig {

    /*** 是否开启端口偏移 */
    private Boolean enable;

    /*** 指定具体端口捉着端口范围， eg. 8080 / 8080-9090 */
    private String portArea;

    public GlobalPortConfig() {
        this.enable = true;
        this.portArea = "";
    }

}
