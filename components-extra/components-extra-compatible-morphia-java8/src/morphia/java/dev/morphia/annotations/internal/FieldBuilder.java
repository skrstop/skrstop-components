package dev.morphia.annotations.internal;

import dev.morphia.annotations.Field;
import dev.morphia.utils.IndexType;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class FieldBuilder {

    private FieldAnnotation annotation = new FieldAnnotation();

    private FieldBuilder() {
        annotation.type = IndexType.ASC;
        annotation.weight = -1;
    }

    public Field build() {
        FieldAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static FieldBuilder fieldBuilder() {
        return new FieldBuilder();
    }

    static private class FieldAnnotation implements Field {
        private IndexType type;
        private String value;
        private int weight;

        public Class<Field> annotationType() {
            return Field.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FieldAnnotation)) {
                return false;
            }
            FieldAnnotation that = (FieldAnnotation) o;
            return Objects.equals(type, that.type) && Objects.equals(value, that.value)
                    && Objects.equals(weight, that.weight);
        }

        public int hashCode() {
            return Objects.hash(type, value, weight);
        }

        @Override
        public IndexType type() {
            return type;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public int weight() {
            return weight;
        }
    }

    public static FieldBuilder fieldBuilder(Field source) {
        FieldBuilder builder = new FieldBuilder();
        builder.annotation.type = source.type();
        builder.annotation.value = source.value();
        builder.annotation.weight = source.weight();
        return builder;
    }

    public FieldBuilder type(IndexType type) {
        annotation.type = type;
        return this;
    }

    public FieldBuilder value(String value) {
        annotation.value = value;
        return this;
    }

    public FieldBuilder weight(int weight) {
        annotation.weight = weight;
        return this;
    }
}