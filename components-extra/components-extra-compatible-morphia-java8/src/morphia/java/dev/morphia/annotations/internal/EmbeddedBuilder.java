package dev.morphia.annotations.internal;

import dev.morphia.annotations.Embedded;
import dev.morphia.mapping.Mapper;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class EmbeddedBuilder {

    private EmbeddedAnnotation annotation = new EmbeddedAnnotation();

    private EmbeddedBuilder() {
        annotation.value = Mapper.IGNORED_FIELDNAME;
        annotation.useDiscriminator = true;
        annotation.discriminatorKey = Mapper.IGNORED_FIELDNAME;
        annotation.discriminator = Mapper.IGNORED_FIELDNAME;
    }

    public Embedded build() {
        EmbeddedAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static EmbeddedBuilder embeddedBuilder() {
        return new EmbeddedBuilder();
    }

    static private class EmbeddedAnnotation implements Embedded {
        private String value;
        private boolean useDiscriminator;
        private String discriminatorKey;
        private String discriminator;

        public Class<Embedded> annotationType() {
            return Embedded.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof EmbeddedAnnotation)) {
                return false;
            }
            EmbeddedAnnotation that = (EmbeddedAnnotation) o;
            return Objects.equals(value, that.value) && Objects.equals(useDiscriminator, that.useDiscriminator)
                    && Objects.equals(discriminatorKey, that.discriminatorKey)
                    && Objects.equals(discriminator, that.discriminator);
        }

        public int hashCode() {
            return Objects.hash(value, useDiscriminator, discriminatorKey, discriminator);
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public boolean useDiscriminator() {
            return useDiscriminator;
        }

        @Override
        public String discriminatorKey() {
            return discriminatorKey;
        }

        @Override
        public String discriminator() {
            return discriminator;
        }
    }

    public static EmbeddedBuilder embeddedBuilder(Embedded source) {
        EmbeddedBuilder builder = new EmbeddedBuilder();
        builder.annotation.value = source.value();
        builder.annotation.useDiscriminator = source.useDiscriminator();
        builder.annotation.discriminatorKey = source.discriminatorKey();
        builder.annotation.discriminator = source.discriminator();
        return builder;
    }

    public EmbeddedBuilder value(String value) {
        annotation.value = value;
        return this;
    }

    public EmbeddedBuilder useDiscriminator(boolean useDiscriminator) {
        annotation.useDiscriminator = useDiscriminator;
        return this;
    }

    public EmbeddedBuilder discriminatorKey(String discriminatorKey) {
        annotation.discriminatorKey = discriminatorKey;
        return this;
    }

    public EmbeddedBuilder discriminator(String discriminator) {
        annotation.discriminator = discriminator;
        return this;
    }
}