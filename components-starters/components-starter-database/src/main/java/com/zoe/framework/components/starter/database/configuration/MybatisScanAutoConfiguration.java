package com.zoe.framework.components.starter.database.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;
import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.value.data.StrUtil;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 * @see MapperScannerRegistrar
 */
@Configuration
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
//@ConditionalOnExpression("T(com.zoe.framework.components.util.value.data.StrUtil).isNotBlank('${zoe.database.config.mapper-class-location:}')")
public class MybatisScanAutoConfiguration implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private String mapperClassLocation;

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + MybatisScanAutoConfiguration.class.getSimpleName() + "#" + index;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        if (StrUtil.isBlank(mapperClassLocation)) {
            return;
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);
        List<String> basePackages = StrUtil.splitTrim(mapperClassLocation, StringPoolConst.COMMA);
        if (basePackages.isEmpty()) {
            return;
        }
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        // for spring-native
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        String beanName = generateBaseBeanName(importingClassMetadata, 0);
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.mapperClassLocation = environment.getProperty(GlobalConfigConst.DATABASE_CONFIG_CLASS_CLASS_LOCATION);
    }
}
