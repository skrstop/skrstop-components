package dev.morphia.annotations.internal;

import dev.morphia.annotations.ShardKey;
import dev.morphia.mapping.ShardKeyType;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ShardKeyBuilder {

    private ShardKeyAnnotation annotation = new ShardKeyAnnotation();

    private ShardKeyBuilder() {
        annotation.type = ShardKeyType.RANGED;
    }

    public ShardKey build() {
        ShardKeyAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ShardKeyBuilder shardKeyBuilder() {
        return new ShardKeyBuilder();
    }

    static private class ShardKeyAnnotation implements ShardKey {
        private ShardKeyType type;
        private String value;

        public Class<ShardKey> annotationType() {
            return ShardKey.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ShardKeyAnnotation)) {
                return false;
            }
            ShardKeyAnnotation that = (ShardKeyAnnotation) o;
            return Objects.equals(type, that.type) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public ShardKeyType type() {
            return type;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static ShardKeyBuilder shardKeyBuilder(ShardKey source) {
        ShardKeyBuilder builder = new ShardKeyBuilder();
        builder.annotation.type = source.type();
        builder.annotation.value = source.value();
        return builder;
    }

    public ShardKeyBuilder type(ShardKeyType type) {
        annotation.type = type;
        return this;
    }

    public ShardKeyBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}