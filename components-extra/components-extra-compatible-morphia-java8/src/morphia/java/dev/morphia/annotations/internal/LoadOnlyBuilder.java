package dev.morphia.annotations.internal;

import dev.morphia.annotations.LoadOnly;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class LoadOnlyBuilder {

    private LoadOnlyAnnotation annotation = new LoadOnlyAnnotation();

    private LoadOnlyBuilder() {
    }

    public LoadOnly build() {
        LoadOnlyAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static LoadOnlyBuilder loadOnlyBuilder() {
        return new LoadOnlyBuilder();
    }

    static private class LoadOnlyAnnotation implements LoadOnly {
        public Class<LoadOnly> annotationType() {
            return LoadOnly.class;
        }
    }
}