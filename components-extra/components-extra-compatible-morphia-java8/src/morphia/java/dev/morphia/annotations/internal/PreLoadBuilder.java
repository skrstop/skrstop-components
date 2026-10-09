package dev.morphia.annotations.internal;

import dev.morphia.annotations.PreLoad;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class PreLoadBuilder {

    private PreLoadAnnotation annotation = new PreLoadAnnotation();

    private PreLoadBuilder() {
    }

    public PreLoad build() {
        PreLoadAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static PreLoadBuilder preLoadBuilder() {
        return new PreLoadBuilder();
    }

    static private class PreLoadAnnotation implements PreLoad {
        public Class<PreLoad> annotationType() {
            return PreLoad.class;
        }
    }
}