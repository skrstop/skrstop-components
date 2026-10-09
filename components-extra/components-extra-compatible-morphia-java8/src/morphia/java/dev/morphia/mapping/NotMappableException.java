package dev.morphia.mapping;


/**
 * Indicates a type is not mappable by Morphia
 *
 * @since 2.2
 */
public final class NotMappableException extends RuntimeException {
    NotMappableException(Class type) {
        super(type.getName() + " is not a mappable type. Mappable types need to be annotated with either @Entity or @Embedded.");
    }
}
