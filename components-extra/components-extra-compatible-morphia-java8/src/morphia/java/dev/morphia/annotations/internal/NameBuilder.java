package dev.morphia.annotations.internal;

import dev.morphia.annotations.Name;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class NameBuilder {

    private NameAnnotation annotation = new NameAnnotation();

    private NameBuilder() {
    }

    public Name build() {
        NameAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static NameBuilder nameBuilder() {
        return new NameBuilder();
    }

    static private class NameAnnotation implements Name {
        private String value;

        public Class<Name> annotationType() {
            return Name.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NameAnnotation)) {
                return false;
            }
            NameAnnotation that = (NameAnnotation) o;
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

    public static NameBuilder nameBuilder(Name source) {
        NameBuilder builder = new NameBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public NameBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}