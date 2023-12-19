package com.skrstop.framework.components.starter.spring.support.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.beans.factory.BeanFactoryUtils.beanOfTypeIncludingAncestors;

/**
 * Bean Utilities Class
 *
 * @author 蒋时华
 * @since 2017.01.13
 */
@Slf4j
@UtilityClass
public class BeanUtil {

    private static final String[] EMPTY_BEAN_NAMES = new String[0];

    /**
     * Is Bean Present or not?
     *
     * @param beanFactory {@link ListableBeanFactory}
     * @param beanClass   The {@link Class} of Bean
     * @return If present , return <code>true</code> , or <code>false</code>
     */
    public static boolean isBeanPresent(ListableBeanFactory beanFactory, Class<?> beanClass) {

        return isBeanPresent(beanFactory, beanClass, false);
    }

    /**
     * Is Bean Present or not?
     *
     * @param beanFactory        {@link ListableBeanFactory}
     * @param beanClass          The {@link Class} of Bean
     * @param includingAncestors including ancestors or not
     * @return If present , return <code>true</code> , or <code>false</code>
     */
    public static boolean isBeanPresent(
            ListableBeanFactory beanFactory, Class<?> beanClass, boolean includingAncestors) {

        String[] beanNames = getBeanNames(beanFactory, beanClass, includingAncestors);

        return !ObjectUtils.isEmpty(beanNames);
    }

    /**
     * Is Bean Present or not?
     *
     * @param beanFactory        {@link ListableBeanFactory}
     * @param beanClassName      The name of {@link Class} of Bean
     * @param includingAncestors including ancestors or not
     * @return If present , return <code>true</code> , or <code>false</code>
     */
    public static boolean isBeanPresent(
            ListableBeanFactory beanFactory, String beanClassName, boolean includingAncestors) {

        boolean present = false;

        ClassLoader classLoader = beanFactory.getClass().getClassLoader();

        if (ClassUtils.isPresent(beanClassName, classLoader)) {

            Class beanClass = ClassUtils.resolveClassName(beanClassName, classLoader);

            present = isBeanPresent(beanFactory, beanClass, includingAncestors);
        }

        return present;
    }

    /**
     * Is Bean Present or not?
     *
     * @param beanFactory   {@link ListableBeanFactory}
     * @param beanClassName The name of {@link Class} of Bean
     * @return If present , return <code>true</code> , or <code>false</code>
     */
    public static boolean isBeanPresent(ListableBeanFactory beanFactory, String beanClassName) {

        return isBeanPresent(beanFactory, beanClassName, false);
    }

    /**
     * Get Bean Names from {@link ConfigurableListableBeanFactory} by type.
     *
     * @param beanFactory {@link ConfigurableListableBeanFactory}
     * @param beanClass   The {@link Class} of Bean
     * @return If found , return the array of Bean Names , or empty array.
     */
    public static String[] getBeanNames(
            ConfigurableListableBeanFactory beanFactory, Class<?> beanClass) {
        return getBeanNames(beanFactory, beanClass, false);
    }

    /**
     * Get Bean Names from {@link ConfigurableListableBeanFactory} by type.
     *
     * @param beanFactory        {@link ConfigurableListableBeanFactory}
     * @param beanClass          The {@link Class} of Bean
     * @param includingAncestors including ancestors or not
     * @return If found , return the array of Bean Names , or empty array.
     */
    public static String[] getBeanNames(
            ConfigurableListableBeanFactory beanFactory, Class<?> beanClass, boolean includingAncestors) {

        Set<String> beanNames = new LinkedHashSet<String>();

        beanNames.addAll(doGetBeanNames(beanFactory, beanClass));

        if (includingAncestors) {

            BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();

            if (parentBeanFactory instanceof ConfigurableListableBeanFactory) {

                ConfigurableListableBeanFactory configurableListableBeanFactory =
                        (ConfigurableListableBeanFactory) parentBeanFactory;

                String[] parentBeanNames =
                        getBeanNames(configurableListableBeanFactory, beanClass, includingAncestors);

                beanNames.addAll(Arrays.asList(parentBeanNames));
            }
        }

        return StringUtils.toStringArray(beanNames);
    }

    /**
     * Get Bean Names from {@link ListableBeanFactory} by type.
     *
     * @param beanFactory {@link ListableBeanFactory}
     * @param beanClass   The {@link Class} of Bean
     * @return If found , return the array of Bean Names , or empty array.
     */
    public static String[] getBeanNames(ListableBeanFactory beanFactory, Class<?> beanClass) {
        return getBeanNames(beanFactory, beanClass, false);
    }

