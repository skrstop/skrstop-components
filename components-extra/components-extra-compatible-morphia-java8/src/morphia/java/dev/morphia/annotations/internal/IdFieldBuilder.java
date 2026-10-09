package dev.morphia.annotations.internal;

import dev.morphia.annotations.IdField;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IdFieldBuilder {

    private IdFieldAnnotation annotation = new IdFieldAnnotation();

    private IdFieldBuilder() {
    }

    public IdField build() {
        IdFieldAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IdFieldBuilder idFieldBuilder() {
        return new IdFieldBuilder();
    }

    static private class IdFieldAnnotation implements IdField {
        private String value;

        public Class<IdField> annotationType() {
            return IdField.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IdFieldAnnotation)) {
                return false;
            }
            IdFieldAnnotation that = (IdFieldAnnotation) o;
            return Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static IdFieldBuilder idFieldBuilder(IdField source) {
        IdFieldBuilder builder = new IdFieldBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public IdFieldBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}