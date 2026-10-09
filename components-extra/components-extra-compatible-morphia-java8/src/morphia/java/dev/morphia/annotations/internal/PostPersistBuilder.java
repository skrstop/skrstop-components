package dev.morphia.annotations.internal;

import dev.morphia.annotations.PostPersist;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class PostPersistBuilder {

    private PostPersistAnnotation annotation = new PostPersistAnnotation();

    private PostPersistBuilder() {
    }

    public PostPersist build() {
        PostPersistAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static PostPersistBuilder postPersistBuilder() {
        return new PostPersistBuilder();
    }

    static private class PostPersistAnnotation implements PostPersist {
        public Class<PostPersist> annotationType() {
            return PostPersist.class;
        }
    }
}