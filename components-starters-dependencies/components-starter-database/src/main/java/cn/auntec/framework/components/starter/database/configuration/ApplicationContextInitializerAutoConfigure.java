package cn.auntec.framework.components.starter.database.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ApplicationContextInitializerAutoConfigure implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static volatile boolean druidInfoSet = false;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (druidInfoSet) {
            return;
        }
        // 关闭druid 默认连接池检查，使用select 1
        log.info("关闭druid默认连接池检查[usePingMethod]，使用select 1");
        System.setProperty("druid.mysql.usePingMethod", "false");
        druidInfoSet = true;
    }


}
