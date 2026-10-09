package dev.morphia.annotations;

import dev.morphia.utils.IndexDirection;

import java.lang.annotation.*;

/**
 * Specified on fields that should be Indexed.
 *
 * @author Scott Hernandez
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Indexed {
    /**
     * @return Options to apply to the index. Use of this field will ignore any of the deprecated options defined on {@link Index} directly.
     */
    IndexOptions options() default @IndexOptions();

    /**
     * @return the type of the index (ascending, descending, geo2d); default is ascending
     * @see IndexDirection
     */
    IndexDirection value() default IndexDirection.ASC;
}
