package dev.morphia.annotations.internal;

import dev.morphia.annotations.ShardKey;
import dev.morphia.annotations.ShardKeys;
import dev.morphia.annotations.ShardOptions;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ShardKeysBuilder {

    private ShardKeysAnnotation annotation = new ShardKeysAnnotation();

    private ShardKeysBuilder() {
        annotation.options = ShardOptionsBuilder.shardOptionsBuilder().build();
    }

    public ShardKeys build() {
        ShardKeysAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ShardKeysBuilder shardKeysBuilder() {
        return new ShardKeysBuilder();
    }

    static private class ShardKeysAnnotation implements ShardKeys {
        private ShardOptions options;
        private ShardKey[] value;

        public Class<ShardKeys> annotationType() {
            return ShardKeys.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ShardKeysAnnotation)) {
                return false;
            }
            ShardKeysAnnotation that = (ShardKeysAnnotation) o;
            return Objects.equals(options, that.options) && Arrays.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(options, value);
        }

        @Override
        public ShardOptions options() {
            return options;
        }

        @Override
        public ShardKey[] value() {
            return value;
        }
    }

    public static ShardKeysBuilder shardKeysBuilder(ShardKeys source) {
        ShardKeysBuilder builder = new ShardKeysBuilder();
        builder.annotation.options = source.options();
        builder.annotation.value = source.value();
        return builder;
    }

    public ShardKeysBuilder options(ShardOptions options) {
        annotation.options = options;
        return this;
    }

    public ShardKeysBuilder value(ShardKey... value) {
        annotation.value = value;
        return this;
    }
}