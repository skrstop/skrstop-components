package dev.morphia.annotations.internal;

import dev.morphia.annotations.Collation;
import dev.morphia.annotations.IndexOptions;

import java.util.Objects;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class IndexOptionsBuilder {

    private IndexOptionsAnnotation annotation = new IndexOptionsAnnotation();

    private IndexOptionsBuilder() {
        annotation.background = false;
        annotation.disableValidation = false;
        annotation.expireAfterSeconds = -1;
        annotation.language = "";
        annotation.languageOverride = "";
        annotation.name = "";
        annotation.sparse = false;
        annotation.unique = false;
        annotation.partialFilter = "";
        annotation.collation = CollationBuilder.collationBuilder().build();
    }

    public IndexOptions build() {
        IndexOptionsAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static IndexOptionsBuilder indexOptionsBuilder() {
        return new IndexOptionsBuilder();
    }

    static private class IndexOptionsAnnotation implements IndexOptions {
        private boolean background;
        private boolean disableValidation;
        private int expireAfterSeconds;
        private String language;
        private String languageOverride;
        private String name;
        private boolean sparse;
        private boolean unique;
        private String partialFilter;
        private Collation collation;

        public Class<IndexOptions> annotationType() {
            return IndexOptions.class;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IndexOptionsAnnotation)) {
                return false;
            }
            IndexOptionsAnnotation that = (IndexOptionsAnnotation) o;
            return Objects.equals(background, that.background)
                    && Objects.equals(disableValidation, that.disableValidation)
                    && Objects.equals(expireAfterSeconds, that.expireAfterSeconds)
                    && Objects.equals(language, that.language)
                    && Objects.equals(languageOverride, that.languageOverride) && Objects.equals(name, that.name)
                    && Objects.equals(sparse, that.sparse) && Objects.equals(unique, that.unique)
                    && Objects.equals(partialFilter, that.partialFilter) && Objects.equals(collation, that.collation);
        }

        public int hashCode() {
            return Objects.hash(background, disableValidation, expireAfterSeconds, language, languageOverride, name,
                    sparse, unique, partialFilter, collation);
        }

        @Override
        public boolean background() {
            return background;
        }

        @Override
        public boolean disableValidation() {
            return disableValidation;
        }

        @Override
        public int expireAfterSeconds() {
            return expireAfterSeconds;
        }

        @Override
        public String language() {
            return language;
        }

        @Override
        public String languageOverride() {
            return languageOverride;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean sparse() {
            return sparse;
        }

        @Override
        public boolean unique() {
            return unique;
        }

        @Override
        public String partialFilter() {
            return partialFilter;
        }

        @Override
        public Collation collation() {
            return collation;
        }
    }

    public static IndexOptionsBuilder indexOptionsBuilder(IndexOptions source) {
        IndexOptionsBuilder builder = new IndexOptionsBuilder();
        builder.annotation.background = source.background();
        builder.annotation.disableValidation = source.disableValidation();
        builder.annotation.expireAfterSeconds = source.expireAfterSeconds();
        builder.annotation.language = source.language();
        builder.annotation.languageOverride = source.languageOverride();
        builder.annotation.name = source.name();
        builder.annotation.sparse = source.sparse();
        builder.annotation.unique = source.unique();
        builder.annotation.partialFilter = source.partialFilter();
        builder.annotation.collation = source.collation();
        return builder;
    }

    public IndexOptionsBuilder background(boolean background) {
        annotation.background = background;
        return this;
    }

    public IndexOptionsBuilder disableValidation(boolean disableValidation) {
        annotation.disableValidation = disableValidation;
        return this;
    }

    public IndexOptionsBuilder expireAfterSeconds(int expireAfterSeconds) {
        annotation.expireAfterSeconds = expireAfterSeconds;
        return this;
    }

    public IndexOptionsBuilder language(String language) {
        annotation.language = language;
        return this;
    }

    public IndexOptionsBuilder languageOverride(String languageOverride) {
        annotation.languageOverride = languageOverride;
        return this;
    }

    public IndexOptionsBuilder name(String name) {
        annotation.name = name;
        return this;
    }

    public IndexOptionsBuilder sparse(boolean sparse) {
        annotation.sparse = sparse;
        return this;
    }

    public IndexOptionsBuilder unique(boolean unique) {
        annotation.unique = unique;
        return this;
    }

    public IndexOptionsBuilder partialFilter(String partialFilter) {
        annotation.partialFilter = partialFilter;
        return this;
    }

    public IndexOptionsBuilder collation(Collation collation) {
        annotation.collation = collation;
        return this;
    }
}