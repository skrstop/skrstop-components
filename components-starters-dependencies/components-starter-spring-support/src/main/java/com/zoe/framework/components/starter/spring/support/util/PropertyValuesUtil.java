package com.zoe.framework.components.starter.spring.support.util;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * {@link PropertyValues} Utilities
 *
 * @author 蒋时华
 * @see PropertyValues
 * @since 2017.01.19
 */
public abstract class PropertyValuesUtil {

    /**
     * Get Sub {@link PropertyValues} from {@link ConfigurableEnvironment}
     *
     * @param environment {@link ConfigurableEnvironment}
     * @param prefix      the prefix of property name
     * @return {@link PropertyValues}
     */
    public static PropertyValues getSubPropertyValues(
            ConfigurableEnvironment environment, String prefix) {

        Map<String, Object> subProperties = PropertySourcesUtil.getSubProperties(environment.getPropertySources(), environment, prefix);

        PropertyValues subPropertyValues = new MutablePropertyValues(subProperties);

        return subPropertyValues;
    }
}
