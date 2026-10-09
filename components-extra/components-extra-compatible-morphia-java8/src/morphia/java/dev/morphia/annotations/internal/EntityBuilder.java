package dev.morphia.annotations.internal;

import dev.morphia.annotations.CappedAt;
import dev.morphia.annotations.Entity;
import dev.morphia.mapping.Mapper;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class EntityBuilder {

    private EntityAnnotation annotation = new EntityAnnotation();

    private EntityBuilder() {
        annotation.cap = CappedAtBuilder.cappedAtBuilder().build();
        annotation.concern = "";
        annotation.discriminator = Mapper.IGNORED_FIELDNAME;
        annotation.discriminatorKey = Mapper.IGNORED_FIELDNAME;
        annotation.useDiscriminator = true;
        annotation.value = Mapper.IGNORED_FIELDNAME;
    }

    public Entity build() {
        EntityAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static EntityBuilder entityBuilder() {
        return new EntityBuilder();
    }

    static private class EntityAnnotation implements Entity {
        private CappedAt cap;
        private String concern;
        private String discriminator;
        private String discriminatorKey;
        private boolean useDiscriminator;
        private String value;

        public Class<Entity> annotationType() {
            return Entity.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof EntityAnnotation)) {
                return false;
            }
            EntityAnnotation that = (EntityAnnotation) o;
            return Objects.equals(cap, that.cap) && Objects.equals(concern, that.concern)
                    && Objects.equals(discriminator, that.discriminator)
                    && Objects.equals(discriminatorKey, that.discriminatorKey)
                    && Objects.equals(useDiscriminator, that.useDiscriminator) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(cap, concern, discriminator, discriminatorKey, useDiscriminator, value);
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
        public boolean useDiscriminator() {
            return useDiscriminator;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static EntityBuilder entityBuilder(Entity source) {
        EntityBuilder builder = new EntityBuilder();
        builder.annotation.cap = source.cap();
        builder.annotation.concern = source.concern();
        builder.annotation.discriminator = source.discriminator();
        builder.annotation.discriminatorKey = source.discriminatorKey();
        builder.annotation.useDiscriminator = source.useDiscriminator();
        builder.annotation.value = source.value();
        return builder;
    }

    public EntityBuilder cap(CappedAt cap) {
        annotation.cap = cap;
        return this;
    }

    public EntityBuilder concern(String concern) {
        annotation.concern = concern;
        return this;
    }

    public EntityBuilder discriminator(String discriminator) {
        annotation.discriminator = discriminator;
        return this;
    }

    public EntityBuilder discriminatorKey(String discriminatorKey) {
        annotation.discriminatorKey = discriminatorKey;
        return this;
    }

    public EntityBuilder useDiscriminator(boolean useDiscriminator) {
        annotation.useDiscriminator = useDiscriminator;
        return this;
    }

    public EntityBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}