    /**
     * Get Bean Names from {@link ListableBeanFactory} by type.
     *
     * @param beanFactory        {@link ListableBeanFactory}
     * @param beanClass          The {@link Class} of Bean
     * @param includingAncestors including ancestors or not
     * @return If found , return the array of Bean Names , or empty array.
     */
    public static String[] getBeanNames(
            ListableBeanFactory beanFactory, Class<?> beanClass, boolean includingAncestors) {

        final BeanFactory actualBeanFactory;

        if (beanFactory instanceof ConfigurableApplicationContext) {

            ConfigurableApplicationContext applicationContext =
                    ConfigurableApplicationContext.class.cast(beanFactory);

            actualBeanFactory = applicationContext.getBeanFactory();

        } else {

            actualBeanFactory = beanFactory;
        }

        if (actualBeanFactory instanceof ConfigurableListableBeanFactory) {

            return getBeanNames(
                    (ConfigurableListableBeanFactory) actualBeanFactory, beanClass, includingAncestors);
        }

        return EMPTY_BEAN_NAMES;
    }

    private static Class<?> getFactoryBeanType(
            ConfigurableListableBeanFactory beanFactory, BeanDefinition factoryBeanDefinition) {

        BeanDefinition actualFactoryBeanDefinition = factoryBeanDefinition;

        final List<Class<?>> beanClasses = new ArrayList<Class<?>>(1);

        ClassLoader classLoader = beanFactory.getBeanClassLoader();

        String factoryBeanClassName = actualFactoryBeanDefinition.getBeanClassName();

        if (StringUtils.isEmpty(factoryBeanClassName)) {

            String factoryBeanName = factoryBeanDefinition.getFactoryBeanName();

            actualFactoryBeanDefinition = beanFactory.getBeanDefinition(factoryBeanName);

            factoryBeanClassName = actualFactoryBeanDefinition.getBeanClassName();
        }

        if (StringUtils.hasText(factoryBeanClassName)) {

            Class<?> factoryBeanClass = resolveBeanType(factoryBeanClassName, classLoader);

            final String factoryMethodName = factoryBeanDefinition.getFactoryMethodName();

            // @Configuration only allow one method FactoryBean
            ReflectionUtils.doWithMethods(
                    factoryBeanClass,
                    new ReflectionUtils.MethodCallback() {

                        @Override
                        public void doWith(Method method)
                                throws IllegalArgumentException, IllegalAccessException {

                            beanClasses.add(method.getReturnType());
                        }
                    },
                    new ReflectionUtils.MethodFilter() {

                        @Override
                        public boolean matches(Method method) {
                            return factoryMethodName.equals(method.getName());
                        }
                    });
        }

        return beanClasses.isEmpty() ? null : beanClasses.get(0);
    }

    private static Class<?> resolveBeanType(
            ConfigurableListableBeanFactory beanFactory, BeanDefinition beanDefinition) {

        String factoryBeanName = beanDefinition.getFactoryBeanName();

        ClassLoader classLoader = beanFactory.getBeanClassLoader();

        Class<?> beanType = null;

        if (StringUtils.hasText(factoryBeanName)) {

            beanType = getFactoryBeanType(beanFactory, beanDefinition);
        }

        if (beanType == null) {

            String beanClassName = beanDefinition.getBeanClassName();

            if (StringUtils.hasText(beanClassName)) {

                beanType = resolveBeanType(beanClassName, classLoader);
            }
        }

        if (beanType == null) {
            log.error("{} can't be resolved bean type!", beanDefinition);
        }

        return beanType;
    }

    /**
     * Get Bean names from {@link ConfigurableListableBeanFactory} by type
     *
     * @param beanFactory {@link ConfigurableListableBeanFactory}
     * @param beanType    The {@link Class type} of Bean
     * @return the array of bean names.
     */
    protected static Set<String> doGetBeanNames(
            ConfigurableListableBeanFactory beanFactory, Class<?> beanType) {

        String[] allBeanNames = beanFactory.getBeanDefinitionNames();

        Set<String> beanNames = new LinkedHashSet<String>();

        for (String beanName : allBeanNames) {

            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

            Class<?> beanClass = resolveBeanType(beanFactory, beanDefinition);

            if (beanClass != null && ClassUtils.isAssignable(beanType, beanClass)) {

                beanNames.add(beanName);
            }
        }

        return Collections.unmodifiableSet(beanNames);
    }

