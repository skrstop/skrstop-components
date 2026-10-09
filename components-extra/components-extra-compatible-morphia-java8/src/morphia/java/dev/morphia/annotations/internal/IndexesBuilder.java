package dev.morphia.annotations.internal;

import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IndexesBuilder {

    private IndexesAnnotation annotation = new IndexesAnnotation();

    private IndexesBuilder() {
    }

    public Indexes build() {
        IndexesAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IndexesBuilder indexesBuilder() {
        return new IndexesBuilder();
    }

    static private class IndexesAnnotation implements Indexes {
        private Index[] value;

        public Class<Indexes> annotationType() {
            return Indexes.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IndexesAnnotation)) {
                return false;
            }
            IndexesAnnotation that = (IndexesAnnotation) o;
            return Arrays.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public Index[] value() {
            return value;
        }
    }

    public static IndexesBuilder indexesBuilder(Indexes source) {
        IndexesBuilder builder = new IndexesBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public IndexesBuilder value(Index... value) {
        annotation.value = value;
        return this;
    }
}