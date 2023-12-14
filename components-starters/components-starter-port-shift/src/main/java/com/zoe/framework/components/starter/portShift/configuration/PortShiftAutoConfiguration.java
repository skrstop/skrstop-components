package com.zoe.framework.components.starter.portShift.configuration;

import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.system.net.PortUtil;
import com.zoe.framework.components.util.value.data.NumberUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@EnableConfigurationProperties({ServerProperties.class, GlobalPortProperties.class})
@AutoConfigureBefore({ServletWebServerFactoryAutoConfiguration.class, ReactiveWebServerFactoryAutoConfiguration.class})
@Slf4j
public class PortShiftAutoConfiguration {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(
            ServerProperties serverProperties,
            GlobalPortProperties globalPortProperties) {
        return factory -> {
            if (!globalPortProperties.getEnable()) {
                return;
            }
            int basePort = serverProperties.getPort();
            String portArea = globalPortProperties.getPortArea();
            if (StrUtil.isNotBlank(portArea)) {
                List<String> split = StrUtil.splitTrim(portArea, StringPoolConst.DASH);
                if (split.size() == 2) {
                    String minPortStr = split.get(0);
                    String maxPortStr = split.get(1);
                    if (NumberUtil.isNumber(minPortStr) && NumberUtil.isNumber(maxPortStr)) {
                        int minPort = NumberUtil.toInt(minPortStr);
                        int maxPort = NumberUtil.toInt(maxPortStr);
                        if (maxPort >= minPort && minPort >= 0) {
                            if (basePort < minPort || basePort > maxPort) {
                                basePort = minPort;
                            }
                            factory.setPort(PortUtil.getServerSocketFromBasePort(basePort, minPort, maxPort));
                            return;
                        }
                    }
                }
            }
            int serverSocketFromBasePort = PortUtil.getServerSocketFromBasePort(basePort);
            if (serverSocketFromBasePort != basePort) {
                log.info("[zoe] Port shift from {} to {}", basePort, serverSocketFromBasePort);
            }
            factory.setPort(serverSocketFromBasePort);
        };
    }

}
