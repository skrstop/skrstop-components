package dev.morphia.annotations;

import dev.morphia.EntityListener;

import java.lang.annotation.*;

/**
 * Specifies other classes to participate in the @Entity's lifecycle
 *
 * @author Scott Hernandez
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EntityListeners {
    /**
     * @return The listeners to use for this entity
     * @deprecated In the next version, this will be restricted to subclasses of {@link EntityListener}. Migrating your listeners to be
     * subclasses now will prevent any compilation issues in the future.
     */
    @Deprecated
    Class<?>[] value() default {};
}
