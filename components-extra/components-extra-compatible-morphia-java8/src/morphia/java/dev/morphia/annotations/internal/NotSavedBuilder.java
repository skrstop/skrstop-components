package dev.morphia.annotations.internal;

import dev.morphia.annotations.NotSaved;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class NotSavedBuilder {

    private NotSavedAnnotation annotation = new NotSavedAnnotation();

    private NotSavedBuilder() {
    }

    public NotSaved build() {
        NotSavedAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static NotSavedBuilder notSavedBuilder() {
        return new NotSavedBuilder();
    }

    static private class NotSavedAnnotation implements NotSaved {
        public Class<NotSaved> annotationType() {
            return NotSaved.class;
        }
    }
}