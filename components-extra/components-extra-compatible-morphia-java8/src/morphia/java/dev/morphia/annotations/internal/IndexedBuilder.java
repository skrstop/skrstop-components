package dev.morphia.annotations.internal;

import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import dev.morphia.utils.IndexDirection;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IndexedBuilder {

    private IndexedAnnotation annotation = new IndexedAnnotation();

    private IndexedBuilder() {
        annotation.options = IndexOptionsBuilder.indexOptionsBuilder().build();
        annotation.value = IndexDirection.ASC;
    }

    public Indexed build() {
        IndexedAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IndexedBuilder indexedBuilder() {
        return new IndexedBuilder();
    }

    static private class IndexedAnnotation implements Indexed {
        private IndexOptions options;
        private IndexDirection value;

        public Class<Indexed> annotationType() {
            return Indexed.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IndexedAnnotation)) {
                return false;
            }
            IndexedAnnotation that = (IndexedAnnotation) o;
            return Objects.equals(options, that.options) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(options, value);
        }

        @Override
        public IndexOptions options() {
            return options;
        }

        @Override
        public IndexDirection value() {
            return value;
        }
    }

    public static IndexedBuilder indexedBuilder(Indexed source) {
        IndexedBuilder builder = new IndexedBuilder();
        builder.annotation.options = source.options();
        builder.annotation.value = source.value();
        return builder;
    }

    public IndexedBuilder options(IndexOptions options) {
        annotation.options = options;
        return this;
    }

    public IndexedBuilder value(IndexDirection value) {
        annotation.value = value;
        return this;
    }
}