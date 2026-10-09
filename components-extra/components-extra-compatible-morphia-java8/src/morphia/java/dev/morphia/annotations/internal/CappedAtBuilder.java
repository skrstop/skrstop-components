package dev.morphia.annotations.internal;

import dev.morphia.annotations.CappedAt;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class CappedAtBuilder {

    private CappedAtAnnotation annotation = new CappedAtAnnotation();

    private CappedAtBuilder() {
        annotation.count = 0;
        annotation.value = 1024 * 1024;
    }

    public CappedAt build() {
        CappedAtAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static CappedAtBuilder cappedAtBuilder() {
        return new CappedAtBuilder();
    }

    static private class CappedAtAnnotation implements CappedAt {
        private long count;
        private long value;

        public Class<CappedAt> annotationType() {
            return CappedAt.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CappedAtAnnotation)) {
                return false;
            }
            CappedAtAnnotation that = (CappedAtAnnotation) o;
            return Objects.equals(count, that.count) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(count, value);
        }

        @Override
        public long count() {
            return count;
        }

        @Override
        public long value() {
            return value;
        }
    }

    public static CappedAtBuilder cappedAtBuilder(CappedAt source) {
        CappedAtBuilder builder = new CappedAtBuilder();
        builder.annotation.count = source.count();
        builder.annotation.value = source.value();
        return builder;
    }

    public CappedAtBuilder count(long count) {
        annotation.count = count;
        return this;
    }

    public CappedAtBuilder value(long value) {
        annotation.value = value;
        return this;
    }
}