    /**
     * Resolve Bean Type
     *
     * @param beanClassName the class name of Bean
     * @param classLoader   {@link ClassLoader}
     * @return Bean type if can be resolved , or return <code>null</code>.
     */
    public static Class<?> resolveBeanType(String beanClassName, ClassLoader classLoader) {

        if (!StringUtils.hasText(beanClassName)) {
            return null;
        }

        Class<?> beanType = null;

        try {

            beanType = ClassUtils.resolveClassName(beanClassName, classLoader);

            beanType = ClassUtils.getUserClass(beanType);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return beanType;
    }

    /**
     * Get Optional Bean by {@link Class} including ancestors(BeanFactory).
     *
     * @param beanFactory        {@link ListableBeanFactory}
     * @param beanClass          The {@link Class} of Bean
     * @param includingAncestors including ancestors or not
     * @param <T>                The {@link Class} of Bean
     * @return Bean object if found , or return <code>null</code>.
     * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
     * @see BeanFactoryUtils#beanOfTypeIncludingAncestors(ListableBeanFactory,
     * Class)
     */
    public static <T> T getOptionalBean(
            ListableBeanFactory beanFactory, Class<T> beanClass, boolean includingAncestors)
            throws BeansException {

        String[] beanNames = getBeanNames(beanFactory, beanClass, includingAncestors);

        if (ObjectUtils.isEmpty(beanNames)) {
            log.debug("The bean [ class : {} ] can't be found ", beanClass.getName());
            return null;
        }

        T bean = null;

        try {

            bean =
                    includingAncestors
                            ? beanOfTypeIncludingAncestors(beanFactory, beanClass)
                            : beanFactory.getBean(beanClass);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return bean;
    }

    /**
     * Get Optional Bean by {@link Class}.
     *
     * @param beanFactory {@link ListableBeanFactory}
     * @param beanClass   The {@link Class} of Bean
     * @param <T>         The {@link Class} of Bean
     * @return Bean object if found , or return <code>null</code>.
     * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
     */
    public static <T> T getOptionalBean(ListableBeanFactory beanFactory, Class<T> beanClass)
            throws BeansException {

        return getOptionalBean(beanFactory, beanClass, false);
    }

    /**
     * Get all sorted Beans of {@link ListableBeanFactory} in specified bean type.
     *
     * @param beanFactory {@link ListableBeanFactory}
     * @param type        bean type
     * @param <T>         bean type
     * @return all sorted Beans
     */
    public static <T> List<T> getSortedBeans(ListableBeanFactory beanFactory, Class<T> type) {

        Map<String, T> beansOfType = BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, type);
        List<T> beansList = new ArrayList<T>(beansOfType.values());
        AnnotationAwareOrderComparator.sort(beansList);
        return Collections.unmodifiableList(beansList);
    }

    /**
     * Sort Beans {@link Map} via {@link AnnotationAwareOrderComparator#sort(List)} rule
     *
     * @param beansMap Beans {@link Map}
     * @param <T>      the type of Bean
     * @return sorted Beans {@link Map}
     */
    public static <T> Map<String, T> sort(final Map<String, T> beansMap) {

        Map<String, T> unmodifiableBeansMap = Collections.unmodifiableMap(beansMap);

        List<NamingBean<T>> namingBeans = new ArrayList<NamingBean<T>>(unmodifiableBeansMap.size());

        for (Map.Entry<String, T> entry : unmodifiableBeansMap.entrySet()) {
            String beanName = entry.getKey();
            T bean = entry.getValue();
            NamingBean<T> namingBean = new NamingBean<T>(beanName, bean);
            namingBeans.add(namingBean);
        }

        AnnotationAwareOrderComparator.sort(namingBeans);

        Map<String, T> sortedBeansMap = new LinkedHashMap<String, T>(beansMap.size());

        for (NamingBean<T> namingBean : namingBeans) {
            sortedBeansMap.put(namingBean.name, namingBean.bean);
        }

        return sortedBeansMap;
    }

    static class NamingBean<T> extends AnnotationAwareOrderComparator
            implements Comparable<NamingBean>, Ordered {

        private final String name;

        private final T bean;

        NamingBean(String name, T bean) {
            this.name = name;
            this.bean = bean;
        }

        @Override
        public int compareTo(NamingBean o) {
            return compare(this, o);
        }

        @Override
        public int getOrder() {
            return getOrder(bean);
        }
    }
}
