package com.zoe.framework.components.starter.annotation.anno.trim;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zoe.framework.components.starter.annotation.handle.trim.TrimJacksonFormatterSerializer;
import com.zoe.framework.components.starter.annotation.handle.trim.TrimmerType;

import java.lang.annotation.*;

/**
 * @author 蒋时华
 * @date 2021-02-24 14:41:23
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = TrimJacksonFormatterSerializer.class)
public @interface TrimSerialize {

    TrimmerType value() default TrimmerType.LEFT_RIGHT;
}
