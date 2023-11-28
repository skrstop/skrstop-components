package cn.auntec.framework.components.starter.id.config;

import cn.auntec.framework.components.starter.id.constant.GenerateTypeConst;
import cn.auntec.framework.components.starter.id.constant.GlobalConfigConst;
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
@ConfigurationProperties(GlobalConfigConst.ID_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalIdConfig {

    /*** 是否开启id service */
    private Boolean enable;

    /**
     * id 生成策略
     * {@link GenerateTypeConst}
     */
    private int generateType;

    /*** 终端id */
    private long workerId;

    /*** 数据中心id */
    private long dataCenterId;

    public GlobalIdConfig() {
        this.enable = true;
        this.generateType = GenerateTypeConst.LOCAL;
        this.workerId = 1;
        this.dataCenterId = 1;
    }

}
