package dev.morphia.annotations.internal;

import dev.morphia.annotations.Version;
import dev.morphia.mapping.Mapper;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class VersionBuilder {

    private VersionAnnotation annotation = new VersionAnnotation();

    private VersionBuilder() {
        annotation.value = Mapper.IGNORED_FIELDNAME;
    }

    public Version build() {
        VersionAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static VersionBuilder versionBuilder() {
        return new VersionBuilder();
    }

    static private class VersionAnnotation implements Version {
        private String value;

        public Class<Version> annotationType() {
            return Version.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof VersionAnnotation)) {
                return false;
            }
            VersionAnnotation that = (VersionAnnotation) o;
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

    public static VersionBuilder versionBuilder(Version source) {
        VersionBuilder builder = new VersionBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public VersionBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}