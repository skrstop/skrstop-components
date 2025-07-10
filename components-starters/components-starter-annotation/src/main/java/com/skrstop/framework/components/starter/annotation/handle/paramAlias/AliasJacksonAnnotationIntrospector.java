package com.skrstop.framework.components.starter.annotation.handle.paramAlias;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.skrstop.framework.components.starter.annotation.anno.paramAlias.RequestParamAlias;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2025-05-16 10:53:47
 * @since 1.0.0
 */
public class AliasJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public void findEnumAliases(Class<?> enumType, Enum<?>[] enumValues, String[][] aliasList) {
        for (Field f : enumType.getDeclaredFields()) {
            if (f.isEnumConstant()) {
                RequestParamAlias aliasAnnotation = f.getAnnotation(RequestParamAlias.class);
                if (aliasAnnotation != null) {
                    String[] aliases = aliasAnnotation.value();
                    if (aliases.length != 0) {
                        final String name = f.getName();
                        // Find matching enum (could create Ma
                        for (int i = 0, end = enumValues.length; i < end; ++i) {
                            if (name.equals(enumValues[i].name())) {
                                aliasList[i] = aliases;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void findEnumAliases(MapperConfig<?> config, AnnotatedClass annotatedClass,
                                Enum<?>[] enumValues, String[][] aliasList) {
        HashMap<String, String[]> enumToAliasMap = new HashMap<>();
        for (AnnotatedField field : annotatedClass.fields()) {
            RequestParamAlias alias = field.getAnnotation(RequestParamAlias.class);
            if (alias != null) {
                enumToAliasMap.putIfAbsent(field.getName(), alias.value());
            }
        }

        for (int i = 0, end = enumValues.length; i < end; ++i) {
            Enum<?> enumValue = enumValues[i];
            aliasList[i] = enumToAliasMap.getOrDefault(enumValue.name(), new String[]{});
        }
    }

    @Override
    public List<PropertyName> findPropertyAliases(Annotated m) {
        RequestParamAlias ann = _findAnnotation(m, RequestParamAlias.class);
        if (ann == null) {
            return null;
        }
        String[] strs = ann.value();
        final int len = strs.length;
        if (len == 0) {
            return Collections.emptyList();
        }
        List<PropertyName> result = new ArrayList<>(len);
        for (int i = 0; i < len; ++i) {
            result.add(PropertyName.construct(strs[i]));
        }
        return result;
    }
}
