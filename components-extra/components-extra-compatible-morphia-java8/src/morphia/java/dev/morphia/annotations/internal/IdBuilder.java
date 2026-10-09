package dev.morphia.annotations.internal;

import dev.morphia.annotations.Id;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IdBuilder {

    private IdAnnotation annotation = new IdAnnotation();

    private IdBuilder() {
    }

    public Id build() {
        IdAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IdBuilder idBuilder() {
        return new IdBuilder();
    }

    static private class IdAnnotation implements Id {
        public Class<Id> annotationType() {
            return Id.class;
        }
    }
}