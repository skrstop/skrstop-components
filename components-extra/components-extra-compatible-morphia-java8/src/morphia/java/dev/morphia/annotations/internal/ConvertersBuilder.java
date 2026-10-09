package dev.morphia.annotations.internal;

import dev.morphia.annotations.Converters;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ConvertersBuilder {

    private ConvertersAnnotation annotation = new ConvertersAnnotation();

    private ConvertersBuilder() {
    }

    public Converters build() {
        ConvertersAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ConvertersBuilder convertersBuilder() {
        return new ConvertersBuilder();
    }

    static private class ConvertersAnnotation implements Converters {
        private Class<?>[] value;

        public Class<Converters> annotationType() {
            return Converters.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ConvertersAnnotation)) {
                return false;
            }
            ConvertersAnnotation that = (ConvertersAnnotation) o;
            return Arrays.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public Class<?>[] value() {
            return value;
        }
    }

    public static ConvertersBuilder convertersBuilder(Converters source) {
        ConvertersBuilder builder = new ConvertersBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public ConvertersBuilder value(Class<?>... value) {
        annotation.value = value;
        return this;
    }
}