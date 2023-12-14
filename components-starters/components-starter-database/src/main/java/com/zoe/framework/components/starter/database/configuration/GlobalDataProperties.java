package com.zoe.framework.components.starter.database.configuration;

import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;
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
public class GlobalDataProperties {

    /*** 是否开启sql健康检查 */
    private Boolean sqlHealthyCheck = false;

    /*** mapper class 路径, 为空则不扫描，多个用逗号分割，可以使用{@link org.mybatis.spring.annotation.MapperScan} */
    private String mapperClassLocation = "";

    /*** mapper xml 路径,多个路径用逗号分割 */
    private String mapperXmlLocation = "classpath*:mapper/*.xml";

    /*** 下划线转驼峰 */
    private Boolean mapUnderscoreToCamelCase = true;

    /*** 懒加载 */
    private Boolean lazyLoadingEnabled = true;

}
