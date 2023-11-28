package cn.auntec.framework.components.starter.spring.support.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.util.ClassUtils.resolveClassName;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

/**
 * Annotated {@link BeanDefinition} Utilities
 *
 * @author 蒋时华
 * @see BeanDefinition
 * @since 2017.01.09
 */
@Slf4j
public abstract class AnnotatedBeanDefinitionRegistryUtil {

    /**
     * Is present bean that was registered by the specified {@link Annotation annotated} {@link Class
     * class}
     *
     * @param registry       {@link BeanDefinitionRegistry}
     * @param annotatedClass the {@link Annotation annotated} {@link Class class}
     * @return if present, return <code>true</code>, or <code>false</code>
     * @since 1.0.3
     */
    public static boolean isPresentBean(BeanDefinitionRegistry registry, Class<?> annotatedClass) {

        boolean present = false;

        String[] beanNames = registry.getBeanDefinitionNames();

        ClassLoader classLoader = annotatedClass.getClassLoader();

        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                AnnotationMetadata annotationMetadata =
                        ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
                String className = annotationMetadata.getClassName();
                Class<?> targetClass = resolveClassName(className, classLoader);
                present = nullSafeEquals(targetClass, annotatedClass);
                if (present) {
                    log.debug("The annotatedClass[class : {} , bean name : {}] was present in registry[{}]", className, beanName, registry);
                    break;
                }
            }
        }

        return present;
    }

    /**
     * Register Beans if not present in {@link BeanDefinitionRegistry registry}
     *
     * @param registry         {@link BeanDefinitionRegistry}
     * @param annotatedClasses {@link Annotation annotation} class
     */
    public static void registerBeans(BeanDefinitionRegistry registry, Class<?>... annotatedClasses) {

        if (ObjectUtils.isEmpty(annotatedClasses)) {
            return;
        }

        // Remove all annotated-classes that have been registered

        new ArrayList<Class<?>>(asList(annotatedClasses)).removeIf(annotatedClass -> isPresentBean(registry, annotatedClass));

        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(registry);

        log.debug("{} will register annotated classes : {} .", registry.getClass().getSimpleName(), asList(annotatedClasses));

        reader.register(annotatedClasses);
    }

    /**
     * Scan base packages for register {@link Component @Component}s
     *
     * @param registry     {@link BeanDefinitionRegistry}
     * @param basePackages base packages
     * @return the count of registered components.
     */
    public static int scanBasePackages(BeanDefinitionRegistry registry, String... basePackages) {

        int count = 0;

        if (!ObjectUtils.isEmpty(basePackages)) {

            log.debug("{} will scan base packages {}.", registry.getClass().getSimpleName(), Arrays.asList(basePackages));
            List<String> registeredBeanNames = Arrays.asList(registry.getBeanDefinitionNames());

            ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner =
                    new ClassPathBeanDefinitionScanner(registry);
            count = classPathBeanDefinitionScanner.scan(basePackages);

            List<String> scannedBeanNames = new ArrayList<String>(count);
            scannedBeanNames.addAll(Arrays.asList(registry.getBeanDefinitionNames()));
            scannedBeanNames.removeAll(registeredBeanNames);

            log.debug("The Scanned Components[ count : {}] under base packages {} :", count, Arrays.asList(basePackages));

            for (String scannedBeanName : scannedBeanNames) {
                BeanDefinition scannedBeanDefinition = registry.getBeanDefinition(scannedBeanName);
                log.debug("Component [ name : {} , class : {} ]", scannedBeanName, scannedBeanDefinition.getBeanClassName());
            }
        }

        return count;
    }
}
