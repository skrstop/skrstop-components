package dev.morphia.annotations.internal;

import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import dev.morphia.annotations.Validation;

import java.util.Objects;

import static com.mongodb.client.model.ValidationAction.ERROR;
import static com.mongodb.client.model.ValidationLevel.STRICT;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class ValidationBuilder {

    private ValidationAnnotation annotation = new ValidationAnnotation();

    private ValidationBuilder() {
        annotation.level = STRICT;
        annotation.action = ERROR;
    }

    public Validation build() {
        ValidationAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static ValidationBuilder validationBuilder() {
        return new ValidationBuilder();
    }

    static private class ValidationAnnotation implements Validation {
        private String value;
        private ValidationLevel level;
        private ValidationAction action;

        public Class<Validation> annotationType() {
            return Validation.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ValidationAnnotation)) {
                return false;
            }
            ValidationAnnotation that = (ValidationAnnotation) o;
            return Objects.equals(value, that.value) && Objects.equals(level, that.level)
                    && Objects.equals(action, that.action);
        }

        public int hashCode() {
            return Objects.hash(value, level, action);
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public ValidationLevel level() {
            return level;
        }

        @Override
        public ValidationAction action() {
            return action;
        }
    }

    public static ValidationBuilder validationBuilder(Validation source) {
        ValidationBuilder builder = new ValidationBuilder();
        builder.annotation.value = source.value();
        builder.annotation.level = source.level();
        builder.annotation.action = source.action();
        return builder;
    }

    public ValidationBuilder value(String value) {
        annotation.value = value;
        return this;
    }

    public ValidationBuilder level(ValidationLevel level) {
        annotation.level = level;
        return this;
    }

    public ValidationBuilder action(ValidationAction action) {
        annotation.action = action;
        return this;
    }
}