package dev.morphia.annotations;

import java.lang.annotation.*;

/**
 * Denotes the ID field on an entity.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Id {

}
