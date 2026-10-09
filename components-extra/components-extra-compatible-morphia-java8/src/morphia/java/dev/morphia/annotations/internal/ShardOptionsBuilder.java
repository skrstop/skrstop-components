package dev.morphia.annotations.internal;

import dev.morphia.annotations.ShardOptions;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ShardOptionsBuilder {

    private ShardOptionsAnnotation annotation = new ShardOptionsAnnotation();

    private ShardOptionsBuilder() {
        annotation.numInitialChunks = -1;
        annotation.presplitHashedZones = false;
        annotation.unique = false;
    }

    public ShardOptions build() {
        ShardOptionsAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ShardOptionsBuilder shardOptionsBuilder() {
        return new ShardOptionsBuilder();
    }

    static private class ShardOptionsAnnotation implements ShardOptions {
        private int numInitialChunks;
        private boolean presplitHashedZones;
        private boolean unique;

        public Class<ShardOptions> annotationType() {
            return ShardOptions.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ShardOptionsAnnotation)) {
                return false;
            }
            ShardOptionsAnnotation that = (ShardOptionsAnnotation) o;
            return Objects.equals(numInitialChunks, that.numInitialChunks)
                    && Objects.equals(presplitHashedZones, that.presplitHashedZones)
                    && Objects.equals(unique, that.unique);
        }

        public int hashCode() {
            return Objects.hash(numInitialChunks, presplitHashedZones, unique);
        }

        @Override
        public int numInitialChunks() {
            return numInitialChunks;
        }

        @Override
        public boolean presplitHashedZones() {
            return presplitHashedZones;
        }

        @Override
        public boolean unique() {
            return unique;
        }
    }

    public static ShardOptionsBuilder shardOptionsBuilder(ShardOptions source) {
        ShardOptionsBuilder builder = new ShardOptionsBuilder();
        builder.annotation.numInitialChunks = source.numInitialChunks();
        builder.annotation.presplitHashedZones = source.presplitHashedZones();
        builder.annotation.unique = source.unique();
        return builder;
    }

    public ShardOptionsBuilder numInitialChunks(int numInitialChunks) {
        annotation.numInitialChunks = numInitialChunks;
        return this;
    }

    public ShardOptionsBuilder presplitHashedZones(boolean presplitHashedZones) {
        annotation.presplitHashedZones = presplitHashedZones;
        return this;
    }

    public ShardOptionsBuilder unique(boolean unique) {
        annotation.unique = unique;
        return this;
    }
}