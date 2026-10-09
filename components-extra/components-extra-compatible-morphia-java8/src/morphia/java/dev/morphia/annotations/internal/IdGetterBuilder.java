package dev.morphia.annotations.internal;

import dev.morphia.annotations.IdGetter;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IdGetterBuilder {

    private IdGetterAnnotation annotation = new IdGetterAnnotation();

    private IdGetterBuilder() {
    }

    public IdGetter build() {
        IdGetterAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IdGetterBuilder idGetterBuilder() {
        return new IdGetterBuilder();
    }

    static private class IdGetterAnnotation implements IdGetter {
        public Class<IdGetter> annotationType() {
            return IdGetter.class;
        }
    }
}