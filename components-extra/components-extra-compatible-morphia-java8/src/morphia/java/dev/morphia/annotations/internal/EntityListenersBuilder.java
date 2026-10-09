package dev.morphia.annotations.internal;

import dev.morphia.annotations.EntityListeners;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class EntityListenersBuilder {

    private EntityListenersAnnotation annotation = new EntityListenersAnnotation();

    private EntityListenersBuilder() {
        annotation.value = new Class[]{};
    }

    public EntityListeners build() {
        EntityListenersAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static EntityListenersBuilder entityListenersBuilder() {
        return new EntityListenersBuilder();
    }

    static private class EntityListenersAnnotation implements EntityListeners {
        private Class<?>[] value;

        public Class<EntityListeners> annotationType() {
            return EntityListeners.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof EntityListenersAnnotation)) {
                return false;
            }
            EntityListenersAnnotation that = (EntityListenersAnnotation) o;
            return Arrays.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public Class<?>[] value() {
            return value;
        }
    }

    public static EntityListenersBuilder entityListenersBuilder(EntityListeners source) {
        EntityListenersBuilder builder = new EntityListenersBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public EntityListenersBuilder value(Class<?>... value) {
        annotation.value = value;
        return this;
    }
}