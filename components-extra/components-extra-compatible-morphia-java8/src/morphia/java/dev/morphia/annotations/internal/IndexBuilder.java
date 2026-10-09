package dev.morphia.annotations.internal;

import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IndexBuilder {

    private IndexAnnotation annotation = new IndexAnnotation();

    private IndexBuilder() {
        annotation.fields = new Field[]{};
        annotation.options = IndexOptionsBuilder.indexOptionsBuilder().build();
    }

    public Index build() {
        IndexAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IndexBuilder indexBuilder() {
        return new IndexBuilder();
    }

    static private class IndexAnnotation implements Index {
        private Field[] fields;
        private IndexOptions options;

        public Class<Index> annotationType() {
            return Index.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IndexAnnotation)) {
                return false;
            }
            IndexAnnotation that = (IndexAnnotation) o;
            return Arrays.equals(fields, that.fields) && Objects.equals(options, that.options);
        }

        public int hashCode() {
            return Objects.hash(fields, options);
        }

        @Override
        public Field[] fields() {
            return fields;
        }

        @Override
        public IndexOptions options() {
            return options;
        }
    }

    public static IndexBuilder indexBuilder(Index source) {
        IndexBuilder builder = new IndexBuilder();
        builder.annotation.fields = source.fields();
        builder.annotation.options = source.options();
        return builder;
    }

    public IndexBuilder fields(Field... fields) {
        annotation.fields = fields;
        return this;
    }

    public IndexBuilder options(IndexOptions options) {
        annotation.options = options;
        return this;
    }
}