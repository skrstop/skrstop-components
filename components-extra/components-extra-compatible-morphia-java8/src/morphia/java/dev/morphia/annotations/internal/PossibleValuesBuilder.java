package dev.morphia.annotations.internal;

import dev.morphia.annotations.PossibleValues;

import java.util.Arrays;
import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class PossibleValuesBuilder {

    private PossibleValuesAnnotation annotation = new PossibleValuesAnnotation();

    private PossibleValuesBuilder() {
        annotation.fqcn = true;
    }

    public PossibleValues build() {
        PossibleValuesAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static PossibleValuesBuilder possibleValuesBuilder() {
        return new PossibleValuesBuilder();
    }

    static private class PossibleValuesAnnotation implements PossibleValues {
        private String[] value;
        private boolean fqcn;

        public Class<PossibleValues> annotationType() {
            return PossibleValues.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PossibleValuesAnnotation)) {
                return false;
            }
            PossibleValuesAnnotation that = (PossibleValuesAnnotation) o;
            return Arrays.equals(value, that.value) && Objects.equals(fqcn, that.fqcn);
        }

        public int hashCode() {
            return Objects.hash(value, fqcn);
        }

        @Override
        public String[] value() {
            return value;
        }

        @Override
        public boolean fqcn() {
            return fqcn;
        }
    }

    public static PossibleValuesBuilder possibleValuesBuilder(PossibleValues source) {
        PossibleValuesBuilder builder = new PossibleValuesBuilder();
        builder.annotation.value = source.value();
        builder.annotation.fqcn = source.fqcn();
        return builder;
    }

    public PossibleValuesBuilder value(String... value) {
        annotation.value = value;
        return this;
    }

    public PossibleValuesBuilder fqcn(boolean fqcn) {
        annotation.fqcn = fqcn;
        return this;
    }
}