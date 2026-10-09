package dev.morphia.annotations;

import dev.morphia.annotations.internal.MorphiaExperimental;

import java.lang.annotation.*;

/**
 * Denotes the possible values for a configuration option. Depending on the config property, this list may not be exhausted. Consult
 * the documentation for that property for details.
 *
 * @morphia.experimental
 * @since 2.4
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MorphiaExperimental
public @interface PossibleValues {
    String[] value();

    /**
     * Indicates that a fully qualified class name maybe listed as well.
     *
     * @return true if a class name maybe be listed.
     */
    boolean fqcn() default true;
}
