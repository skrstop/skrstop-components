package cn.auntec.framework.components.starter.feign.protostuff.autoconfigure;

import feign.Contract;
import feign.Feign;
import feign.QueryMapEncoder;
import feign.querymap.BeanQueryMapEncoder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2020-05-13 19:00:42
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureAfter({FeignAutoConfiguration.class, FeignClientsConfiguration.class})
@Order(Ordered.LOWEST_PRECEDENCE)
public class FeignCustomAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Contract feignContract() {
//        return new feign.Contract.Default();
        return new SpringMvcContract();
    }

    @Bean
    @ConditionalOnMissingClass("org.springframework.data.domain.Pageable")
    @ConditionalOnMissingBean
    public QueryMapEncoder feignQueryMapEncoderPageable() {
        return new BeanQueryMapEncoder();
    }

}
