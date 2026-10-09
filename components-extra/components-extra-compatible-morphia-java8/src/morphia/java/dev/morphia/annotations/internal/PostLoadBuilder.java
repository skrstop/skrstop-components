package dev.morphia.annotations.internal;

import dev.morphia.annotations.PostLoad;

/**
 * @morphia.internal
 * @since 2.3
 */
@MorphiaInternal
public final class PostLoadBuilder {

    private PostLoadAnnotation annotation = new PostLoadAnnotation();

    private PostLoadBuilder() {
    }

    public PostLoad build() {
        PostLoadAnnotation anno = annotation;
        annotation = null;
        return anno;
    }

    public static PostLoadBuilder postLoadBuilder() {
        return new PostLoadBuilder();
    }

    static private class PostLoadAnnotation implements PostLoad {
        public Class<PostLoad> annotationType() {
            return PostLoad.class;
        }
    }
}