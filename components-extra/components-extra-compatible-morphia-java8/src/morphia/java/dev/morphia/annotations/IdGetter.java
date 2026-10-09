package dev.morphia.annotations;

import java.lang.annotation.*;

/**
 * This annotation allows the lazy-load proxy to return the ID of a referenced entity without reading the reference from the database.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IdGetter {

}
