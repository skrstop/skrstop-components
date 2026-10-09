package dev.morphia.annotations.internal;

import dev.morphia.annotations.AlsoLoad;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class AlsoLoadBuilder {

    private AlsoLoadAnnotation annotation = new AlsoLoadAnnotation();

    private AlsoLoadBuilder() {
    }

    public AlsoLoad build() {
        AlsoLoadAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static AlsoLoadBuilder alsoLoadBuilder() {
        return new AlsoLoadBuilder();
    }

    static private class AlsoLoadAnnotation implements AlsoLoad {
        private String[] value;

        public Class<AlsoLoad> annotationType() {
            return AlsoLoad.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AlsoLoadAnnotation)) {
                return false;
            }
            AlsoLoadAnnotation that = (AlsoLoadAnnotation) o;
            return Arrays.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String[] value() {
            return value;
        }
    }

    public static AlsoLoadBuilder alsoLoadBuilder(AlsoLoad source) {
        AlsoLoadBuilder builder = new AlsoLoadBuilder();
        builder.annotation.value = source.value();
        return builder;
    }

    public AlsoLoadBuilder value(String... value) {
        annotation.value = value;
        return this;
    }
}