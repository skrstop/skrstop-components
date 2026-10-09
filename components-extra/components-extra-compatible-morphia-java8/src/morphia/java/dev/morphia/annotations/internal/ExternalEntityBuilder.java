package dev.morphia.annotations.internal;

import dev.morphia.annotations.CappedAt;
import dev.morphia.annotations.ExternalEntity;
import dev.morphia.mapping.Mapper;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ExternalEntityBuilder {

    private ExternalEntityAnnotation annotation = new ExternalEntityAnnotation();

    private ExternalEntityBuilder() {
        annotation.cap = CappedAtBuilder.cappedAtBuilder().build();
        annotation.concern = "";
        annotation.discriminator = Mapper.IGNORED_FIELDNAME;
        annotation.discriminatorKey = Mapper.IGNORED_FIELDNAME;
        annotation.useDiscriminator = true;
        annotation.value = Mapper.IGNORED_FIELDNAME;
    }

    public ExternalEntity build() {
        ExternalEntityAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ExternalEntityBuilder externalEntityBuilder() {
        return new ExternalEntityBuilder();
    }

    static private class ExternalEntityAnnotation implements ExternalEntity {
        private CappedAt cap;
        private String concern;
        private String discriminator;
        private String discriminatorKey;
        private Class<?> target;
        private boolean useDiscriminator;
        private String value;

        public Class<ExternalEntity> annotationType() {
            return ExternalEntity.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ExternalEntityAnnotation)) {
                return false;
            }
            ExternalEntityAnnotation that = (ExternalEntityAnnotation) o;
            return Objects.equals(cap, that.cap) && Objects.equals(concern, that.concern)
                    && Objects.equals(discriminator, that.discriminator)
                    && Objects.equals(discriminatorKey, that.discriminatorKey) && Objects.equals(target, that.target)
                    && Objects.equals(useDiscriminator, that.useDiscriminator) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(cap, concern, discriminator, discriminatorKey, target, useDiscriminator, value);
        }

        @Override
        public CappedAt cap() {
            return cap;
        }

        @Override
        public String concern() {
            return concern;
        }

        @Override
        public String discriminator() {
            return discriminator;
        }

        @Override
        public String discriminatorKey() {
            return discriminatorKey;
        }

        @Override
        public Class<?> target() {
            return target;
        }

        @Override
        public boolean useDiscriminator() {
            return useDiscriminator;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static ExternalEntityBuilder externalEntityBuilder(ExternalEntity source) {
        ExternalEntityBuilder builder = new ExternalEntityBuilder();
        builder.annotation.cap = source.cap();
        builder.annotation.concern = source.concern();
        builder.annotation.discriminator = source.discriminator();
        builder.annotation.discriminatorKey = source.discriminatorKey();
        builder.annotation.target = source.target();
        builder.annotation.useDiscriminator = source.useDiscriminator();
        builder.annotation.value = source.value();
        return builder;
    }

    public ExternalEntityBuilder cap(CappedAt cap) {
        annotation.cap = cap;
        return this;
    }

    public ExternalEntityBuilder concern(String concern) {
        annotation.concern = concern;
        return this;
    }

    public ExternalEntityBuilder discriminator(String discriminator) {
        annotation.discriminator = discriminator;
        return this;
    }

    public ExternalEntityBuilder discriminatorKey(String discriminatorKey) {
        annotation.discriminatorKey = discriminatorKey;
        return this;
    }

    public ExternalEntityBuilder target(Class<?> target) {
        annotation.target = target;
        return this;
    }

    public ExternalEntityBuilder useDiscriminator(boolean useDiscriminator) {
        annotation.useDiscriminator = useDiscriminator;
        return this;
    }

    public ExternalEntityBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}