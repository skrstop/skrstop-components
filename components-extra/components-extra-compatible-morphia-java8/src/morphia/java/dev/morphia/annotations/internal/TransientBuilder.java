package dev.morphia.annotations.internal;

import dev.morphia.annotations.Transient;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class TransientBuilder {

    private TransientAnnotation annotation = new TransientAnnotation();

    private TransientBuilder() {
    }

    public Transient build() {
        TransientAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static TransientBuilder transientBuilder() {
        return new TransientBuilder();
    }

    static private class TransientAnnotation implements Transient {
        public Class<Transient> annotationType() {
            return Transient.class;
        }
    }
}