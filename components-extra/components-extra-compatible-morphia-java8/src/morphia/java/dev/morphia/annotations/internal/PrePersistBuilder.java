package dev.morphia.annotations.internal;

import dev.morphia.annotations.PrePersist;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class PrePersistBuilder {

    private PrePersistAnnotation annotation = new PrePersistAnnotation();

    private PrePersistBuilder() {
    }

    public PrePersist build() {
        PrePersistAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static PrePersistBuilder prePersistBuilder() {
        return new PrePersistBuilder();
    }

    static private class PrePersistAnnotation implements PrePersist {
        public Class<PrePersist> annotationType() {
            return PrePersist.class;
        }
    }
}