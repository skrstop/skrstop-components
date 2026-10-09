package dev.morphia.annotations.internal;

import dev.morphia.annotations.Property;
import dev.morphia.mapping.Mapper;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class PropertyBuilder {

    private PropertyAnnotation annotation = new PropertyAnnotation();

    private PropertyBuilder() {
        annotation.concreteClass = Object.class;
        annotation.value = Mapper.IGNORED_FIELDNAME;
    }

    public Property build() {
        PropertyAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static PropertyBuilder propertyBuilder() {
        return new PropertyBuilder();
    }

    static private class PropertyAnnotation implements Property {
        private Class<?> concreteClass;
        private String value;

        public Class<Property> annotationType() {
            return Property.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PropertyAnnotation)) {
                return false;
            }
            PropertyAnnotation that = (PropertyAnnotation) o;
            return Objects.equals(concreteClass, that.concreteClass) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(concreteClass, value);
        }

        @Override
        public Class<?> concreteClass() {
            return concreteClass;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static PropertyBuilder propertyBuilder(Property source) {
        PropertyBuilder builder = new PropertyBuilder();
        builder.annotation.concreteClass = source.concreteClass();
        builder.annotation.value = source.value();
        return builder;
    }

    public PropertyBuilder concreteClass(Class<?> concreteClass) {
        annotation.concreteClass = concreteClass;
        return this;
    }

    public PropertyBuilder value(String value) {
        annotation.value = value;
        return this;
    }
}