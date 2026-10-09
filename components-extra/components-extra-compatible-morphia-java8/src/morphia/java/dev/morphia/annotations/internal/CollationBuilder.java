package dev.morphia.annotations.internal;

import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import dev.morphia.annotations.Collation;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class CollationBuilder {

    private CollationAnnotation annotation = new CollationAnnotation();

    private CollationBuilder() {
        annotation.backwards = false;
        annotation.caseLevel = false;
        annotation.locale = "";
        annotation.normalization = false;
        annotation.numericOrdering = false;
        annotation.alternate = CollationAlternate.NON_IGNORABLE;
        annotation.caseFirst = CollationCaseFirst.OFF;
        annotation.maxVariable = CollationMaxVariable.PUNCT;
        annotation.strength = CollationStrength.TERTIARY;
    }

    public Collation build() {
        CollationAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static CollationBuilder collationBuilder() {
        return new CollationBuilder();
    }

    static private class CollationAnnotation implements Collation {
        private boolean backwards;
        private boolean caseLevel;
        private String locale;
        private boolean normalization;
        private boolean numericOrdering;
        private CollationAlternate alternate;
        private CollationCaseFirst caseFirst;
        private CollationMaxVariable maxVariable;
        private CollationStrength strength;

        public Class<Collation> annotationType() {
            return Collation.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CollationAnnotation)) {
                return false;
            }
            CollationAnnotation that = (CollationAnnotation) o;
            return Objects.equals(backwards, that.backwards) && Objects.equals(caseLevel, that.caseLevel)
                    && Objects.equals(locale, that.locale) && Objects.equals(normalization, that.normalization)
                    && Objects.equals(numericOrdering, that.numericOrdering)
                    && Objects.equals(alternate, that.alternate) && Objects.equals(caseFirst, that.caseFirst)
                    && Objects.equals(maxVariable, that.maxVariable) && Objects.equals(strength, that.strength);
        }

        public int hashCode() {
            return Objects.hash(backwards, caseLevel, locale, normalization, numericOrdering, alternate, caseFirst,
                    maxVariable, strength);
        }

        @Override
        public boolean backwards() {
            return backwards;
        }

        @Override
        public boolean caseLevel() {
            return caseLevel;
        }

        @Override
        public String locale() {
            return locale;
        }

        @Override
        public boolean normalization() {
            return normalization;
        }

        @Override
        public boolean numericOrdering() {
            return numericOrdering;
        }

        @Override
        public CollationAlternate alternate() {
            return alternate;
        }

        @Override
        public CollationCaseFirst caseFirst() {
            return caseFirst;
        }

        @Override
        public CollationMaxVariable maxVariable() {
            return maxVariable;
        }

        @Override
        public CollationStrength strength() {
            return strength;
        }
    }

    public static CollationBuilder collationBuilder(Collation source) {
        CollationBuilder builder = new CollationBuilder();
        builder.annotation.backwards = source.backwards();
        builder.annotation.caseLevel = source.caseLevel();
        builder.annotation.locale = source.locale();
        builder.annotation.normalization = source.normalization();
        builder.annotation.numericOrdering = source.numericOrdering();
        builder.annotation.alternate = source.alternate();
        builder.annotation.caseFirst = source.caseFirst();
        builder.annotation.maxVariable = source.maxVariable();
        builder.annotation.strength = source.strength();
        return builder;
    }

    public CollationBuilder backwards(boolean backwards) {
        annotation.backwards = backwards;
        return this;
    }

    public CollationBuilder caseLevel(boolean caseLevel) {
        annotation.caseLevel = caseLevel;
        return this;
    }

    public CollationBuilder locale(String locale) {
        annotation.locale = locale;
        return this;
    }

    public CollationBuilder normalization(boolean normalization) {
        annotation.normalization = normalization;
        return this;
    }

    public CollationBuilder numericOrdering(boolean numericOrdering) {
        annotation.numericOrdering = numericOrdering;
        return this;
    }

    public CollationBuilder alternate(CollationAlternate alternate) {
        annotation.alternate = alternate;
        return this;
    }

    public CollationBuilder caseFirst(CollationCaseFirst caseFirst) {
        annotation.caseFirst = caseFirst;
        return this;
    }

    public CollationBuilder maxVariable(CollationMaxVariable maxVariable) {
        annotation.maxVariable = maxVariable;
        return this;
    }

    public CollationBuilder strength(CollationStrength strength) {
        annotation.strength = strength;
        return this;
    }
}