package dev.morphia.annotations.experimental;

import java.lang.annotation.*;

/**
 * Marks an entity's constructor for use in creation rather than relying on a no arg constructor and field injection.
 *
 * @deprecated This annotation is not necessary and will be removed soon.
 */
@Inherited
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Constructor {
}
