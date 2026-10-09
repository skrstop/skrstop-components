package dev.morphia.annotations.internal;

import dev.morphia.annotations.Reference;
import dev.morphia.mapping.Mapper;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ReferenceBuilder {

    private ReferenceAnnotation annotation = new ReferenceAnnotation();

    private ReferenceBuilder() {
        annotation.idOnly = false;
        annotation.ignoreMissing = false;
        annotation.lazy = false;
        annotation.value = Mapper.IGNORED_FIELDNAME;
    }

    public Reference build() {
        ReferenceAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ReferenceBuilder referenceBuilder() {
        return new ReferenceBuilder();
    }

    static private class ReferenceAnnotation implements Reference {
        private boolean idOnly;
        private boolean ignoreMissing;
        private boolean lazy;
        private String value;

        public Class<Reference> annotationType() {
            return Reference.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ReferenceAnnotation)) {
                return false;
            }
            ReferenceAnnotation that = (ReferenceAnnotation) o;
            return Objects.equals(idOnly, that.idOnly) && Objects.equals(ignoreMissing, that.ignoreMissing)
                    && Objects.equals(lazy, that.lazy) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(idOnly, ignoreMissing, lazy, value);
        }

        @Override
        public boolean idOnly() {
            return idOnly;
        }

        @Override
        public boolean ignoreMissing() {
            return ignoreMissing;
        }

        @Override
        public boolean lazy() {
            return lazy;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static ReferenceBuilder referenceBuilder(Reference source) {
        ReferenceBuilder builder = new ReferenceBuilder();
        builder.annotation.idOnly = source.idOnly();
        builder.annotation.ignoreMissing = source.ignoreMissing();
        builder.annotation.lazy = source.lazy();
        builder.annotation.value = source.value();
        return builder;
    }

    public ReferenceBuilder idOnly(boolean idOnly) {
        annotation.idOnly = idOnly;
        return this;
    }

    public ReferenceBuilder ignoreMissing(boolean ignoreMissing) {
        annotation.ignoreMissing = ignoreMissing;
        return this;
    }

    public ReferenceBuilder lazy(boolean lazy) {
        annotation.lazy = lazy;
        return this;
    }

    public ReferenceBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}