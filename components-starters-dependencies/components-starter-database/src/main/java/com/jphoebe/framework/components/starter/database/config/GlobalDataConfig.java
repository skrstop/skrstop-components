package com.jphoebe.framework.components.starter.database.config;

import com.jphoebe.framework.components.starter.database.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * FtpConfig class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.DATABASE_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalDataConfig {

    /*** 是否开启sql健康检查 */
    private Boolean sqlHealthyCheck;

    public GlobalDataConfig() {
        sqlHealthyCheck = false;
    }

}
