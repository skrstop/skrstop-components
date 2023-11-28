/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.auntec.framework.components.starter.spring.support.util;

import cn.auntec.framework.components.util.value.data.ObjectUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.springframework.util.ObjectUtils.containsElement;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 * {@link BeanFactory} Utilities class
 *
 * @author 蒋时华
 * @since 1.0.2
 */
public abstract class BeanFactoryUtil {

    /**
     * Get optional Bean
     *
     * @param beanFactory {@link ListableBeanFactory}
     * @param beanName    the name of Bean
     * @param beanType    the {@link Class type} of Bean
     * @param <T>         the {@link Class type} of Bean
     * @return A bean if present , or <code>null</code>
     */
    public static <T> T getOptionalBean(
            ListableBeanFactory beanFactory, String beanName, Class<T> beanType) {

        if (!hasText(beanName)) {
            return null;
        }

        String[] beanNames = ObjectUtil.of(beanName);

        List<T> beans = getBeans(beanFactory, beanNames, beanType);

        return CollectionUtils.isEmpty(beans) ? null : beans.get(0);
    }

    /**
     * Gets name-matched Beans from {@link ListableBeanFactory BeanFactory}
     *
     * @param beanFactory {@link ListableBeanFactory BeanFactory}
     * @param beanNames   the names of Bean
     * @param beanType    the {@link Class type} of Bean
     * @param <T>         the {@link Class type} of Bean
     * @return the read-only and non-null {@link List} of Bean names
     */
    public static <T> List<T> getBeans(
            ListableBeanFactory beanFactory, String[] beanNames, Class<T> beanType) {

        if (isEmpty(beanNames)) {
            return emptyList();
        }

        String[] allBeanNames = org.springframework.beans.factory.BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, beanType);

        List<T> beans = new ArrayList<T>(beanNames.length);

        for (String beanName : beanNames) {
            if (containsElement(allBeanNames, beanName)) {
                beans.add(beanFactory.getBean(beanName, beanType));
            }
        }

        return unmodifiableList(beans);
    }
}
