package dev.morphia.annotations;

import dev.morphia.mapping.codec.BaseReferenceCodec;

import java.lang.annotation.*;

/**
 * Defines a specific handler for a type above and beyond the codecs
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Documented
@Inherited
public @interface Handler {
    /**
     * @return the handler Class
     */
    Class<? extends BaseReferenceCodec> value();
}
