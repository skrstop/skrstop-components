package dev.morphia.annotations.internal;

import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Text;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class TextBuilder {

    private TextAnnotation annotation = new TextAnnotation();

    private TextBuilder() {
        annotation.options = IndexOptionsBuilder.indexOptionsBuilder().build();
        annotation.value = -1;
    }

    public Text build() {
        TextAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static TextBuilder textBuilder() {
        return new TextBuilder();
    }

    static private class TextAnnotation implements Text {
        private IndexOptions options;
        private int value;

        public Class<Text> annotationType() {
            return Text.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TextAnnotation)) {
                return false;
            }
            TextAnnotation that = (TextAnnotation) o;
            return Objects.equals(options, that.options) && Objects.equals(value, that.value);
        }

        public int hashCode() {
            return Objects.hash(options, value);
        }

        @Override
        public IndexOptions options() {
            return options;
        }

        @Override
        public int value() {
            return value;
        }
    }

    public static TextBuilder textBuilder(Text source) {
        TextBuilder builder = new TextBuilder();
        builder.annotation.options = source.options();
        builder.annotation.value = source.value();
        return builder;
    }

    public TextBuilder options(IndexOptions options) {
        annotation.options = options;
        return this;
    }

    public TextBuilder value(int value) {
        annotation.value = value;
        return this;
    }
}